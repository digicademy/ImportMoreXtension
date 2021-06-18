/**
 * MultiSelectionDialog.java - is a generic extension of an org.adwmainz.da.extensions.askmore.views.BasicInputDialog that represents an input
 *  dialog with one selection field that allows the selection of multiple items as used within the ImportMoreXtension developed at the Digital
 *  Academy of the Academy of Sciences and Literature | Mainz.
 * @author Patrick D. Brookshire
 * @version 1.0.0
 */
package org.adwmainz.da.extensions.importmore.views;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.GroupLayout.Alignment;

import org.adwmainz.da.extensions.askmore.exceptions.InputDialogClosedException;
import org.adwmainz.da.extensions.askmore.views.BasicInputDialog;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

public class MultiSelectionDialog<T> extends BasicInputDialog<List<T>> {

	// constant
	private static final long serialVersionUID = -8224506369509575024L;

	// accessible component
	protected JList<T> list;
	
	protected JButton selectAllButton;
	protected JButton deselectAllButton;

	// component identifier
	protected String labelText;
	protected String selectAllButtonText;
	protected String deselectAllButtonText;
	
	// constructors
	/**
	 * Creates a new MultiSelectionDialog with the specified params and a default alignment (i.e. the 
	 * selection form control is aligned to the left with a vertical alignment set to <code>Alignment.BASELINE</code> 
	 * while the main buttons (OK/CANCEL) are aligned to the right).
	 * @param owner the Frame from which the dialog is displayed
	 * @param isModal specifies whether the dialog blocks user input to other top-level windows when shown
	 * @param options a list of options that should be selectable
	 */
	public MultiSelectionDialog(Window owner, boolean isModal, List<T> options) {
		super(owner, isModal, Alignment.CENTER, Alignment.BASELINE, Alignment.TRAILING);
		list = new JList<>(new Vector<>(options));
	}
	
	/**
	 * Creates a new MultiSelectionDialog with the specified params
	 * @param owner the Frame from which the dialog is displayed
	 * @param isModal specifies whether the dialog blocks user input to other top-level windows when shown
	 * @param horizontalAlignment specifies the horizontal alignment of the selection form control. Use 
	 * <code>Alignment.LEADING</code> to left indent, <code>Alignment.TRAILING</code> to right indent or 
	 * <code>Alignment.CENTER</code> to full indent
	 * @param verticalAlignment specifies the vertical alignment of the selection form control. Use 
	 * <code>Alignment.LEADING</code> to align it to the top edge, <code>Alignment.TRAILING</code> to align 
	 * it to the bottom edge or <code>Alignment.BASELINE</code> to align it to the baseline.
	 * @param options a list of options that should be selectable
	 */
	public MultiSelectionDialog(Window owner, boolean isModal, Alignment horizontalAlignment,
			Alignment verticalAlignment, List<T> options) {
		super(owner, isModal, horizontalAlignment, verticalAlignment);
		list = new JList<>(new Vector<>(options));
	}
	
	/**
	 * Creates a new MultiSelectionDialog with the specified params
	 * @param owner the Frame from which the dialog is displayed
	 * @param isModal specifies whether the dialog blocks user input to other top-level windows when shown
	 * @param horizontalFormGroupAlignment specifies the horizontal alignment of the selection form control. Use 
	 * <code>Alignment.LEADING</code> to left indent, <code>Alignment.TRAILING</code> to right indent or 
	 * <code>Alignment.CENTER</code> to full indent
	 * @param verticalAlignment specifies the vertical alignment of the selection form control. Use 
	 * <code>Alignment.LEADING</code> to align it to the top edge, <code>Alignment.TRAILING</code> to align 
	 * it to the bottom edge or <code>Alignment.BASELINE</code> to align it to the baseline.
	 * @param horizontalOkCancelBtnAlignment specifies the horizontal alignment of normal the main buttons
	 *  (OK/CANCEL). Use <code>Alignment.LEADING</code> to left indent, <code>Alignment.TRAILING</code> to 
	 *  right indent or <code>Alignment.CENTER</code> to full indent
	 * @param options a list of options that should be selectable
	 */
	public MultiSelectionDialog(Window owner, boolean isModal, Alignment horizontalFormGroupAlignment,
			Alignment verticalAlignment, Alignment horizontalOkCancelBtnAlignment, List<T> options) {
		super(owner, isModal, horizontalFormGroupAlignment, verticalAlignment, horizontalOkCancelBtnAlignment);
		list = new JList<>(new Vector<>(options));
	}

	// basic getter and setters
	@Override
	public List<T> getUserInput() throws InputDialogClosedException {
		if (userInput == null)
			throw new InputDialogClosedException();
		return userInput;
	}
	
	/**
	 * Replaces the default ListCellRenderer with a new one
	 * @param cellRenderer a ListCellRenderer
	 */
	public void setCellRenderer (ListCellRenderer<T> cellRenderer) {
		list.setCellRenderer(cellRenderer);
	}
	
	// basic initialization methods
	/**
	 * Initializes all components and positions them
	 */
	@Override
	public void initComponents() {
		// load localized data
		ResourceBundle rb = ResourceBundle.getBundle("org.adwmainz.da.extensions.importmore.resources.DialogTextBundle");
		selectAllButtonText = rb.getString("SELECT_ALL");
		deselectAllButtonText = rb.getString("DESELECT_ALL");
		
		
		// init default components and layout
		super.initComponents();
		
		// add option list
		addFormElement(new JScrollPane(list));
		
		// init selection buttons
		selectAllButton = new JButton(selectAllButtonText);
		deselectAllButton = new JButton(deselectAllButtonText);
		
		// add to layout and link sizes
		addFormElement(selectAllButton);
		addFormElement(deselectAllButton);
		
		// add actions
		selectAllButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectAllButtonActionPerfomed();
			}
		});
		deselectAllButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				deselectAllButtonActionPerfomed();
			}
		});
		
		pack();
	}
	
	/**
	 * Fetches all user input and and adds it to the field <code>userInput</code>
	 */
	@Override
	protected void okButtonActionPerformed(ActionEvent e) {
		userInput = list.getSelectedValuesList();
	}
	
	/**
	 * Selects all values
	 */
	protected void selectAllButtonActionPerfomed() {
		list.setSelectionInterval(0, list.getModel().getSize()-1);
	}
	
	/**
	 * Clears the current selection
	 */
	protected void deselectAllButtonActionPerfomed() {
		list.clearSelection();
	}

	/**
	 * Defines how many selectable elements should be visible without scrolling
	 * @param rowCount the number of selectable elements that should be visible without scrolling
	 */
	public void setVisibleRowCount(int rowCount) {
		list.setVisibleRowCount(rowCount);
	}
	
	@Override
	public boolean hasLabels() {
		return false;
	}

}
