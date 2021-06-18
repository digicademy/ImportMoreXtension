/**
 * DisplayImportChartOperation.java - is an implementation of a ro.sync.ecss.extensions.api.AuthorOperation which adds a custom operation to the
 *  Oxygen XML editor that displays a chart showing the number of elements imported from an external resource. It is one of the main classes within
 *   the ImportMoreXtension developed at the Digital Academy of the Academy of Sciences and Literature | Mainz.
 * @author Patrick D. Brookshire
 * @version 1.0.0
 */
package org.adwmainz.da.extensions.importmore.operations;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.adwmainz.da.extensions.askmore.exceptions.InputDialogClosedException;
import org.adwmainz.da.extensions.askmore.utils.APIAccessUtils;
import org.adwmainz.da.extensions.askmore.utils.ArgumentParser;
import org.adwmainz.da.extensions.askmore.utils.AskMoreArgumentProvider;
import org.adwmainz.da.extensions.importmore.factories.FrameFactory;
import org.adwmainz.da.extensions.importmore.exceptions.ImportMoreXMLException;
import org.adwmainz.da.extensions.importmore.exceptions.ImportMoreXPathException;
import org.adwmainz.da.extensions.importmore.factories.DatasetFactory;
import org.adwmainz.da.extensions.importmore.utils.ImportMoreArgumentParser;
import org.adwmainz.da.extensions.importmore.utils.ImportMoreArgumentProvider;
import org.adwmainz.da.extensions.importmore.utils.ViewUtils;
import org.adwmainz.da.extensions.importmore.utils.XMLImportService;
import org.jfree.data.general.PieDataset;

import ro.sync.ecss.extensions.api.ArgumentDescriptor;
import ro.sync.ecss.extensions.api.ArgumentsMap;
import ro.sync.ecss.extensions.api.AuthorAccess;
import ro.sync.ecss.extensions.api.AuthorDocumentController;
import ro.sync.ecss.extensions.api.AuthorOperation;
import ro.sync.ecss.extensions.api.AuthorOperationException;

public class DisplayImportChartOperation implements AuthorOperation {
	
	// field
	protected ArgumentDescriptor[] arguments;
	
	// constructor
	/**
	 * Creates a new DisplayImportChartOperation
	 */
	public DisplayImportChartOperation() {
		// load localized data
		ResourceBundle rb = ResourceBundle.getBundle("org.adwmainz.da.extensions.importmore.resources.DialogTextBundle");
		
		// set argument descriptions
		arguments = new ArgumentDescriptor[] {
				ImportMoreArgumentProvider.getResourceLocationArgumentDescriptor(), 
				ImportMoreArgumentProvider.getRequestedElementLocationArgumentDescriptor(),
				ImportMoreArgumentProvider.getNamespacePrefixesArgumentDescriptor(),
				ImportMoreArgumentProvider.getNamespaceUrisArgumentDescriptor(),
				ImportMoreArgumentProvider.getLocalElementLocationArgumentDescriptor(),
				ImportMoreArgumentProvider.getDisplayPercentagesArgumentDescriptor(),
				ImportMoreArgumentProvider.getPrecisionArgumentDescriptor(),
				AskMoreArgumentProvider.getDialogTitleArgumentDescriptor(rb.getString("IMPORT_STATS"))
		};
	}

	// overridden methods
	@Override
	public String getDescription() {
		return "Displays a chart that shows how many elements are currently imported from an external resource\n"
				+ " (⚠️ requires jcommon-1.0.16.jar and jfreechart-1.5.0.jar (or compatible releases) in the class path).";
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
		boolean displayPercentages = ArgumentParser.getValidBoolean(args, ImportMoreArgumentProvider.ARGUMENT_DISPLAY_PERCENTAGES);
		int precision = ArgumentParser.getValidInt(args, ImportMoreArgumentProvider.ARGUMENT_PRECISION, 0);
		String dialogTitle = ArgumentParser.getValidString(args, AskMoreArgumentProvider.ARGUMENT_DIALOG_TITLE);
		
		// fetch all distinct fragments from resource
		Set<String> originalFragments;
		try {
			originalFragments = new HashSet<>(XMLImportService.fetchSerializedXPathResults(resourceName, requestedElementLocation, namespaceMap));
			if (originalFragments.isEmpty())
				throw new AuthorOperationException(XMLImportService.getNoResultMessage(resourceName));
		} catch (IOException | ImportMoreXMLException | ImportMoreXPathException ex) {
			throw new IllegalArgumentException(ex);
		}

		// get author document controller
		AuthorDocumentController documentController = authorAccess.getDocumentController();
		
		// calc number of imported fragments
		int numberOfImportedElements = 0;
		for (String localFragment: new HashSet<>(APIAccessUtils.getSerializedXPathResults(documentController, localElementLocation))) {
			if (originalFragments.contains(localFragment))
				++numberOfImportedElements;
		}
		
		// calc number of missing fragments
		int numberOfNotImportedElements = originalFragments.size() - numberOfImportedElements;
		
		// load localized data
		ResourceBundle rb = ResourceBundle.getBundle("org.adwmainz.da.extensions.importmore.resources.DialogTextBundle");
		
		// create dataset
		Map<String, Integer> labeledData = new LinkedHashMap<>();
		labeledData.put(rb.getString("NOT_IMPORTED_ELEMENTS"), numberOfNotImportedElements);
		labeledData.put(rb.getString("IMPORTED_ELEMENTS"), numberOfImportedElements);
		
		PieDataset dataset;
		if (displayPercentages)
			dataset = DatasetFactory.createPieDatasetWithPercentages(labeledData, precision);
		else
			dataset = DatasetFactory.createPieDataset(labeledData);
		
		// show frame
		ViewUtils.showFrame(FrameFactory.createPieChartFrame(dialogTitle, dialogTitle, dataset));
	}

	@Override
	public ArgumentDescriptor[] getArguments() {
		return arguments;
	}

}
