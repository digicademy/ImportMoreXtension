/**
 * ImportElementsOperation - is an extension of a ro.sync.ecss.extensions.commons.operations.InsertFragmentOperation which adds a custom operation
 *  to the Oxygen XML Editor that lets a user insert an XML fragment that consists of multiple elements from an external resource. It is one of the
 *  main classes within the ImportMoreXtension developed at the Digital Academy of the Academy of Sciences and Literature | Mainz.
 * @author Patrick D. Brookshire
 * @version 1.0.0
 */
package org.adwmainz.da.extensions.importmore.operations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.adwmainz.da.extensions.askmore.exceptions.InputDialogClosedException;
import org.adwmainz.da.extensions.askmore.models.HashedArgumentsMap;
import org.adwmainz.da.extensions.askmore.models.SelectableOption;
import org.adwmainz.da.extensions.askmore.utils.ArgumentParser;
import org.adwmainz.da.extensions.askmore.utils.AskMoreArgumentProvider;
import org.adwmainz.da.extensions.importmore.exceptions.ImportMoreXMLException;
import org.adwmainz.da.extensions.importmore.exceptions.ImportMoreXPathException;
import org.adwmainz.da.extensions.importmore.utils.ImportMoreArgumentParser;
import org.adwmainz.da.extensions.importmore.utils.ImportMoreArgumentProvider;
import org.adwmainz.da.extensions.importmore.utils.SaxonUtils;
import org.adwmainz.da.extensions.importmore.utils.StringUtils;
import org.adwmainz.da.extensions.importmore.utils.ViewUtils;
import org.adwmainz.da.extensions.importmore.utils.XMLImportService;

import net.sf.saxon.s9api.XdmNode;
import ro.sync.ecss.extensions.api.ArgumentDescriptor;
import ro.sync.ecss.extensions.api.ArgumentsMap;
import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.ecss.extensions.api.AuthorDocumentController;
import ro.sync.ecss.extensions.api.AuthorOperationException;
import ro.sync.ecss.extensions.api.node.AuthorNode;
import ro.sync.ecss.extensions.commons.operations.InsertFragmentOperation;

public class ImportElementsOperation extends InsertFragmentOperation {
	
	// field
	protected ArgumentDescriptor[] arguments;

