/**
 * ImportMoreStringUtils.java - is a helper class providing String-related methods as used within the ImportMoreXtension developed at the Digital
 *  Academy of the Academy of Sciences and Literature | Mainz.
 * @author Patrick D. Brookshire
 * @version 1.0.0
 */
package org.adwmainz.da.extensions.importmore.utils;

public class StringUtils {
	
	/**
	 * Reduces the length of a String to be less than or equal to the specified newLength by replacing additional chars with <code>…</code>
	 * @param str some String
	 * @param newLength the maximum length of the returned String
	 * @throws IllegalArgumentException if <code>newLength</code> is less than 1
	 */
	public static String reduceLength(String str, int newLength) throws IllegalArgumentException {
		if (newLength < 1)
			throw new IllegalArgumentException("newLength must be at least 1 to create the String \"…\"");
		
		if (str.length() > newLength)
			return str.substring(0, (newLength-1)) + "…";
		return str;
	}
	
}
