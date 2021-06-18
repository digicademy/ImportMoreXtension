/**
 * BasicFrame.java - is an abstract extension of a javax.swing.JFrame representing a frame as used within the ImportMoreXtension developed at the 
 *  Digital Academy of the Academy of Sciences and Literature | Mainz.
 * @author Patrick D. Brookshire
 * @version 1.0.0
 */
package org.adwmainz.da.extensions.importmore.views;

import javax.swing.JFrame;

public abstract class BasicFrame extends JFrame {

	// constant
	private static final long serialVersionUID = -2114736842315027254L;

	// constructor
	/**
	 * Creates a new BasicFrame
	 */
	public BasicFrame() {
		super();
	}

	// basic initialization method
	/**
	 * Initializes the components and positions them
	 */
	public abstract void initComponents();
	
}
