/**
 * ImportMoreArgumentProvider.java - is a helper class storing common argument names and descriptors as used within the ImportMoreXtension developed
 *  at the Digital Academy of the Academy of Sciences and Literature | Mainz.
 * @author Patrick D. Brookshire
 * @version 1.0.0
 */
package org.adwmainz.da.extensions.importmore.utils;
import java.util.ResourceBundle;

import org.adwmainz.da.extensions.askmore.models.EditableArgumentDescriptor;
import org.adwmainz.da.extensions.askmore.utils.AskMoreAnnotationParser;
import org.adwmainz.da.extensions.askmore.utils.AskMoreArgumentProvider;

import ro.sync.ecss.extensions.api.ArgumentDescriptor;
import ro.sync.ecss.extensions.api.AuthorConstants;

public class ImportMoreArgumentProvider {

	// constant list of all argument names used within this ImportMoreXtension
	public static final String ARGUMENT_ATTR_NAME = "name";
	public static final String ARGUMENT_ATTR_NAMESPACE = "namespace";
	public static final String ARGUMENT_ATTR_VALUE = "value";
	public static final String ARGUMENT_ATTR_VALUE_EXPRESSION = "valueExpression";
	public static final String ARGUMENT_CONFIRM_ACTION_MESSAGE = "confirmActionMessage";
	public static final String ARGUMENT_DISPLAY_PERCENTAGES = "displayPercentages";
	public static final String ARGUMENT_EDIT_ATTR = "editAttribute";
	public static final String ARGUMENT_EQUAL_ELEMENT_EXPRESSION = "equalElementExpression";
	public static final String ARGUMENT_LOCAL_ELEMENT_LOCATION = "localElementLocation";
	public static final String ARGUMENT_LOCAL_IDENTIFIER_LOCATION = "localIdentifierLocation";
	public static final String ARGUMENT_NAMESPACE_PREFIXES = "namespacePrefixes";
	public static final String ARGUMENT_NAMESPACE_URIS = "namespaceUris";
	public static final String ARGUMENT_PRECISION = "presicion";
	public static final String ARGUMENT_REMOVE_IF_EMPTY = "removeIfEmpty";
	public static final String ARGUMENT_RESOURCE_LOCATION = "resourceLocation";
	public static final String ARGUMENT_REQUESTED_ELEMENT_LOCATION = "requestedElementLocation";
	public static final String ARGUMENT_REQUESTED_IDENTIFIER_LOCATION = "requestedIdentifierLocation";
	public static final String ARGUMENT_SELECTABLE_NAME_EXPRESSION = "selectableNameExpression";
	public static final String ARGUMENT_SKIP_EMPTY_VALUES = "skipEmptyValues";
	public static final String ARGUMENT_SYSTEM_ID = "systemId";

	
	
	// basic ArgumentDescriptor getter methods
	public static ArgumentDescriptor getAttributeValueExpressionArgumentDescriptor() {
		return new ArgumentDescriptor(
				ARGUMENT_ATTR_VALUE_EXPRESSION, 
				ArgumentDescriptor.TYPE_XPATH_EXPRESSION, 
				"The XPath expression relative to the result(s) of the param '" +ARGUMENT_REQUESTED_ELEMENT_LOCATION + "' that should be used to create the "
						+ "new value for the attribute.\n"
						+ "Please note that this expression must not return empty strings! You may use the expression (possiblyEmptyExpression, nonEmptyDefaultExpression)[1] to avoid this.",
				".");
	}
	
	public static ArgumentDescriptor getDisplayPercentagesArgumentDescriptor() {
		return new ArgumentDescriptor(
				ARGUMENT_DISPLAY_PERCENTAGES, 
				ArgumentDescriptor.TYPE_CONSTANT_LIST, 
				"Specifies whether percentages should be displayed on the chart or not.",
				new String[] {
						AuthorConstants.ARG_VALUE_TRUE, 
						AuthorConstants.ARG_VALUE_FALSE},
				AuthorConstants.ARG_VALUE_FALSE);
	}
	
