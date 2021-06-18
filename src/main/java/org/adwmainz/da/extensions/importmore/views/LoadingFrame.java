/**
 * LoadingFrame.java - is an extension of an org.adwmainz.da.extensions.importmore.views.BasicFrame that represents a frame displayed while loading
 *  external resources as used within the ImportMoreXtension developed at the Digital Academy of the Academy of Sciences and Literature | Mainz.
 * @author Patrick D. Brookshire
 * @version 0.1.0
 */
package org.adwmainz.da.extensions.importmore.views;

import java.awt.Dimension;
import java.util.ResourceBundle;

public class LoadingFrame extends BasicFrame {

	// constant
	private static final long serialVersionUID = 1219618731752785793L;

	
	// constructor
	/**
	 * Creates a new LoadingFrame
	 */
	public LoadingFrame() {
		super();
	}

	// implemented method
	/**
	 * Initializes all components and positions them
	 */
	@Override
	public void initComponents() {
		// load localized data
		ResourceBundle rb = ResourceBundle.getBundle("org.adwmainz.da.extensions.importmore.resources.DialogTextBundle");
		
		String message = rb.getString("PLEASE_WAIT");
		setTitle(message);
		
		getContentPane().setPreferredSize(new Dimension(2 * message.length() * message.length(), message.length()));
		pack();
	}

}
