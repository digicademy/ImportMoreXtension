/**
 * ImportMoreXPathException.java - is an exception class signaling that an XPath parsing error occurred within the ImportMoreXtension developed at
 *  the Digital Academy of the Academy of Sciences and Literature | Mainz.
 * @author Patrick D. Brookshire
 * @version 1.0.0
 */
package org.adwmainz.da.extensions.importmore.exceptions;

public class ImportMoreXPathException extends Exception {

	// default serial version ID
	private static final long serialVersionUID = 1L;

	/**
	 * Creates a new ImportMoreXPathException with <code>null</code> as its detail message.
	 */
	public ImportMoreXPathException() {
		super();
	}

	/**
	 * Creates a new ImportMoreXPathException with the specified message.
	 * @param message the detail message
	 */
	public ImportMoreXPathException(String message) {
		super(message);
	}

	/**
	 * Creates a new ImportMoreXPathException with the specified cause.
	 * @param cause the Throwable that caused this exception
	 */
	public ImportMoreXPathException(Throwable cause) {
		super(cause);
	}

	/**
	 * Creates a new ImportMoreXPathException with the specified message and cause.
	 * @param message the detail message
	 * @param cause the Throwable that caused this exception
	 */
	public ImportMoreXPathException(String message, Throwable cause) {
		super(message, cause);
	}


	/**
	 * Creates a new ImportMoreXPathException with the specified message and cause as well as suppression and 
	 * writable stack trace controls.
	 * @param message the detail message
	 * @param cause the Throwable that caused this exception
	 * @param enableSuppression controls whether or not suppression should be enabled or disabled
	 * @param writableStackTrace controls whether or not the stack trace should be writable
	 */
	public ImportMoreXPathException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