	public static ArgumentDescriptor getEqualElementExpressionArgumentDescriptor() {
		return new ArgumentDescriptor(
				ARGUMENT_EQUAL_ELEMENT_EXPRESSION, 
				ArgumentDescriptor.TYPE_XPATH_EXPRESSION, 
				"The XPath expression relative to the result(s) of the param '" + ARGUMENT_REQUESTED_ELEMENT_LOCATION + "' that define an external element as"
						+ " equal to a local one.\n"
						+ "Please note that all expressions will be wrapped with the 'normalize-space' function!",
				".");
	}
	
	public static ArgumentDescriptor getFragmentWithImportExpressionsArgumentDescriptor() {
		return new ArgumentDescriptor(
				AskMoreArgumentProvider.ARGUMENT_FRAGMENT, 
				ArgumentDescriptor.TYPE_STRING, 
				"The fragment to be inserted." + ImportMoreAnnotationParser.getDescription());
	}
	
	public static ArgumentDescriptor getLocalElementLocationArgumentDescriptor() {
		return new ArgumentDescriptor(
				ARGUMENT_LOCAL_ELEMENT_LOCATION, 
				ArgumentDescriptor.TYPE_XPATH_EXPRESSION, 
				"An XPath expression indicating the location of elements that are already imported.");
	}
	
	public static ArgumentDescriptor getLocalIdentifierArgumentDescriptor() {
		return new ArgumentDescriptor(
				ARGUMENT_LOCAL_IDENTIFIER_LOCATION, 
				ArgumentDescriptor.TYPE_XPATH_EXPRESSION, 
				"The location relative to the result(s) of the param '"+ImportMoreArgumentProvider.ARGUMENT_LOCAL_ELEMENT_LOCATION+"' that define where to locate "
						+ "the keys within the resource.\n"
						+ "Please note that this expression will be wrapped with the 'normalize-space' function and should match the results of the param"
						+ ARGUMENT_REQUESTED_IDENTIFIER_LOCATION+"!");
	}
	
	public static ArgumentDescriptor getNamespacePrefixesArgumentDescriptor() {
		return new ArgumentDescriptor(
				ARGUMENT_NAMESPACE_PREFIXES, 
				ArgumentDescriptor.TYPE_STRING, 
				"The namespace prefixes used in XPath expressions to query the resource, separated by new lines."
				+ "\n(This list must contain as many values as the one of "+ARGUMENT_NAMESPACE_URIS+")");
	}
	
	public static ArgumentDescriptor getNamespaceUrisArgumentDescriptor() {
		return new ArgumentDescriptor(
				ARGUMENT_NAMESPACE_URIS, 
				ArgumentDescriptor.TYPE_STRING, 
				"The namespace uris used in XPath expressions to query the resource, separated by new lines."
				+ "\n(This list must contain as many values as the one of "+ARGUMENT_NAMESPACE_PREFIXES+")");
	}
	
	public static ArgumentDescriptor getPrecisionArgumentDescriptor() {
		return new ArgumentDescriptor(
				ARGUMENT_PRECISION,
				ArgumentDescriptor.TYPE_STRING, 
				"The number of fraction digits that should be displayed if the param "+ ImportMoreArgumentProvider.ARGUMENT_DISPLAY_PERCENTAGES +""
						+ " is set to " + AuthorConstants.ARG_VALUE_TRUE + ".");
	}

	public static ArgumentDescriptor getRequestedElementLocationArgumentDescriptor() {
		return new ArgumentDescriptor(
				ARGUMENT_REQUESTED_ELEMENT_LOCATION, 
				ArgumentDescriptor.TYPE_XPATH_EXPRESSION, 
				"An XPath expression indicating the position of the original element(s) in the " + ARGUMENT_RESOURCE_LOCATION + ".");
	}
	
