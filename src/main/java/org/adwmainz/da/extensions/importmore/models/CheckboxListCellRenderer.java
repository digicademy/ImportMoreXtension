/**
 * CheckboxListCellRenderer.java - is a custom renderer that displays jList items as JCheckBoxes as used within the AskMoreXtension developed at
 *  the Digital Academy of the Academy of Sciences and Literature | Mainz.
 * @author Patrick D. Brookshire
 * @version 1.0.0
 */
package org.adwmainz.da.extensions.importmore.models;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

public class CheckboxListCellRenderer<E> extends JCheckBox implements ListCellRenderer<E> {

	// generated serial version id
	private static final long serialVersionUID = -278844714842243873L;

	// overridden renderer method
	@Override
	public Component getListCellRendererComponent(JList<? extends E> list, E value, int index, boolean isSelected, boolean cellHasFocus) {
		
		// keep original list configuration
		setComponentOrientation(list.getComponentOrientation());
        setFont(list.getFont());
        setBackground(list.getBackground());
        setForeground(list.getForeground());
        setSelected(isSelected);
        setEnabled(list.isEnabled());
        setText(value == null ? "" : value.toString());  

        return this;
	}

}
