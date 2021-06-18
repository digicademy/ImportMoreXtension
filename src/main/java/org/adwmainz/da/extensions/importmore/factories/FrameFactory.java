/**
 * FrameFactory.java - is a factory class that creates frames used within the ImportMoreXtension developed at the Digital Academy of the Academy of
 *  Sciences and Literature | Mainz.
 * @author Patrick D. Brookshire
 * @version 1.0.0
 */
package org.adwmainz.da.extensions.importmore.factories;

import org.adwmainz.da.extensions.importmore.views.LoadingFrame;
import org.adwmainz.da.extensions.importmore.views.PieChartFrame;
import org.jfree.data.general.PieDataset;

public class FrameFactory {
	
	/**
	 * Creates a PieChartFrame with the specified params
	 * @param dialogTitle the title the generated dialog should have
	 * @param chartTitle the title the generated pie chart should have
	 * @param dataset the dataset that should be used to create a pie chart
	 */
	public static PieChartFrame createPieChartFrame(String dialogTitle, String chartTitle, PieDataset dataset) {
		PieChartFrame frame = new PieChartFrame(chartTitle, dataset);
		frame.setTitle(dialogTitle);
		return frame;
	}
	
	/**
	 * Creates a basic LoadingFrame
	 */
	public static LoadingFrame createLoadingFrame() {
		LoadingFrame frame = new LoadingFrame();
		frame.setLocationRelativeTo(null); // center view
		return frame;
	}

}
