/**
 * PieChartFrame.java - is an extension of an org.adwmainz.da.extensions.importmore.views.BasicFrame that represents a frame with a pie chart as
 *  used within the ImportMoreXtension developed at the Digital Academy of the Academy of Sciences and Literature | Mainz.
 * @author Patrick D. Brookshire
 * @version 1.0.0
 */
package org.adwmainz.da.extensions.importmore.views;

import java.awt.Dimension;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.Plot;
import org.jfree.data.general.PieDataset;

public class PieChartFrame extends BasicFrame {

	// constant
	private static final long serialVersionUID = -1709194777361025119L;
	
	// fields
	protected String chartTitle;
	protected PieDataset dataset;
	
	// accessible components
	protected ChartPanel chartPanel;

	// constructor
	/**
	 * Crates a new PieChartFrame with the specified params
	 * @param chartTitle the title that should be displayed next to the chart
	 * @param dataset the PieDataset that should be used to create the chart
	 */
	public PieChartFrame(String chartTitle, PieDataset dataset) {
		super();
		this.chartTitle = chartTitle;
		this.dataset = dataset;
	}

	// basic getter and setter
	/**
	 * Returns the title displayed next to the chart
	 */
	public String getChartTitle() {
		return chartTitle;
	}

	/**
	 * Replaces the title displayed next to the chart with a new one
	 * @param chartTitle the new title that should be displayed next to the chart
	 */
	public void setChartTitle(String chartTitle) {
		this.chartTitle = chartTitle;
		updateChartPanel();
	}
	
	/**
	 * Returns the PieDataset underlying the chart
	 */
	public PieDataset getDataset() {
		return dataset;
	}

	/**
	 * Replaces the PieDataset underlying the chart with a new one
	 * @param dataset the new PieDataset that should be used to create the chart
	 */
	public void setDataset(PieDataset dataset) {
		this.dataset = dataset;
		updateChartPanel();
	}

	/**
	 * Initializes all components and positions them
	 */
	public void initComponents() {
		chartPanel = createChartPanel(createChart());
		add(chartPanel);
		pack();
	}
	
	// helper methods
	protected JFreeChart createChart() {
		// create chart
		JFreeChart chart = ChartFactory.createPieChart(chartTitle, dataset, false, true, true);
		
		// style chart
		chart.setBackgroundPaint(getBackground()); // use default background
		
		// style plot
		Plot plot = chart.getPlot();
		plot.setBackgroundPaint(getBackground()); // use default background
		plot.setOutlinePaint(null); // remove border
		
		return chart;
	}
	
	protected ChartPanel createChartPanel(JFreeChart chart) {
		// create panel
		ChartPanel chartPanel = new ChartPanel(chart);
		
		// reduce size
		Dimension d = chartPanel.getPreferredSize();
        chartPanel.setPreferredSize(new Dimension(d.width/2,d.height/2));
        
        return chartPanel;
	}
	
	protected void updateChartPanel() {
		chartPanel.setChart(createChart());
		revalidate();
		repaint();
	}
	
}