	public static ArgumentDescriptor getRequestedIdentifierArgumentDescriptor() {
		return new ArgumentDescriptor(
				ARGUMENT_REQUESTED_IDENTIFIER_LOCATION, 
				ArgumentDescriptor.TYPE_XPATH_EXPRESSION, 
				"The location relative to the result(s) of the param '"+ImportMoreArgumentProvider.ARGUMENT_REQUESTED_ELEMENT_LOCATION+"' that define where to "
						+ "locate the keys within the resource.\n"
						+ "Please note that this expression will be wrapped with the 'normalize-space' function and should match the results of the param"
						+ ARGUMENT_LOCAL_IDENTIFIER_LOCATION+"!");
	}
	
	public static ArgumentDescriptor getResourceLocationArgumentDescriptor() {
		return new ArgumentDescriptor(
				ARGUMENT_RESOURCE_LOCATION, 
				ArgumentDescriptor.TYPE_STRING, 
				"The URL of the resource containing the original elements or its local file path."+AskMoreAnnotationParser.getDescription());
	}
	
	public static ArgumentDescriptor getResultsViewMessageArgumentDescriptor(String defaultValue) {
		ArgumentDescriptor basicArgument = AskMoreArgumentProvider.getResultsViewMessageArgumentDescriptor(defaultValue);
		EditableArgumentDescriptor derivedArgument = EditableArgumentDescriptor.copyOf(basicArgument);
		derivedArgument.setDescription(basicArgument.getDescription()+"\n"+ImportMoreAnnotationParser.getDescription());
		return derivedArgument;
	}
	
	public static ArgumentDescriptor getSelectableNameExpressionArgumentDescriptor() {
		return new ArgumentDescriptor(
				ARGUMENT_SELECTABLE_NAME_EXPRESSION, 
				ArgumentDescriptor.TYPE_XPATH_EXPRESSION, 
				"The XPath expression relative to the result(s) of the param '" +ARGUMENT_REQUESTED_ELEMENT_LOCATION + "' that should be used to create the "
						+ "selectable options\n"
						+ "Please note that this expression must not return empty nodes! You may use the expression (possiblyEmptyExpression, nonEmptyDefaultExpression)[1] to avoid this.",
						".");
	}
	
	public static ArgumentDescriptor getSystemIdArgumentDescriptor() {
		return new ArgumentDescriptor(
				ARGUMENT_SYSTEM_ID, 
				ArgumentDescriptor.TYPE_STRING, 
				"The systemId displayed in the results view tab for each element.\n"+ImportMoreAnnotationParser.getDescription());
	}
	
	public static ArgumentDescriptor getSkipEmptyValuesArgumentDescriptor() {
		return new ArgumentDescriptor(
				ARGUMENT_SKIP_EMPTY_VALUES, 
				ArgumentDescriptor.TYPE_CONSTANT_LIST, 
				"Decides whether empty elements should be omitted or imported.",
				new String[] {
						AuthorConstants.ARG_VALUE_TRUE, 
						AuthorConstants.ARG_VALUE_FALSE},
				AuthorConstants.ARG_VALUE_TRUE);
	}
	
	// other methods
	/**
	 * Tries to find an ArgumentDescriptor with the given name in an Array
	 * @param arguments an ArgumentDescriptor Array
	 * @param argumentName the name of an ArgumentDescriptor
	 * @throws IllegalArgumentException if the given Array does not contain an ArgumentDescriptor with the given name
	 */
	public static ArgumentDescriptor getArgument(ArgumentDescriptor[] arguments, String argumentName) {
		for (ArgumentDescriptor argument: arguments)
			if (argument.getName().equals(argumentName))
				return argument;
		throw new IllegalArgumentException("Could not find an argument with the name " + argumentName);
	}
	
	/**
	 * Returns a localized message noting that an import was aborted
	 */
	public static String getAbortedImportMessage() {
		ResourceBundle rb = ResourceBundle.getBundle("org.adwmainz.da.extensions.importmore.resources.DialogTextBundle");
		return rb.getString("ABORTED_IMPORT");
	}

}
