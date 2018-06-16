/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.test;

import greenhouse.dao.ChartDataDAO;
import greenhouse.model.ChartData;
import static greenhouse.test.TimeSeriesFrame.randBetween;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
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
import org.jfree.data.time.Day;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.RectangleInsets;

/**
 *
 * @author Hasitha Lakmal
 */
public class DynamicChart extends javax.swing.JFrame {

    TimeSeries s1;
    DateAxis domainAxis;
    private AbstractXYItemRenderer Renderer;
    CombinedDomainXYPlot combinedplot;
    XYPlot mainPlot;
    int seriesnumber = 2;//already there are 2 series added to the chart
    int x = 5;

    /**
     * Creates new form DynamicChart
     */
    public DynamicChart() {
        initComponents();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        domainAxis = new DateAxis("Date");
        NumberAxis rangeAxis = new NumberAxis("Value");
        Renderer = new CandlestickRenderer();

        mainPlot = new XYPlot();
        mainPlot.setDomainAxis(domainAxis);
        mainPlot.setRangeAxis(rangeAxis);
        mainPlot.setRenderer(Renderer);
        // Create a line series
        TimeSeriesCollection timecollection = createMADataset(2);//value just to make difference in the new dataset
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
        JFreeChart chart = new JFreeChart("Temperature", null, combinedplot, false);
        ChartPanel chartPanel = new ChartPanel(chart);
        Dimension size = tempPanel.getSize();
        chartPanel.setPreferredSize(size);
        tempPanel.add(chartPanel, BorderLayout.CENTER);
        tempPanel.validate();
    }

    public XYPlot createPlot(XYDataset dataset) {

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

    private TimeSeriesCollection createMADataset(int s) {
        final TimeSeriesCollection dataset = new TimeSeriesCollection();
        dataset.setDomainIsPointsInTime(true);

        TreeSet<Date> set = new TreeSet<>();

        GregorianCalendar gc = new GregorianCalendar();
        for (int i = 0; i < 10; i++) {
            int year = randBetween(2010, 2010);
            gc.set(gc.YEAR, year);
            int dayOfYear = randBetween(1, gc.getActualMaximum(gc.DAY_OF_YEAR));
            gc.set(gc.DAY_OF_YEAR, dayOfYear);
            String date = gc.get(gc.YEAR) + "/" + (gc.get(gc.MONTH) + 1) + "/" + gc.get(gc.DAY_OF_MONTH);
            Date d = gc.getTime();
            set.add(d);
        }
        System.out.println("");
        s1 = new TimeSeries("", Minute.class);
        Random rand = new Random();
        for (Date d : set) {
            System.out.println(d);
            int randomNum = rand.nextInt((100 - 0) + 1);
            s1.addOrUpdate(new Minute(d), randomNum);
        }

        dataset.addSeries(s1);
        return dataset;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        tempPanel = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        lightPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        tempPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        tempPanel.setLayout(new java.awt.BorderLayout());

        jButton1.setText("update");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        lightPanel.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        lightPanel.setLayout(new java.awt.BorderLayout());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(305, 305, 305)
                        .addComponent(jButton1))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lightPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 631, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(tempPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 631, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(19, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(tempPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lightPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 83, Short.MAX_VALUE)
                .addComponent(jButton1)
                .addGap(19, 19, 19))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        s1.clear();
        try {
            ArrayList<ChartData> data = ChartDataDAO.getChartData("temperature");
            for (ChartData tdm : data) {
                s1.addOrUpdate(new Day(tdm.getDate()), Double.parseDouble(tdm.getData()));
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DynamicChart.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DynamicChart.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(DynamicChart.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jButton1ActionPerformed

    public double getrandomNumber() {
        Random numGen = new Random();
        return numGen.nextDouble() * 100;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DynamicChart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DynamicChart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DynamicChart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DynamicChart.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new DynamicChart().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel lightPanel;
    private javax.swing.JPanel tempPanel;
    // End of variables declaration//GEN-END:variables
}
