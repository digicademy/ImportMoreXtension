/**
 * APIAccessUtils.java - is a helper class providing methods related to the main Oxygen API as used within the ImportMoreXtension developed at the
 *  Digital Academy of the Academy of Sciences and Literature | Mainz.
 * @author Patrick D. Brookshire
 * @version 1.0.0
 */
package org.adwmainz.da.extensions.importmore.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.text.BadLocationException;

import ro.sync.ecss.dom.wrappers.AuthorNodeDomWrapper;
import ro.sync.ecss.extensions.api.AuthorDocumentController;
import ro.sync.ecss.extensions.api.AuthorOperationException;
import ro.sync.ecss.extensions.api.node.AuthorNode;

public class APIAccessUtils {
	
	/**
	 * Returns a Map of serialized XPath results and their respective AuthorNodes for a given XPath expression
	 * @param documentController the current AuthorDocumentController
	 * @param xPathExpression an XPath expression
	 * @throws AuthorOperationException
	 * @throws BadLocationException
	 */
	public static Map<String, AuthorNode> getSerializedXPathResultsWithTargetNode(AuthorDocumentController documentController, String xPathExpression)
			throws AuthorOperationException, BadLocationException {
		// get raw results
		Object[] rawResults = documentController.evaluateXPath(xPathExpression, false, true, true);
		
		// format to list of strings
		Map<String, AuthorNode> results = new LinkedHashMap<>();
		for (Object rawResult: rawResults) {
			if (rawResult instanceof AuthorNodeDomWrapper) {
				AuthorNode targetNode = ((AuthorNodeDomWrapper) rawResult).getWrappedAuthorNode();
				String serializedTargetNode = org.adwmainz.da.extensions.askmore.utils.APIAccessUtils.serializeAuthorNode(documentController, targetNode);
				results.put(serializedTargetNode, targetNode);
			}
			else {
				results.put(rawResult.toString(), null);
			}
		}
		return results;
	}
	
}
