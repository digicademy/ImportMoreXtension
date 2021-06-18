/**
 * URLFactory.java - is a factory class that creates URLs as used within the ImportMoreXtension developed at the Digital Academy of the Academy of
 *  Sciences and Literature | Mainz.
 * @author Patrick D. Brookshire
 * @version 1.0.0
 */
package org.adwmainz.da.extensions.importmore.factories;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class URLFactory {
	
	/**
	 * Returns the URL corresponding to the specified resourceName
	 * @param resourcePath the path to a resource
	 * @throws MalformedURLException if <code>resourceName</code> cannot be parsed
	 */
	public static URL create(String resourcePath) throws MalformedURLException {
		File file = new File(resourcePath);
		if (file.isFile())
			return file.toURI().toURL();
		return new URL(resourcePath);
	}
	
}
