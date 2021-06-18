/**
 * XMLImportService.java - is a helper class that provides methods for fetching data from an XML resource other than the one currently opened in oXygen.
 *  It is one of the main classes within the ImportMoreXtension developed at the Digital Academy of the Academy of Sciences and Literature | Mainz.
 * @author Patrick D. Brookshire
 * @version 1.0.0
 */
package org.adwmainz.da.extensions.importmore.utils;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.adwmainz.da.extensions.askmore.models.SelectableOption;
import org.adwmainz.da.extensions.importmore.exceptions.ImportMoreXMLException;
import org.adwmainz.da.extensions.importmore.exceptions.ImportMoreXPathException;
import org.adwmainz.da.extensions.importmore.factories.FrameFactory;
import org.adwmainz.da.extensions.importmore.factories.URLFactory;
import org.adwmainz.da.extensions.importmore.views.BasicFrame;

import net.sf.saxon.s9api.XdmNode;

public class XMLImportService {

	// main methods
	/**
	 * Fetches a List of serialized XPath results from the given resource while displaying a loading dialog
	 * @param resourcePath the path to a resource
	 * @param xPathExpression an XPath expression
	 * @param namespaceMap a Map of namespace prefixes and their respective namespace URIs
	 * @throws IOException
	 * @throws ImportMoreXMLException if the resource cannot be parsed
	 * @throws ImportMoreXPathException if the XPath expression is erroneous
	 */
	public static List<String> fetchSerializedXPathResults(String resourcePath, String xPathExpression, Map<String, String> namespaceMap)
			throws IOException, ImportMoreXMLException, ImportMoreXPathException {
		BasicFrame loadingFrame = FrameFactory.createLoadingFrame();
		ViewUtils.showFrame(loadingFrame);
		try {
			XdmNode doc = SaxonUtils.fetchXMLResource(URLFactory.create(resourcePath));
			return SaxonUtils.getSerializedXPathResults(doc, xPathExpression, namespaceMap);
		} finally {
			loadingFrame.dispose();
		}
	}

	/**
	 * Fetches a List of nodes identified by a given XPath expression from the given resource while displaying a loading dialog
	 * @param resourcePath the path to a resource
	 * @param xPathExpression an XPath expression
	 * @param namespaceMap a Map of namespace prefixes and their respective namespace URIs
	 * @throws IOException
	 * @throws ImportMoreXMLException if the resource cannot be parsed
	 * @throws ImportMoreXPathException if the XPath expression does not identify nodes or is erroneous
	 */
	public static List<XdmNode> fetchNodes(String resourcePath, String xPathExpression, Map<String, String> namespaceMap)
			throws IOException, ImportMoreXMLException, ImportMoreXPathException {
		BasicFrame loadingFrame = FrameFactory.createLoadingFrame();
		ViewUtils.showFrame(loadingFrame);
		try {
			XdmNode doc = SaxonUtils.fetchXMLResource(URLFactory.create(resourcePath));
			return SaxonUtils.getNodes(doc, xPathExpression, namespaceMap);
		} finally {
			loadingFrame.dispose();
		}
	}
	
	/**
	 * Fetches a Set of nodes as SelectableOptions from the given resource while displaying a loading dialog
	 * @param resourceName the path to a resource
	 * @param xPathExpression an XPath expression identifying the real values of each SelectableOption to be created
	 * @param renderedValueExpression an XPath expression identifying the rendered value of each SelectableOption to be created using the real value as context node
	 * @param namespaceMap a Map of namespace prefixes and their respective namespace URIs
	 * @throws IOException
	 * @throws ImportMoreXMLException if the resource cannot be parsed
	 * @throws ImportMoreXPathException if the XPath expression does not identify nodes or is erroneous
	 */
	public static Set<SelectableOption<XdmNode>> fetchSelectableNodes(String resourceName, String xPathExpression, String renderedValueExpression, Map<String, String> namespaceMap)
			throws IOException, ImportMoreXMLException, ImportMoreXPathException {
		BasicFrame loadingFrame = FrameFactory.createLoadingFrame();
		ViewUtils.showFrame(loadingFrame);
		try {
			// fetch resource
			XdmNode doc = SaxonUtils.fetchXMLResource(URLFactory.create(resourceName));
			
			// iterate over base nodes to build map
			Set<SelectableOption<XdmNode>> resultOptions = new LinkedHashSet<>();
			for (XdmNode node: SaxonUtils.getNodes(doc, xPathExpression, namespaceMap)) {
				String renderedValue = SaxonUtils.getFirstXPathResult(node, renderedValueExpression, namespaceMap).getStringValue();
				resultOptions.add(new SelectableOption<>(node, StringUtils.reduceLength(renderedValue, ViewUtils.MAX_SELECTABLE_ELEMENT_LENGTH)));
			}
			return resultOptions;
		} finally {
			loadingFrame.dispose();
		}
	}
	
	/**
	 * Fetches a List of nodes with keys from the given resource while displaying a loading dialog
	 * @param resourcePath the path to a resource
	 * @param xPathExpression an XPath expression
	 * @param keyExpression an XPath expression identifying the Map key using its value as context node
	 * @param namespaceMap a Map of namespace prefixes and their respective namespace URIs
	 * @throws IOException
	 * @throws ImportMoreXMLException if the resource cannot be parsed
	 * @throws ImportMoreXPathException if the XPath expression does not identify nodes or is erroneous
	 */
	public static Map<String, XdmNode> fetchNodesWithKeys(String resourceName, String xPathExpression, String keyExpression, Map<String, String> namespaceMap)
			throws IOException, ImportMoreXMLException, ImportMoreXPathException {
		BasicFrame loadingFrame = FrameFactory.createLoadingFrame();
		ViewUtils.showFrame(loadingFrame);
		try {
			// fetch resource
			XdmNode doc = SaxonUtils.fetchXMLResource(URLFactory.create(resourceName));
			
			// iterate over base nodes to build map
			Map<String, XdmNode> resultMap = new LinkedHashMap<>();
			for (XdmNode node: SaxonUtils.getNodes(doc, xPathExpression, namespaceMap)) {
				String key = SaxonUtils.getFirstXPathResult(node, keyExpression, namespaceMap).getStringValue();
				resultMap.put(key, node);
			}
			return resultMap;
		} finally {
			loadingFrame.dispose();
		}
	}
	
	// helper methods
	/**
	 * Returns a message noting that one of the fetch methods of this class did not return any results
	 * @param resourcePath the path to a resource
	 */
	public static String getNoResultMessage(String resourcePath) {
		return "Could not fetch any items from " + resourcePath;
	}

}
