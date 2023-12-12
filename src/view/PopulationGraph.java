/*******************************************************************************
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.util.Map.Entry;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import society.PopulationStatistics;
import util.MutableInt;

public class PopulationGraph extends JPanel {

	private JFreeChart chart;
	
	public PopulationGraph(PopulationStatistics populationStatistics) {

		initUI(populationStatistics);
	}

	private void initUI(PopulationStatistics populationStatistics) {

		XYDataset dataset = createDataset(populationStatistics);
		chart = createChart(dataset);
		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
		chartPanel.setBackground(Color.white);

		add(chartPanel);
	}

	public void updateData(PopulationStatistics populationStatistics) {
		chart.getXYPlot().setDataset(createDataset(populationStatistics));
	}
	
	private XYDataset createDataset(PopulationStatistics populationStatistics) {

		var series1 = new XYSeries("population");
		for(Entry<Integer, MutableInt> entry : populationStatistics.getStats().entrySet()) {
			series1.add(entry.getKey(), Integer.valueOf(entry.getValue().getValue()));
		}

		var dataset = new XYSeriesCollection();
		dataset.addSeries(series1);

		return dataset;
	}

	private JFreeChart createChart(final XYDataset dataset) {

		JFreeChart chart = ChartFactory.createXYLineChart(
				"Number of people for each age category",
				"Age",
				"Number of people",
				dataset,
				PlotOrientation.VERTICAL,
				true,
				true,
				false
				);

		XYPlot plot = chart.getXYPlot();

		var renderer = new XYLineAndShapeRenderer();

		renderer.setSeriesPaint(0, Color.RED);
		renderer.setSeriesStroke(0, new BasicStroke(2.0f));
		renderer.setSeriesPaint(1, Color.BLUE);
		renderer.setSeriesStroke(1, new BasicStroke(2.0f));

		plot.setRenderer(renderer);
		plot.setBackgroundPaint(Color.white);
		plot.setRangeGridlinesVisible(false);
		plot.setDomainGridlinesVisible(false);

		chart.getLegend().setFrame(BlockBorder.NONE);

		chart.setTitle(new TextTitle("Number of people for each age category",
				new Font("Serif", Font.BOLD, 18)
				)
				);

		return chart;
	}
}