	// constructor
	/**
	 * creates a new ImportElementsOperation
	 */
	public ImportElementsOperation() {
		super();

		// get arguments from super classes
		ArgumentDescriptor[] basicArguments = super.getArguments();

		// load localized data
		ResourceBundle rb = ResourceBundle.getBundle("org.adwmainz.da.extensions.importmore.resources.DialogTextBundle");
		
		// set arguments
		arguments = new ArgumentDescriptor[] {
				ImportMoreArgumentProvider.getResourceLocationArgumentDescriptor(), 
				ImportMoreArgumentProvider.getRequestedElementLocationArgumentDescriptor(),
				ImportMoreArgumentProvider.getNamespacePrefixesArgumentDescriptor(),
				ImportMoreArgumentProvider.getNamespaceUrisArgumentDescriptor(),
				ImportMoreArgumentProvider.getSelectableNameExpressionArgumentDescriptor(),
				ImportMoreArgumentProvider.getLocalElementLocationArgumentDescriptor(),
				ImportMoreArgumentProvider.getEqualElementExpressionArgumentDescriptor(),
				AskMoreArgumentProvider.getDialogTitleArgumentDescriptor(rb.getString("IMPORT_ELEMENTS")),
				ImportMoreArgumentProvider.getArgument(basicArguments, AskMoreArgumentProvider.ARGUMENT_INSERT_LOCATION),
				ImportMoreArgumentProvider.getArgument(basicArguments, AskMoreArgumentProvider.ARGUMENT_INSERT_POSITION),
				ImportMoreArgumentProvider.getArgument(basicArguments, AskMoreArgumentProvider.ARGUMENT_GO_TO_NEXT_EDITABLE_POSITION)
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
		String localElementLocation = ArgumentParser.getValidString(args, ImportMoreArgumentProvider.ARGUMENT_LOCAL_ELEMENT_LOCATION);
		String equalElementExpression = ArgumentParser.getValidString(args, ImportMoreArgumentProvider.ARGUMENT_EQUAL_ELEMENT_EXPRESSION);
		String requestedElementLocation = ArgumentParser.getValidString(args, ImportMoreArgumentProvider.ARGUMENT_REQUESTED_ELEMENT_LOCATION);
		String selectableNameExpression = ArgumentParser.getValidString(args, ImportMoreArgumentProvider.ARGUMENT_SELECTABLE_NAME_EXPRESSION);
		
		String dialogTitle = ArgumentParser.getValidString(args, AskMoreArgumentProvider.ARGUMENT_DIALOG_TITLE);
		
		// fetch all options from resource
		Map<String, XdmNode> allNodesWithKeys;
		try {
			allNodesWithKeys = XMLImportService.fetchNodesWithKeys(resourceName, requestedElementLocation, equalElementExpression, namespaceMap);
			if (allNodesWithKeys.isEmpty())
				throw new AuthorOperationException(XMLImportService.getNoResultMessage(resourceName));
		} catch (IOException | ImportMoreXMLException | ImportMoreXPathException ex) {
			throw new IllegalArgumentException(ex);
		}
		
		// remove already imported elements from options
		AuthorDocumentController documentController = authorAccess.getDocumentController();
		for (AuthorNode targetNode: documentController.findNodesByXPath(localElementLocation, false, true, true)) {
			/*String localKey = documentController.evaluateXPath(equalElementExpression, targetNode, false, true, true, true)[0].toString();
			if (allNodesWithKeys.containsKey(localKey))
				allNodesWithKeys.remove(localKey);*/
			// MEMO: alternative that avoids ArrayIndexOutOfBoundsExceptions
			// TODO: throw exception like "The xpath " + xPathExpression + " returned an empty result set." (c.f. SaxonUtils)
			Object[] localKeyElems = documentController.evaluateXPath(equalElementExpression, targetNode, false, true, true, true);
			if (localKeyElems.length > 0) {
				String localKey = localKeyElems[0].toString();
				if (allNodesWithKeys.containsKey(localKey))
					allNodesWithKeys.remove(localKey);
			}			
		}
		
		// build final options and add default namespace
		Set<SelectableOption<String>> options = new LinkedHashSet<>();
		for (String key: allNodesWithKeys.keySet()) {
			XdmNode node = allNodesWithKeys.get(key);
			
			// build real value
			String realValue = node.toString();
			
			// build rendered value
			String renderedValue;
			try {
				renderedValue = SaxonUtils.getFirstXPathResult(node, selectableNameExpression, namespaceMap).getStringValue();
				renderedValue = StringUtils.reduceLength(renderedValue, ViewUtils.MAX_SELECTABLE_ELEMENT_LENGTH);
			} catch (ImportMoreXPathException ex) {
				throw new IllegalArgumentException(ex);
			}
			
			options.add(new SelectableOption<>(realValue, renderedValue));
		}
		
		// build fragment from selected options
		String fragment = "";
		try {
			for (SelectableOption<String> selectedOption: ViewUtils.fetchSelectedOption(dialogTitle, new ArrayList<>(options)))
				fragment += selectedOption.getRealValue();
		} catch (InputDialogClosedException ex) {
			// abort action if user closes the dialog
			throw new AuthorOperationException(ImportMoreArgumentProvider.getAbortedImportMessage());
		}
		
		
		// invoke main action from super class
		HashedArgumentsMap parsedArgs = new HashedArgumentsMap(args, Arrays.asList(
				AskMoreArgumentProvider.ARGUMENT_INSERT_LOCATION, 
				AskMoreArgumentProvider.ARGUMENT_INSERT_POSITION, 
				AskMoreArgumentProvider.ARGUMENT_GO_TO_NEXT_EDITABLE_POSITION)
		);
		parsedArgs.put(AskMoreArgumentProvider.ARGUMENT_FRAGMENT, fragment);
		
		super.doOperation(authorAccess, parsedArgs);
	}
	
	@Override
	public ArgumentDescriptor[] getArguments() {		
		return arguments;
	}

}
