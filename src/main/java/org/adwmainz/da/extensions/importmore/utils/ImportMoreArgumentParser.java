/**
 * ImportMoreArgumentParser.java - is a helper class that validates arguments of an ro.sync.ecss.extensions.api.AuthorOperation as used within the
 *  ImportMoreXtension developed at the Digital Academy of the Academy of Sciences and Literature | Mainz.
 * @author Patrick D. Brookshire
 * @version 1.0.0
 */
package org.adwmainz.da.extensions.importmore.utils;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import ro.sync.ecss.extensions.api.ArgumentsMap;

public class ImportMoreArgumentParser {
	
	/**
	 * Returns a valid List argument value of an AuthorOperation
	 * @param args the ArgumentMap of the operation
	 * @param argumentName the name of the argument
	 * @param delimiter the delimiter used to separate each argument value in the list
	 * @throws IllegalArgumentException if the given argument is not specified or otherwise invalid
	 */
	public static List<String> getValidList(ArgumentsMap args, String argumentName, String delimiter) 
			throws IllegalArgumentException {
		Object argValue = args.getArgumentValue(argumentName);
		if (argValue == null)
			argValue = "";
		
		if (!(argValue instanceof String))
			throw new IllegalArgumentException("'" + argumentName + "' is not specified as a parameter or invalid.");
		
		String argValueAsString = argValue.toString();
		
		List<String> list = new ArrayList<>();
		if (!argValueAsString.isEmpty())
			for (String str: argValueAsString.split(delimiter))
				list.add(str);
		return list;
	}
	
	/**
	 * Returns a valid List argument value of an AuthorOperation that is separated by new lines
	 * @param args the ArgumentMap of the operation
	 * @param argumentName the name of the argument
	 * @throws IllegalArgumentException if the given argument is empty or otherwise invalid
	 */
	public static List<String> getValidList(ArgumentsMap args, String argumentName) 
			throws IllegalArgumentException {
		return getValidList(args, argumentName, "\n");
	}

	/**
	 * Returns a Map of two List argument values of an AuthorOperation that act as key value pairs
	 * @param args the ArgumentMap of the operation
	 * @param keyArgumentName the name of the list argument providing the keys
	 * @param valueArgumentName the name of the list argument providing the values
	 * @param delimiter the delimiter used to separate each argument value in the list
	 * @throws IllegalArgumentException if the given argument is empty or otherwise invalid
	 */
	public static Map<String,String> getValidMap(ArgumentsMap args, String keyArgumentName, String valueArgumentName, String delimiter) 
			throws IllegalArgumentException {
		List<String> keys = getValidList(args, keyArgumentName, delimiter);
		List<String> values = getValidList(args, valueArgumentName, delimiter);
		if (keys.size() != values.size())
			throw new IllegalArgumentException("The number of '"+keyArgumentName+"' params must match the number of '"+valueArgumentName+"' params");
		
		Map<String, String> map = new LinkedHashMap<>();
		for (int i=0; i<keys.size(); ++i)
			map.put(keys.get(i), values.get(i));
		return map;
	}
	
	/**
	 * Returns a Map of two List argument values of an AuthorOperation that act as key value pairs and are both separated by new lines
	 * @param args the ArgumentMap of the operation
	 * @param keyArgumentName the name of the list argument providing the keys
	 * @param valueArgumentName the name of the list argument providing the values
	 * @throws IllegalArgumentException if the given argument is empty or otherwise invalid
	 */
	public static Map<String,String> getValidMap(ArgumentsMap args, String keyArgumentName, String valueArgumentName) throws IllegalArgumentException {
		return getValidMap(args, keyArgumentName, valueArgumentName, "\n");
	}

}
