/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.utility;

import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.date;
import static com.sun.org.apache.xalan.internal.lib.ExsltDatetime.time;
import greenhouse.dao.ChartDataDAO;
import greenhouse.model.ChartData;
import java.awt.Color;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

/**
 *
 * @author Hasitha Lakmal
 */
public class GHChart {

    private TimeSeries s1;
    private final DateAxis domainAxis;
    private final AbstractXYItemRenderer Renderer;
    private final CombinedDomainXYPlot combinedplot;
    private final XYPlot mainPlot;
    final ChartPanel chartPanel;
    private JFreeChart chart;

    public GHChart(String chartName, String dataColumn) throws ClassNotFoundException, IOException, SQLException {
        this(chartName, dataColumn, "X Axis", "Y Axis");
    }

    public GHChart(String chartName, String dataColumn, String xAxisName, String yAxisName) throws ClassNotFoundException, IOException, SQLException {
        domainAxis = new DateAxis(xAxisName);
        NumberAxis rangeAxis = new NumberAxis(yAxisName);
        Renderer = new CandlestickRenderer();

        mainPlot = new XYPlot();
        mainPlot.setDomainAxis(domainAxis);
        mainPlot.setRangeAxis(rangeAxis);
        mainPlot.setRenderer(Renderer);
        // Create a line series
        TimeSeriesCollection timecollection = createDataset(dataColumn);//value just to make difference in the new dataset
        mainPlot.setDataset(1, timecollection);
        mainPlot.setRenderer(1, new XYLineAndShapeRenderer(true, false));
        mainPlot.getRenderer(1).setSeriesPaint(0, Color.blue);

        combinedplot = new CombinedDomainXYPlot(domainAxis);
        combinedplot.setDomainGridlinePaint(Color.white);
        combinedplot.setDomainGridlinesVisible(true);
        combinedplot.add(mainPlot, 3);

        // Do some setting up, see the API Doc
        Renderer.setSeriesPaint(0, Color.BLACK);
        rangeAxis.setAutoRangeIncludesZero(false);

        // Now create the chart and chart panel
        chart = new JFreeChart(chartName, null, combinedplot, false);
        chartPanel = new ChartPanel(chart);
    }

    public ChartPanel generateChartPanel() {
        return chartPanel;
    }

    public TimeSeries getTimeSeries() {
        return s1;
    }

    private XYPlot createPlot(XYDataset dataset) {

        NumberAxis RangeaxisValue = new NumberAxis();

        XYItemRenderer renderer = new XYLineAndShapeRenderer(true, false);
        renderer.setSeriesPaint(0, Color.green);
        renderer.setSeriesPaint(1, Color.blue);

        XYPlot newPlot = new XYPlot(dataset, domainAxis, RangeaxisValue, renderer);
        newPlot.setBackgroundPaint(Color.white);
        newPlot.setDomainGridlinePaint(Color.gray);
        newPlot.setRangeGridlinePaint(Color.gray);
        newPlot.setAxisOffset(new RectangleInsets(5.0, 5.0, 5.0, 5.0));
        return newPlot;
    }

    private TimeSeriesCollection createDataset(String dataColumn) throws ClassNotFoundException, IOException, SQLException {
        final TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.setDomainIsPointsInTime(true);
        s1 = new TimeSeries("", Minute.class);
        ArrayList<ChartData> data = ChartDataDAO.getChartData(dataColumn);
        for (ChartData tdm : data) {
            Calendar dateCal = Calendar.getInstance();
            dateCal.setTime(tdm.getDate());
            Calendar timeCal = Calendar.getInstance();
            timeCal.setTime(tdm.getTime());
            dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
            dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
            dateCal.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));
            s1.addOrUpdate(new Minute(dateCal.getTime()), Double.parseDouble(tdm.getData()));
        }
        dataset.addSeries(s1);

        return dataset;
    }

    public JFreeChart getJFreeChart() {
        return this.chart;
    }

}
