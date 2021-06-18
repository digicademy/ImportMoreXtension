/**
 * ViewUtils.java - is a controller like helper class for the different views used within the AskMoreXtension developed at the Digital Academy of
 *  the Academy of Sciences and Literature | Mainz.
 * @author Patrick D. Brookshire
 * @version 1.0.0
 */
package org.adwmainz.da.extensions.importmore.utils;

import java.util.List;
import javax.swing.WindowConstants;

import org.adwmainz.da.extensions.askmore.exceptions.InputDialogClosedException;
import org.adwmainz.da.extensions.askmore.utils.InputDialogUtils;
import org.adwmainz.da.extensions.importmore.models.CheckboxListCellRenderer;
import org.adwmainz.da.extensions.importmore.views.BasicFrame;
import org.adwmainz.da.extensions.importmore.views.MultiSelectionDialog;

public class ViewUtils {
	
	// constant values
	public static final int DEFAULT_VISIBLE_ROW_COUNT = 11;
	public static final int MAX_SELECTABLE_ELEMENT_LENGTH = 100;
	
	/**
	 * Returns the selected option from an input dialog 
	 * @param <T> the type of the options and the returned value
	 * @param dialogTitle the title the generated dialog should have
	 * @param options the set of options that should be selectable in the generated selection field
	 * @param visibleRowCount the number of options that should be visible (without scrolling)
	 * @throws InputDialogClosedException if the input dialog is closed
	 */
	public static <T> List<T> fetchSelectedOption(String dialogTitle, List<T> options, int visibleRowCount) throws InputDialogClosedException {
		// create dialog
		MultiSelectionDialog<T> dialog = new MultiSelectionDialog<>(null, true, options);
		dialog.setTitle(dialogTitle);
		dialog.setVisibleRowCount(visibleRowCount);
		dialog.setCellRenderer(new CheckboxListCellRenderer<T>());
		
		// configure and show dialog
		InputDialogUtils.showDialog(dialog);
		
		// get user input
		return dialog.getUserInput();
	}
	
	/**
	 * Returns the selected option from an input dialog 
	 * @param <T> the type of the options and the returned value
	 * @param dialogTitle the title the generated dialog should have
	 * @param options the set of options that should be selectable in the generated selection field
	 * @throws InputDialogClosedException if the input dialog is closed
	 */
	public static <T> List<T> fetchSelectedOption(String dialogTitle, List<T> options) throws InputDialogClosedException {
		return fetchSelectedOption(dialogTitle, options, DEFAULT_VISIBLE_ROW_COUNT);
	}
	
	/**
	 * Initiates and shows the specified BasicFrame 
	 * @param frame a BasicFrame
	 */
	public static void showFrame(BasicFrame frame) {
		frame.initComponents();
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setVisible(true);
	}
	
}
