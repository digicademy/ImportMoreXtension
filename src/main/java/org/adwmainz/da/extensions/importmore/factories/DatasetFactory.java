/**
 * DatasetFactory.java - is a factory class that creates datasets as used within the ImportMoreXtension developed at the Digital Academy of the
 *  Academy of Sciences and Literature | Mainz.
 * @author Patrick D. Brookshire
 * @version 1.0.0
 */
package org.adwmainz.da.extensions.importmore.factories;

import java.util.Map;

import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

public class DatasetFactory {
	
	/**
	 * Converts the given Map to a PieDataset
	 * @param labeledData some Integer values that are labeled with a String key
	 */
	public static PieDataset createPieDataset(Map<String, Integer> labeledData) {
    	DefaultPieDataset dataset = new DefaultPieDataset();
    	for (Map.Entry<String, Integer> entry: labeledData.entrySet()) {
    		String key = entry.getKey();
    		int value = entry.getValue();
    		dataset.setValue(key + ": " + value, value);
    	}
    	return dataset;
    }
    
	/**
	 * Converts the given Map to a PieDataset the keys of which contain percentages (e.g. "foo: 42 (12,34%)")
	 * @param labeledData some Integer values that are labeled with a String key
	 * @param precision the number of decimal places the calculated percentages should be trimmed to
	 * @throws IllegalArgumentException if <code>precision</code> is not a positive integer
	 */
    public static PieDataset createPieDatasetWithPercentages(Map<String, Integer> labeledData, int precision)
			throws IllegalArgumentException {
		// validate fractDigits
		if (precision < 0)
			throw new IllegalArgumentException(precision + "is not a positive integer!");
		
    	// calc base
    	int base = 0;
    	for (int value: labeledData.values())
    		base += value;
    	
    	// generate dataset
    	DefaultPieDataset dataset = new DefaultPieDataset();
    	for (Map.Entry<String, Integer> entry: labeledData.entrySet()) {
    		String key = entry.getKey();
    		int absoluteValue = entry.getValue();
    		double relativeValue = (double)absoluteValue / base;
    		dataset.setValue(String.format("%s: %d (%."+precision+"f%%)", key, absoluteValue, 100*relativeValue), absoluteValue);
    	}
    	return dataset;
    }
    
}
