/**
 * IsInListCheckOperation - is an implementation of a ro.sync.ecss.extensions.api.AuthorOperation which adds a custom operation to the Oxygen XML
 *  Editor that checks whether the current state of specific elements differs from there state in an external resource. It is one of the main
 *  classes within the ImportMoreXtension developed at the Digital Academy of the Academy of Sciences and Literature | Mainz.
 * @author Patrick D. Brookshire
 * @version 1.0.0
 */
package org.adwmainz.da.extensions.importmore.operations;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.text.BadLocationException;

import org.adwmainz.da.extensions.askmore.exceptions.InputDialogClosedException;
import org.adwmainz.da.extensions.askmore.utils.ArgumentParser;
import org.adwmainz.da.extensions.askmore.utils.AskMoreArgumentProvider;
import org.adwmainz.da.extensions.importmore.exceptions.ImportMoreXMLException;
import org.adwmainz.da.extensions.importmore.exceptions.ImportMoreXPathException;
import org.adwmainz.da.extensions.importmore.utils.APIAccessUtils;
import org.adwmainz.da.extensions.importmore.utils.ImportMoreArgumentParser;
import org.adwmainz.da.extensions.importmore.utils.ImportMoreArgumentProvider;
import org.adwmainz.da.extensions.importmore.utils.XMLImportService;

import ro.sync.document.DocumentPositionedInfo;
import ro.sync.ecss.component.validation.AuthorDocumentPositionedInfo;
import ro.sync.ecss.extensions.api.ArgumentDescriptor;
import ro.sync.ecss.extensions.api.ArgumentsMap;
import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.ecss.extensions.api.AuthorDocumentController;
import ro.sync.ecss.extensions.api.AuthorOperation;
import ro.sync.ecss.extensions.api.AuthorOperationException;
import ro.sync.ecss.extensions.api.node.AuthorNode;
import ro.sync.exml.workspace.api.PluginWorkspaceProvider;
import ro.sync.exml.workspace.api.results.ResultsManager;
import ro.sync.exml.workspace.api.results.ResultsManager.ResultType;

public class IsInListCheckOperation implements AuthorOperation {
	
	protected static final String ARGUMENT_REPLACING_FRAGMENT = "replacingFragment";
	
	// field
	protected ArgumentDescriptor[] arguments;

	// constructor
	/**
	 * Creates a new IsInListCheckOperation
	 */
	public IsInListCheckOperation() {
		super();
		
		// load localized data
		ResourceBundle rb = ResourceBundle.getBundle("org.adwmainz.da.extensions.importmore.resources.DialogTextBundle");
				
		arguments = new ArgumentDescriptor[] {
				ImportMoreArgumentProvider.getResourceLocationArgumentDescriptor(),
				ImportMoreArgumentProvider.getRequestedElementLocationArgumentDescriptor(),
				ImportMoreArgumentProvider.getNamespacePrefixesArgumentDescriptor(),
				ImportMoreArgumentProvider.getNamespaceUrisArgumentDescriptor(),
				ImportMoreArgumentProvider.getLocalElementLocationArgumentDescriptor(),
				AskMoreArgumentProvider.getResultsTabNameArgumentDescriptor(rb.getString("DIFFS_FOUND")),
				AskMoreArgumentProvider.getResultsViewMessageArgumentDescriptor(rb.getString("DIFF_FOUND")),
				AskMoreArgumentProvider.getNoResultMessageArgumentDescriptor()
		};
		
		
	}
	
	// overridden methods
	@Override
	public String getDescription() {
		return "Checks whether elements in the current document differ from corresponding ones from an external resource or not.";
	}

	@Override
	public void doOperation(AuthorAccess authorAccess, ArgumentsMap args)
			throws IllegalArgumentException, AuthorOperationException {
		// try to get url from an input dialog
		String resourceName;
		try {
			resourceName = ArgumentParser.getValidStringWithUserInput(args, ImportMoreArgumentProvider.ARGUMENT_RESOURCE_LOCATION);
		} catch (InputDialogClosedException ex) {
			// abort action if user closes the dialog
			throw new IllegalArgumentException(AskMoreArgumentProvider.getClosedDialogMessage());
		}
		
		// get other params
		Map<String, String> namespaceMap = ImportMoreArgumentParser.getValidMap(args, ImportMoreArgumentProvider.ARGUMENT_NAMESPACE_PREFIXES, ImportMoreArgumentProvider.ARGUMENT_NAMESPACE_URIS);
		String requestedElementLocation = ArgumentParser.getValidString(args, ImportMoreArgumentProvider.ARGUMENT_REQUESTED_ELEMENT_LOCATION);
		String localElementLocation = ArgumentParser.getValidString(args, ImportMoreArgumentProvider.ARGUMENT_LOCAL_ELEMENT_LOCATION);		
		String resultsTabName = ArgumentParser.getValidString(args, AskMoreArgumentProvider.ARGUMENT_RESULTS_TAB_NAME);
		String resultMessage = ArgumentParser.getValidString(args, AskMoreArgumentProvider.ARGUMENT_MESSAGE);
		String noResultMessage = ArgumentParser.getValidString(args, AskMoreArgumentProvider.ARGUMENT_NO_RESULT_MESSAGE);

		// get author document controller
		AuthorDocumentController documentController = authorAccess.getDocumentController();
		
		// prepare results view
		String systemID = documentController.getAuthorDocumentNode().getSystemID();
		ResultsManager resultsManager = PluginWorkspaceProvider.getPluginWorkspace().getResultsManager();
		resultsTabName += " - " + new File(systemID).getName();
		resultsManager.setResults(resultsTabName, null, null); 

		// fetch all options from resource
		List<String> originalFragments;
		try {
			originalFragments = XMLImportService.fetchSerializedXPathResults(resourceName, requestedElementLocation, namespaceMap);
			if (originalFragments.isEmpty())
				throw new AuthorOperationException(noResultMessage);
		} catch (IOException | ImportMoreXMLException | ImportMoreXPathException ex) {
			throw new IllegalArgumentException(ex);
		}
		
		// get local fragments with their respective target nodes
		Map<String, AuthorNode> localFragmentsWithTargetNode;
		try {
			localFragmentsWithTargetNode = APIAccessUtils.getSerializedXPathResultsWithTargetNode(documentController, localElementLocation);
		} catch (BadLocationException ex) {
			throw new IllegalArgumentException(ex);
		}

		// iterate over author nodes to find diffs
		int numberOfDiffs = 0;
		for (String localFragment: localFragmentsWithTargetNode.keySet()) {
			// add fragments that cannot be found in the resource to ResultsView
			if (!originalFragments.contains(localFragment)) {
				String message = resultMessage + " - " + localFragment;
				AuthorDocumentPositionedInfo result = new AuthorDocumentPositionedInfo(DocumentPositionedInfo.SEVERITY_WARN, message, systemID, localFragmentsWithTargetNode.get(localFragment));
				resultsManager.addResult(resultsTabName, result, ResultType.PROBLEM, true, false);
				++numberOfDiffs;
			}
		}
				
		// try to exit early if there are no results
		ResourceBundle rb = ResourceBundle.getBundle("org.adwmainz.da.extensions.importmore.resources.DialogTextBundle");
		if (numberOfDiffs == 0) {
			JOptionPane.showMessageDialog(null, noResultMessage);
			return;
		}

		// notify user and add event handler
		JOptionPane.showMessageDialog(null, String.format(rb.getString("FOUND_X_DIFFS"), numberOfDiffs));
	}
	
	@Override
	public ArgumentDescriptor[] getArguments() {		
		return arguments;
	}

}
