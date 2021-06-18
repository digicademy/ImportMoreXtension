/**
 * ImportAnnotatedFragmentOperation - is an extension of a org.adwmainz.da.extensions.askmore.operations.DisplayInResultsViewOperation which adds a custom
 *  operation to the Oxygen XML Editor that lets a user import elements from an external resource to the so called ResultsView. It is one of the main classes
 *  within the ImportMoreXtension developed at the Digital Academy of the Academy of Sciences and Literature | Mainz.
 * @author Patrick D. Brookshire
 * @version 0.1.0
 */
package org.adwmainz.da.extensions.importmore.operations;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.adwmainz.da.extensions.askmore.exceptions.InputDialogClosedException;
import org.adwmainz.da.extensions.askmore.operations.DisplayInResultsViewOperation;
import org.adwmainz.da.extensions.askmore.utils.APIAccessUtils;
import org.adwmainz.da.extensions.askmore.utils.ArgumentParser;
import org.adwmainz.da.extensions.askmore.utils.AskMoreArgumentProvider;
import org.adwmainz.da.extensions.importmore.exceptions.ImportMoreXMLException;
import org.adwmainz.da.extensions.importmore.exceptions.ImportMoreXPathException;
import org.adwmainz.da.extensions.importmore.utils.ImportMoreAnnotationParser;
import org.adwmainz.da.extensions.importmore.utils.ImportMoreArgumentParser;
import org.adwmainz.da.extensions.importmore.utils.ImportMoreArgumentProvider;
import org.adwmainz.da.extensions.importmore.utils.XMLImportService;

import net.sf.saxon.s9api.XdmNode;
import ro.sync.document.DocumentPositionedInfo;
import ro.sync.ecss.extensions.api.ArgumentDescriptor;
import ro.sync.ecss.extensions.api.ArgumentsMap;
import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.ecss.extensions.api.AuthorDocumentController;
import ro.sync.ecss.extensions.api.AuthorOperationException;
import ro.sync.exml.workspace.api.PluginWorkspaceProvider;
import ro.sync.exml.workspace.api.results.ResultsManager;
import ro.sync.exml.workspace.api.results.ResultsManager.ResultType;

public class ImportToResultsViewOperation extends DisplayInResultsViewOperation {

	// field
	protected ArgumentDescriptor[] arguments;

	// constructor
	/**
	 * Creates a new ImportToResultsViewOperation
	 */
	public ImportToResultsViewOperation() {
		super();

		// load localized data
		ResourceBundle rb = ResourceBundle.getBundle("org.adwmainz.da.extensions.importmore.resources.DialogTextBundle");

		// set arguments
		arguments = new ArgumentDescriptor[] {
				ImportMoreArgumentProvider.getResourceLocationArgumentDescriptor(), 
				ImportMoreArgumentProvider.getRequestedElementLocationArgumentDescriptor(),
				ImportMoreArgumentProvider.getNamespacePrefixesArgumentDescriptor(),
				ImportMoreArgumentProvider.getNamespaceUrisArgumentDescriptor(),
				ImportMoreArgumentProvider.getResultsViewMessageArgumentDescriptor(""),
				ImportMoreArgumentProvider.getSystemIdArgumentDescriptor(),
				AskMoreArgumentProvider.getNoResultMessageArgumentDescriptor(),
				AskMoreArgumentProvider.getResultsTabNameArgumentDescriptor(rb.getString("IMPORTED_ELEMENTS")),
				AskMoreArgumentProvider.getSeverityArgumentDescriptor()
		};
	}


	
	// overridden methods
	@Override
	public String getDescription() {
		return "Opens a dialog to import multiple elements from an external resource that are not yet present within the current document.";
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
		String message = ArgumentParser.getValidString(args, AskMoreArgumentProvider.ARGUMENT_MESSAGE);
		String noResultMessage = ArgumentParser.getValidString(args, AskMoreArgumentProvider.ARGUMENT_NO_RESULT_MESSAGE);
		String systemId = ArgumentParser.getValidString(args, ImportMoreArgumentProvider.ARGUMENT_SYSTEM_ID, "-");
		String resultsTabName = ArgumentParser.getValidString(args, AskMoreArgumentProvider.ARGUMENT_RESULTS_TAB_NAME);
		int severity = APIAccessUtils.getSeverity(ArgumentParser.getValidString(args, AskMoreArgumentProvider.ARGUMENT_SEVERITY, "Info"));

		// get controller
		AuthorDocumentController controller = authorAccess.getDocumentController();
		
		// prepare results view
		ResultsManager resManager = PluginWorkspaceProvider.getPluginWorkspace().getResultsManager();
		String localSystemId = controller.getAuthorDocumentNode().getSystemID();
		resultsTabName += " - " + new File(localSystemId).getName();
		resManager.setResults(resultsTabName, null, null); // clear tab

		// fetch all options from resource
		List<XdmNode> importedNodes;
		try {
			importedNodes = XMLImportService.fetchNodes(resourceName, requestedElementLocation, namespaceMap);
			if (importedNodes.isEmpty())
				throw new AuthorOperationException(noResultMessage);
		} catch (IOException | ImportMoreXMLException | ImportMoreXPathException ex) {
			throw new IllegalArgumentException(ex);
		}
		
		// add results
		for (XdmNode importedNode: importedNodes) {
			try {
				String resultMessage = ImportMoreAnnotationParser.replaceAnnotations(message, importedNode, namespaceMap);
				String resultSystemId = ImportMoreAnnotationParser.replaceAnnotations(systemId, importedNode, namespaceMap);
				DocumentPositionedInfo info = new DocumentPositionedInfo(severity, resultMessage, resultSystemId);
				resManager.addResult(resultsTabName, info, ResultType.GENERIC, true, false);
			} catch (ImportMoreXMLException | ImportMoreXPathException ex) {
				throw new IllegalArgumentException(ex);
			}
		}
	}
	
	@Override
	public ArgumentDescriptor[] getArguments() {		
		return arguments;
	}
	
}
