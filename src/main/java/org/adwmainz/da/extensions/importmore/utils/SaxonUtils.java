/**
 * SaxonUtils.java - is a helper class providing methods related to the main Saxon API as used within the ImportMoreXtension developed at the
 *  Digital Academy of the Academy of Sciences and Literature | Mainz.
 * @author Patrick D. Brookshire
 * @version 1.0.0
 */
package org.adwmainz.da.extensions.importmore.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.xml.transform.stream.StreamSource;

import org.adwmainz.da.extensions.importmore.exceptions.ImportMoreXMLException;
import org.adwmainz.da.extensions.importmore.exceptions.ImportMoreXPathException;

import net.sf.saxon.s9api.DocumentBuilder;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.XPathCompiler;
import net.sf.saxon.s9api.XPathSelector;
import net.sf.saxon.s9api.XdmItem;
import net.sf.saxon.s9api.XdmNode;

public class SaxonUtils {
	
	// static field
	protected static Processor processor = new Processor(false);
	
	// methods
	/**
	 * Deserializes a given String to a Document node
	 * @param serializedXml a serialized XML Document node
	 * @throws ImportMoreXMLException if serializedXml cannot be deserialized to a Document node
	 */
	public static XdmNode buildDocument(String serializedXml) throws ImportMoreXMLException {
		DocumentBuilder builder = processor.newDocumentBuilder();
		try {
			return builder.build(new StreamSource(new StringReader(serializedXml)));
		} catch (SaxonApiException ex) {
			throw new ImportMoreXMLException("Cannot parse " + serializedXml, ex);
		}
	}
	
	/**
	 * Fetches an XML resource from the given URL
	 * @param url a URL
	 * @throws IOException
	 * @throws ImportMoreXMLException if the resource cannot be parsed
	 */
	public static XdmNode fetchXMLResource(URL url) throws IOException, ImportMoreXMLException {
		try (InputStream stream = url.openStream()) {
			DocumentBuilder builder = processor.newDocumentBuilder();
			try {
				return builder.build(new StreamSource(stream));
			} catch (SaxonApiException ex) {
				throw new ImportMoreXMLException("Cannot parse XML from " + url.toString(), ex);
			}
		}
	}

	// XPath methods	
	protected static XPathSelector getXPathSelector(XdmNode contextNode, String xPathExpression, Map<String, String> namespaceMap) throws SaxonApiException {
		// init compiler and declare namespaces
		XPathCompiler compiler = processor.newXPathCompiler();
		for (Map.Entry<String, String> entry : namespaceMap.entrySet())
			compiler.declareNamespace(entry.getKey(), entry.getValue());
		
		// get selector
	    XPathSelector selector = compiler.compile(xPathExpression).load();
		selector.setContextItem(contextNode);
		return selector;
	}

	/**
	 * Returns a List of serialized XPath results 
	 * @param contextNode the context node the XPath expression should be evaluated from
	 * @param xPathExpression an XPath expression
	 * @param namespaceMap a Map of namespace prefixes and their respective namespace URIs
	 * @throws ImportMoreXPathException if the XPath expression is erroneous
	 */
	public static List<String> getSerializedXPathResults(XdmNode contextNode, String xPathExpression, Map<String, String> namespaceMap)
			throws ImportMoreXPathException {
	    List<String> results = new ArrayList<>();
	    try {
			for (XdmItem item: getXPathSelector(contextNode, xPathExpression, namespaceMap).evaluate())
				results.add(item.toString());
		} catch (SaxonApiException ex) {
			throw new ImportMoreXPathException("Cannot compile the xpath " + xPathExpression, ex);
		}
	    return results;
	}
	
	/**
	 * Returns a List of nodes identified by a given XPath expression
	 * @param contextNode the context node the XPath expression should be evaluated from
	 * @param xPathExpression an XPath expression
	 * @param namespaceMap a Map of namespace prefixes and their respective namespace URIs
	 * @throws ImportMoreXPathException if the XPath expression does not identify nodes or is erroneous
	 */
	public static List<XdmNode> getNodes(XdmNode contextNode, String xPathExpression, Map<String, String> namespaceMap) throws ImportMoreXPathException {
	    List<XdmNode> results = new ArrayList<>();
	    try {
			for (XdmItem item: getXPathSelector(contextNode, xPathExpression, namespaceMap).evaluate()) {
				if (!(item instanceof XdmNode))
					throw new ImportMoreXPathException("The XPath "+xPathExpression+" does not identify any nodes");
				results.add((XdmNode) item);
			}
		} catch (SaxonApiException ex) {
			throw new ImportMoreXPathException("Cannot compile the xpath " + xPathExpression, ex);
		}
	    return results;
	}
	
	/**
	 * Returns the first item identified by a given XPath expression
	 * @param contextNode the context node the XPath expression should be evaluated from
	 * @param xPathExpression an XPath expression
	 * @param namespaceMap a Map of namespace prefixes and their respective namespace URIs
	 * @throws ImportMoreXPathException if the XPath expression is erroneous
	 */
	public static XdmItem getFirstXPathResult(XdmNode contextNode, String xPathExpression, Map<String, String> namespaceMap) throws ImportMoreXPathException {
		Optional<XdmItem> firstResult;
		try {
			firstResult = Optional.ofNullable(getXPathSelector(contextNode, xPathExpression, namespaceMap).evaluateSingle());
		} catch (SaxonApiException ex) {
			throw new ImportMoreXPathException("Cannot compile the xpath " + xPathExpression, ex);
		}
		if (firstResult.isPresent())
	    	return firstResult.get();
	    throw new ImportMoreXPathException("The xpath " + xPathExpression + " returned an empty result set.");
	}

}
