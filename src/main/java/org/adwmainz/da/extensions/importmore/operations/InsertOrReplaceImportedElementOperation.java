/**
 * InsertOrReplaceImportedFragmentOperation.java - is an extension of a ro.sync.ecss.extensions.commons.operations.InsertOrReplaceFragmentOperation which
 *  adds a custom operation to the Oxygen XML Editor that lets a user insert an XML fragment (or replace an existing one) from an external resource. 
 *  It is one of the main classes within the ImportMoreXtension developed at the Digital Academy of the Academy of Sciences and Literature | Mainz.
 * @author Patrick D. Brookshire
 * @version 1.0.0
 */
package org.adwmainz.da.extensions.importmore.operations;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.adwmainz.da.extensions.askmore.exceptions.InputDialogClosedException;
import org.adwmainz.da.extensions.askmore.models.HashedArgumentsMap;
import org.adwmainz.da.extensions.askmore.utils.ArgumentParser;
import org.adwmainz.da.extensions.askmore.utils.AskMoreArgumentProvider;
import org.adwmainz.da.extensions.importmore.exceptions.ImportMoreXMLException;
import org.adwmainz.da.extensions.importmore.exceptions.ImportMoreXPathException;
import org.adwmainz.da.extensions.importmore.utils.ImportMoreArgumentParser;
import org.adwmainz.da.extensions.importmore.utils.ImportMoreArgumentProvider;
import org.adwmainz.da.extensions.importmore.utils.XMLImportService;

import net.sf.saxon.s9api.XdmNode;
import ro.sync.ecss.extensions.api.ArgumentDescriptor;
import ro.sync.ecss.extensions.api.ArgumentsMap;
import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.ecss.extensions.api.AuthorOperationException;
import ro.sync.ecss.extensions.api.access.AuthorEditorAccess;
import ro.sync.ecss.extensions.commons.operations.InsertOrReplaceFragmentOperation;

public class InsertOrReplaceImportedElementOperation extends InsertOrReplaceFragmentOperation {
	
	// field
	protected ArgumentDescriptor[] arguments;

	// constructor
	/**
	 * Creates a new InsertOrReplaceImportedFragmentOperation
	 */
	public InsertOrReplaceImportedElementOperation() {
		super();
		
		// get arguments from super class
		ArgumentDescriptor[] basicArguments = super.getArguments();

		// set arguments
		arguments = new ArgumentDescriptor[] {
				ImportMoreArgumentProvider.getResourceLocationArgumentDescriptor(), 
				ImportMoreArgumentProvider.getRequestedElementLocationArgumentDescriptor(),
				ImportMoreArgumentProvider.getNamespacePrefixesArgumentDescriptor(),
				ImportMoreArgumentProvider.getNamespaceUrisArgumentDescriptor(),
				ImportMoreArgumentProvider.getArgument(basicArguments, AskMoreArgumentProvider.ARGUMENT_INSERT_LOCATION),
				ImportMoreArgumentProvider.getArgument(basicArguments, AskMoreArgumentProvider.ARGUMENT_INSERT_POSITION),
				ImportMoreArgumentProvider.getArgument(basicArguments, AskMoreArgumentProvider.ARGUMENT_GO_TO_NEXT_EDITABLE_POSITION),
				AskMoreArgumentProvider.getRemoveSelectionArgumentDescriptor()
		};
		
	}

	// overridden methods
	@Override
	public String getDescription() {
		return "Extends the default InsertOrReplaceFragmentOperation by adding the possibility to get the fragment from an external resource.";
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
		
		// import first element
		String serializedImportedNode;
		try {
			List<XdmNode> importedNodes = XMLImportService.fetchNodes(resourceName, requestedElementLocation, namespaceMap);
			if (importedNodes.isEmpty())
				throw new AuthorOperationException(XMLImportService.getNoResultMessage(resourceName));
			serializedImportedNode = importedNodes.get(0).toString();
		} catch (IOException | ImportMoreXMLException | ImportMoreXPathException ex) {
			throw new IllegalArgumentException(ex);
		}
		
		// invoke main action of super class
		HashedArgumentsMap parsedArgs = new HashedArgumentsMap(args, Arrays.asList(
				AskMoreArgumentProvider.ARGUMENT_INSERT_LOCATION, 
				AskMoreArgumentProvider.ARGUMENT_INSERT_POSITION, 
				AskMoreArgumentProvider.ARGUMENT_GO_TO_NEXT_EDITABLE_POSITION)
		);
		parsedArgs.put(AskMoreArgumentProvider.ARGUMENT_FRAGMENT, serializedImportedNode);
		
		// prevent super class from removing selected content per default
		String insertLocation = ArgumentParser.getValidString(args, AskMoreArgumentProvider.ARGUMENT_INSERT_LOCATION, "");
		if (!insertLocation.isEmpty()) {
			AuthorEditorAccess editorAccess = authorAccess.getEditorAccess();
			editorAccess.setCaretPosition(editorAccess.getSelectionStart() + 1);
		}
		super.doOperation(authorAccess, parsedArgs);
	}
	
	@Override
	public ArgumentDescriptor[] getArguments() {		
		return arguments;
	}

}
