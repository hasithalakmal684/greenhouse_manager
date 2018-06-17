/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package greenhouse.view;

import gnu.io.SerialPort;
import greenhouse.GreenHouse;
import greenhouse.dao.ChartDataDAO;
import greenhouse.dao.DataDAO;
import greenhouse.dao.DeviceDAO;
import greenhouse.dao.DeviceTypeDAO;
import greenhouse.dao.GreenhouseDAO;
import greenhouse.listener.SerialPortConnection;
import greenhouse.listener.SerialWriter;
import greenhouse.model.ChartData;
import greenhouse.model.Data;
import greenhouse.model.DeviceType;
import greenhouse.model.Devices;
import greenhouse.model.Greenhouse;
import greenhouse.utility.DataTypes;
import greenhouse.utility.GHChart;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.LegendItem;
import org.jfree.chart.LegendItemCollection;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.AbstractXYItemRenderer;
import org.jfree.chart.renderer.xy.CandlestickRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Minute;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.time.TimeSeriesDataItem;

/**
 *
 * @author Hasitha Lakmal
 */
public class MainForm extends javax.swing.JFrame implements ActionListener {

    TimeSeries tempDataSeries;
    TimeSeries lightDataSeries;
    TimeSeries humidityDataSeries;
    TimeSeries pressureDataSeries;
    TimeSeries phDataSeries;
    TimeSeries soilMoistureDataSeries;
    boolean deviceTypeChanged = false;

    GHChart tempGHChart;
    GHChart pressureGHChart;
    GHChart lightGHChart;
    GHChart phGHChart;
    GHChart humidityGHChart;
    GHChart soilMoistureGHChart;

    private String lastDashboardGH = "";

    /**
     * Creates new form MainForm
     */
    public MainForm() {
        try {
            initComponents();
            setIconImage(GreenHouse.app_icon);

            Timer t = new Timer(15000, this);
            t.start();

            //reset components
            ghComboBox.removeAllItems();
            ghSummaryComboBox.removeAllItems();
            deviceTypeComboBox.removeAllItems();
            ipComboBox.removeAllItems();
            removeIpButton.setEnabled(false);

            //load components
            ArrayList<Greenhouse> allGreenhouses = GreenhouseDAO.getAllGreenhouses();
            for (Greenhouse g : allGreenhouses) {
                ghComboBox.addItem(g.getName());
                ghSummaryComboBox.addItem(g.getName());
                ghControllerComboBox.addItem(g.getName());
            }

            ArrayList<DeviceType> allDEviceTypes = DeviceTypeDAO.getAllDeviceTypes();
            for (DeviceType dt : allDEviceTypes) {
                deviceTypeComboBox.addItem(dt.getType());
            }

            Data d = DataDAO.getSummarizedDataForGH(allGreenhouses.get(0).getName());
            tempTextField.setText(d.getTemperature());
            lightTextField.setText(d.getLight());
            pressureTextField.setText(d.getPressure());
            humidityTextField.setText(d.getHumidity());
            phTextField.setText(d.getPh());
            soilMoistureTextField.setText(d.getSoilMoisture());

//            Double tempD = new Double(d.getTemperature());
//            Double lightD = new Double(d.getLight());
//            Double humidityD = new Double(d.getHumidity());
//            Double phD = new Double(d.getPh());
//            Double pressureD = new Double(d.getPressure());
//            Double soilMD = new Double(d.getSoilMoisture());
//
//            tempTextField.setBackground(getTexFiledBackground(tempD.intValue()));
//            lightTextField.setBackground(getTexFiledBackground(lightD.intValue()));
//            pressureTextField.setBackground(getTexFiledBackground(pressureD.intValue()));
//            humidityTextField.setBackground(getTexFiledBackground(humidityD.intValue()));
//            phTextField.setBackground(getTexFiledBackground(phD.intValue()));
//            soilMoistureTextField.setBackground(getTexFiledBackground(soilMD.intValue()));
            tempTextField.setBackground(getTexFiledBackground(Integer.parseInt(d.getTemperature())));
            lightTextField.setBackground(getTexFiledBackground(Integer.parseInt(d.getLight())));
            pressureTextField.setBackground(getTexFiledBackground(Integer.parseInt(d.getPressure())));
            humidityTextField.setBackground(getTexFiledBackground(Integer.parseInt(d.getHumidity())));
            phTextField.setBackground(getTexFiledBackground(Integer.parseInt(d.getPh())));
            soilMoistureTextField.setBackground(getTexFiledBackground(Integer.parseInt(d.getSoilMoisture())));
            commandsTextArea.setText("Q;ip;000000;P");

            //load charts
            tempGHChart = new GHChart("Temperature", DataTypes.TEMPERATURE.columnName(), "Date/Time", "Value");
            tempDataSeries = tempGHChart.getTimeSeries();
            ChartPanel tempChart = tempGHChart.generateChartPanel();
            tempChart.setSize(tempChartPanel.getSize());
            tempChartPanel.add(tempChart, BorderLayout.CENTER);
            tempChartPanel.validate();

            pressureGHChart = new GHChart("Pressure", DataTypes.PRESSURE.columnName(), "Date/Time", "Value");
            pressureDataSeries = pressureGHChart.getTimeSeries();
            ChartPanel pressureChart = pressureGHChart.generateChartPanel();
            pressureChart.setSize(pressureChartPanel.getSize());
            pressureChartPanel.add(pressureChart, BorderLayout.CENTER);
            pressureChartPanel.validate();

            lightGHChart = new GHChart("Light", DataTypes.LIGHT.columnName(), "Date/Time", "Value");
            lightDataSeries = lightGHChart.getTimeSeries();
            ChartPanel lightChart = lightGHChart.generateChartPanel();
            lightChart.setSize(lightChartPanel.getSize());
            lightChartPanel.add(lightChart, BorderLayout.CENTER);
            lightChartPanel.validate();

            phGHChart = new GHChart("PH", DataTypes.PH.columnName(), "Date/Time", "Value");
            phDataSeries = phGHChart.getTimeSeries();
            ChartPanel phChart = phGHChart.generateChartPanel();
            phChart.setSize(phChartPanel.getSize());
            phChartPanel.add(phChart, BorderLayout.CENTER);
            phChartPanel.validate();

            humidityGHChart = new GHChart("Humidity", DataTypes.HUMIDITY.columnName(), "Date/Time", "Value");
            humidityDataSeries = humidityGHChart.getTimeSeries();
            ChartPanel humidityChart = humidityGHChart.generateChartPanel();
            humidityChart.setSize(humidityChartPanel.getSize());
            humidityChartPanel.add(humidityChart, BorderLayout.CENTER);
            humidityChartPanel.validate();

            soilMoistureGHChart = new GHChart("Soil Moisture", DataTypes.SOIL_MOISTURE.columnName(), "Date/Time", "Value");
            soilMoistureDataSeries = soilMoistureGHChart.getTimeSeries();
            ChartPanel soilMoistureChart = soilMoistureGHChart.generateChartPanel();
            soilMoistureChart.setSize(soilMoistureChartPanel.getSize());
            soilMoistureChartPanel.add(soilMoistureChart, BorderLayout.CENTER);
            soilMoistureChartPanel.validate();

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private Color getTexFiledBackground(int val) {
        int red = (255 / 100) * val;
        int grn = 255 - (255 / 100) * val;

        Color c = new Color(red, grn, 0);
        return c;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        titleLabel = new javax.swing.JLabel();
        userLabel = new javax.swing.JLabel();
        logoutButton = new javax.swing.JButton();
        summaryPanel = new javax.swing.JPanel();
        tempLabel = new javax.swing.JLabel();
        lightLabel = new javax.swing.JLabel();
        humidityLabel = new javax.swing.JLabel();
        pressureLabel = new javax.swing.JLabel();
        phLabel = new javax.swing.JLabel();
        soilMoistureLabel = new javax.swing.JLabel();
        tempTextField = new javax.swing.JTextField();
        lightTextField = new javax.swing.JTextField();
        humidityTextField = new javax.swing.JTextField();
        pressureTextField = new javax.swing.JTextField();
        phTextField = new javax.swing.JTextField();
        soilMoistureTextField = new javax.swing.JTextField();
        ghSummaryLabel = new javax.swing.JLabel();
        ghSummaryComboBox = new javax.swing.JComboBox<>();
        reloadGreenhousesButton = new javax.swing.JButton();
        mainTabbedPane = new javax.swing.JTabbedPane();
        dashboardPanel = new javax.swing.JPanel();
        dashboardSubPanel = new javax.swing.JPanel();
        ghLabel = new javax.swing.JLabel();
        typeLabel = new javax.swing.JLabel();
        ipLabel = new javax.swing.JLabel();
        ghComboBox = new javax.swing.JComboBox<>();
        deviceTypeComboBox = new javax.swing.JComboBox<>();
        ipComboBox = new javax.swing.JComboBox<>();
        startDateLabel = new javax.swing.JLabel();
        endDateLabel = new javax.swing.JLabel();
        startTimeLabel = new javax.swing.JLabel();
        endTimeLabel = new javax.swing.JLabel();
        startDateChooser = new com.toedter.calendar.JDateChooser();
        endDateChooser = new com.toedter.calendar.JDateChooser();
        startTimeHrLabel = new javax.swing.JLabel();
        startTimeMinLabel = new javax.swing.JLabel();
        startTimeHrSpinner = new javax.swing.JSpinner();
        startTimeMinSpinner = new javax.swing.JSpinner();
        startTimePeriodSpinner = new javax.swing.JSpinner();
        endTimeHrSpinner = new javax.swing.JSpinner();
        endTimeHrLabel = new javax.swing.JLabel();
        endTimeMinSpinner = new javax.swing.JSpinner();
        endTimeMinLabel = new javax.swing.JLabel();
        endTimePeriodSpinner = new javax.swing.JSpinner();
        viewDataButton = new javax.swing.JButton();
        dashboardDataTabbedPane = new javax.swing.JTabbedPane();
        tempDataPanel = new javax.swing.JPanel();
        tempChartPanel = new javax.swing.JPanel();
        tempCSVButton = new javax.swing.JButton();
        tempJPEGButton = new javax.swing.JButton();
        tempDataShowButton = new javax.swing.JButton();
        pressureDataPanel = new javax.swing.JPanel();
        pressureChartPanel = new javax.swing.JPanel();
        pressureJPEGButton = new javax.swing.JButton();
        pressureCSVButton = new javax.swing.JButton();
        pressureDataShowButton = new javax.swing.JButton();
        lightDataPanel = new javax.swing.JPanel();
        lightChartPanel = new javax.swing.JPanel();
        lightJPEGButton = new javax.swing.JButton();
        lightCSVButton = new javax.swing.JButton();
        lightShowDataButton = new javax.swing.JButton();
        phDataPanel = new javax.swing.JPanel();
        phChartPanel = new javax.swing.JPanel();
        phJPEGButton = new javax.swing.JButton();
        phCSVButton = new javax.swing.JButton();
        phShowDataButton = new javax.swing.JButton();
        humidityDataPanel = new javax.swing.JPanel();
        humidityChartPanel = new javax.swing.JPanel();
        humidityJPEGButton = new javax.swing.JButton();
        humidityCSVButton = new javax.swing.JButton();
        humidityShowDataButton = new javax.swing.JButton();
        soilMoisturePanel = new javax.swing.JPanel();
        soilMoistureChartPanel = new javax.swing.JPanel();
        soilMoistureJPEGButton = new javax.swing.JButton();
        soilMoistureCSVButton = new javax.swing.JButton();
        soilMoistShowDataButton = new javax.swing.JButton();
        allCSVButton = new javax.swing.JButton();
        allJPEGButton = new javax.swing.JButton();
        controllerPanel = new javax.swing.JPanel();
        controllerSubPanel = new javax.swing.JPanel();
        ghLabel2 = new javax.swing.JLabel();
        ipLabel2 = new javax.swing.JLabel();
        ghControllerComboBox = new javax.swing.JComboBox<>();
        ipControllerComboBox = new javax.swing.JComboBox<>();
        allCommandButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        commandsTextArea = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        addIpButton = new javax.swing.JButton();
        removeIpButton = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        dataFormattedTextField = new javax.swing.JFormattedTextField();
        addDataButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newFileMenu = new javax.swing.JMenu();
        newNodeControllerMenuItem = new javax.swing.JMenuItem();
        newGreenhouseMenuItem = new javax.swing.JMenuItem();
        restartFileMenuItem = new javax.swing.JMenuItem();
        exitFileMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        newFileMenu1 = new javax.swing.JMenu();
        updateNodeControllerMenuItem = new javax.swing.JMenuItem();
        updateGreenhouseMenuItem = new javax.swing.JMenuItem();
        updatePasswordMenuItem = new javax.swing.JMenuItem();
        jMenu1 = new javax.swing.JMenu();
        serialPortConnMenuItem = new javax.swing.JMenuItem();
        dbConnMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new java.awt.GridLayout(1, 1));

        titleLabel.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        titleLabel.setText("Greenhouse Manager");

        userLabel.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        userLabel.setText("Hi, USER_NAME");

        logoutButton.setText("Logout");
        logoutButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                logoutButtonActionPerformed(evt);
            }
        });

        summaryPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Summary", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        tempLabel.setText("Temperature :");

        lightLabel.setText("Light :");

        humidityLabel.setText("Humidity :");

        pressureLabel.setText("Pressure :");

        phLabel.setText("PH :");

        soilMoistureLabel.setText("Soil Moisture :");

        tempTextField.setEditable(false);
        tempTextField.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        tempTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        lightTextField.setEditable(false);
        lightTextField.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        lightTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        humidityTextField.setEditable(false);
        humidityTextField.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        humidityTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        pressureTextField.setEditable(false);
        pressureTextField.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        pressureTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        phTextField.setEditable(false);
        phTextField.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        phTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        soilMoistureTextField.setEditable(false);
        soilMoistureTextField.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        soilMoistureTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);

        ghSummaryLabel.setText("Green House :");

        ghSummaryComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "GH 1", "GH 2", "GH 3", "GH 4" }));
        ghSummaryComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ghSummaryComboBoxActionPerformed(evt);
            }
        });

        reloadGreenhousesButton.setText("Reload Greenhouses");
        reloadGreenhousesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reloadGreenhousesButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout summaryPanelLayout = new javax.swing.GroupLayout(summaryPanel);
        summaryPanel.setLayout(summaryPanelLayout);
        summaryPanelLayout.setHorizontalGroup(
            summaryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(summaryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(summaryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(summaryPanelLayout.createSequentialGroup()
                        .addComponent(tempLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(tempTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pressureLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(pressureTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(humidityLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(humidityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lightLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(phLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(phTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(soilMoistureLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(soilMoistureTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 78, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(summaryPanelLayout.createSequentialGroup()
                        .addComponent(ghSummaryLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(ghSummaryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(reloadGreenhousesButton)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        summaryPanelLayout.setVerticalGroup(
            summaryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, summaryPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(summaryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ghSummaryLabel)
                    .addComponent(ghSummaryComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(reloadGreenhousesButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(summaryPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tempLabel)
                    .addComponent(pressureLabel)
                    .addComponent(tempTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pressureTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(humidityLabel)
                    .addComponent(humidityTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lightLabel)
                    .addComponent(lightTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(phLabel)
                    .addComponent(phTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(soilMoistureTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(soilMoistureLabel))
                .addContainerGap())
        );

        mainTabbedPane.setTabPlacement(javax.swing.JTabbedPane.LEFT);
        mainTabbedPane.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N

        dashboardSubPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Data", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        ghLabel.setText("Green House :");

        typeLabel.setText("Device Type :");

        ipLabel.setText("IP :");

        ghComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "GH 1", "GH 2", "GH 3", "GH 4" }));
        ghComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ghComboBoxActionPerformed(evt);
            }
        });

        deviceTypeComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Node", "Controller" }));
        deviceTypeComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deviceTypeComboBoxActionPerformed(evt);
            }
        });

        ipComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "192.168.8.1", "192.168.8.2", "192.168.8.3", "192.168.8.4" }));

        startDateLabel.setText("Start Date :");

        endDateLabel.setText("End Date :");

        startTimeLabel.setText("Time :");

        endTimeLabel.setText("Time :");

        startTimeHrLabel.setText("h");

        startTimeMinLabel.setText("m");

        startTimeHrSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, 12, 1));

        startTimeMinSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 59, 1));

        startTimePeriodSpinner.setModel(new javax.swing.SpinnerListModel(new String[] {"AM", "PM"}));

        endTimeHrSpinner.setModel(new javax.swing.SpinnerNumberModel(1, 1, 12, 1));

        endTimeHrLabel.setText("h");

        endTimeMinSpinner.setModel(new javax.swing.SpinnerNumberModel(0, 0, 59, 1));

        endTimeMinLabel.setText("m");

        endTimePeriodSpinner.setModel(new javax.swing.SpinnerListModel(new String[] {"AM", "PM"}));

        viewDataButton.setText("View Data");
        viewDataButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewDataButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tempChartPanelLayout = new javax.swing.GroupLayout(tempChartPanel);
        tempChartPanel.setLayout(tempChartPanelLayout);
        tempChartPanelLayout.setHorizontalGroup(
            tempChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        tempChartPanelLayout.setVerticalGroup(
            tempChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 323, Short.MAX_VALUE)
        );

        tempCSVButton.setText("To CSV");
        tempCSVButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tempCSVButtonActionPerformed(evt);
            }
        });

        tempJPEGButton.setText("To JPEG");
        tempJPEGButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tempJPEGButtonActionPerformed(evt);
            }
        });

        tempDataShowButton.setText("Show Data");
        tempDataShowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                tempDataShowButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout tempDataPanelLayout = new javax.swing.GroupLayout(tempDataPanel);
        tempDataPanel.setLayout(tempDataPanelLayout);
        tempDataPanelLayout.setHorizontalGroup(
            tempDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tempDataPanelLayout.createSequentialGroup()
                .addGroup(tempDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(tempDataPanelLayout.createSequentialGroup()
                        .addContainerGap(590, Short.MAX_VALUE)
                        .addComponent(tempDataShowButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tempJPEGButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(tempCSVButton))
                    .addComponent(tempChartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        tempDataPanelLayout.setVerticalGroup(
            tempDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(tempDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(tempChartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(tempDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(tempCSVButton)
                    .addComponent(tempJPEGButton)
                    .addComponent(tempDataShowButton))
                .addContainerGap())
        );

        dashboardDataTabbedPane.addTab("Temperature", tempDataPanel);

        javax.swing.GroupLayout pressureChartPanelLayout = new javax.swing.GroupLayout(pressureChartPanel);
        pressureChartPanel.setLayout(pressureChartPanelLayout);
        pressureChartPanelLayout.setHorizontalGroup(
            pressureChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 815, Short.MAX_VALUE)
        );
        pressureChartPanelLayout.setVerticalGroup(
            pressureChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 323, Short.MAX_VALUE)
        );

        pressureJPEGButton.setText("To JPEG");
        pressureJPEGButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pressureJPEGButtonActionPerformed(evt);
            }
        });

        pressureCSVButton.setText("To CSV");
        pressureCSVButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pressureCSVButtonActionPerformed(evt);
            }
        });

        pressureDataShowButton.setText("Show Data");
        pressureDataShowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pressureDataShowButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pressureDataPanelLayout = new javax.swing.GroupLayout(pressureDataPanel);
        pressureDataPanel.setLayout(pressureDataPanelLayout);
        pressureDataPanelLayout.setHorizontalGroup(
            pressureDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pressureDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pressureDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pressureChartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pressureDataPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(pressureDataShowButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pressureJPEGButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pressureCSVButton)))
                .addContainerGap())
        );
        pressureDataPanelLayout.setVerticalGroup(
            pressureDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pressureDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pressureChartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pressureDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(pressureCSVButton)
                    .addComponent(pressureJPEGButton)
                    .addComponent(pressureDataShowButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dashboardDataTabbedPane.addTab("Pressure", pressureDataPanel);

        javax.swing.GroupLayout lightChartPanelLayout = new javax.swing.GroupLayout(lightChartPanel);
        lightChartPanel.setLayout(lightChartPanelLayout);
        lightChartPanelLayout.setHorizontalGroup(
            lightChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 815, Short.MAX_VALUE)
        );
        lightChartPanelLayout.setVerticalGroup(
            lightChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 323, Short.MAX_VALUE)
        );

        lightJPEGButton.setText("To JPEG");
        lightJPEGButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lightJPEGButtonActionPerformed(evt);
            }
        });

        lightCSVButton.setText("To CSV");
        lightCSVButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lightCSVButtonActionPerformed(evt);
            }
        });

        lightShowDataButton.setText("Show Data");
        lightShowDataButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lightShowDataButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout lightDataPanelLayout = new javax.swing.GroupLayout(lightDataPanel);
        lightDataPanel.setLayout(lightDataPanelLayout);
        lightDataPanelLayout.setHorizontalGroup(
            lightDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lightDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(lightDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lightChartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, lightDataPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lightShowDataButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lightJPEGButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lightCSVButton)))
                .addContainerGap())
        );
        lightDataPanelLayout.setVerticalGroup(
            lightDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(lightDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lightChartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(lightDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lightCSVButton)
                    .addComponent(lightJPEGButton)
                    .addComponent(lightShowDataButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dashboardDataTabbedPane.addTab("Light", lightDataPanel);

        javax.swing.GroupLayout phChartPanelLayout = new javax.swing.GroupLayout(phChartPanel);
        phChartPanel.setLayout(phChartPanelLayout);
        phChartPanelLayout.setHorizontalGroup(
            phChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 815, Short.MAX_VALUE)
        );
        phChartPanelLayout.setVerticalGroup(
            phChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 323, Short.MAX_VALUE)
        );

        phJPEGButton.setText("To JPEG");
        phJPEGButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phJPEGButtonActionPerformed(evt);
            }
        });

        phCSVButton.setText("To CSV");
        phCSVButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phCSVButtonActionPerformed(evt);
            }
        });

        phShowDataButton.setText("Show Data");
        phShowDataButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                phShowDataButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout phDataPanelLayout = new javax.swing.GroupLayout(phDataPanel);
        phDataPanel.setLayout(phDataPanelLayout);
        phDataPanelLayout.setHorizontalGroup(
            phDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(phDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(phDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(phChartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, phDataPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(phShowDataButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(phJPEGButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(phCSVButton)))
                .addContainerGap())
        );
        phDataPanelLayout.setVerticalGroup(
            phDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(phDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(phChartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(phDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(phCSVButton)
                    .addComponent(phJPEGButton)
                    .addComponent(phShowDataButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dashboardDataTabbedPane.addTab("PH", phDataPanel);

        javax.swing.GroupLayout humidityChartPanelLayout = new javax.swing.GroupLayout(humidityChartPanel);
        humidityChartPanel.setLayout(humidityChartPanelLayout);
        humidityChartPanelLayout.setHorizontalGroup(
            humidityChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 815, Short.MAX_VALUE)
        );
        humidityChartPanelLayout.setVerticalGroup(
            humidityChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 323, Short.MAX_VALUE)
        );

        humidityJPEGButton.setText("To JPEG");
        humidityJPEGButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                humidityJPEGButtonActionPerformed(evt);
            }
        });

        humidityCSVButton.setText("To CSV");
        humidityCSVButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                humidityCSVButtonActionPerformed(evt);
            }
        });

        humidityShowDataButton.setText("Show Data");
        humidityShowDataButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                humidityShowDataButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout humidityDataPanelLayout = new javax.swing.GroupLayout(humidityDataPanel);
        humidityDataPanel.setLayout(humidityDataPanelLayout);
        humidityDataPanelLayout.setHorizontalGroup(
            humidityDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(humidityDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(humidityDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(humidityChartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, humidityDataPanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(humidityShowDataButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(humidityJPEGButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(humidityCSVButton)))
                .addContainerGap())
        );
        humidityDataPanelLayout.setVerticalGroup(
            humidityDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(humidityDataPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(humidityChartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(humidityDataPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(humidityCSVButton)
                    .addComponent(humidityJPEGButton)
                    .addComponent(humidityShowDataButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dashboardDataTabbedPane.addTab("Humidity", humidityDataPanel);

        javax.swing.GroupLayout soilMoistureChartPanelLayout = new javax.swing.GroupLayout(soilMoistureChartPanel);
        soilMoistureChartPanel.setLayout(soilMoistureChartPanelLayout);
        soilMoistureChartPanelLayout.setHorizontalGroup(
            soilMoistureChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 815, Short.MAX_VALUE)
        );
        soilMoistureChartPanelLayout.setVerticalGroup(
            soilMoistureChartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 323, Short.MAX_VALUE)
        );

        soilMoistureJPEGButton.setText("To JPEG");
        soilMoistureJPEGButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                soilMoistureJPEGButtonActionPerformed(evt);
            }
        });

        soilMoistureCSVButton.setText("To CSV");
        soilMoistureCSVButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                soilMoistureCSVButtonActionPerformed(evt);
            }
        });

        soilMoistShowDataButton.setText("Show Data");
        soilMoistShowDataButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                soilMoistShowDataButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout soilMoisturePanelLayout = new javax.swing.GroupLayout(soilMoisturePanel);
        soilMoisturePanel.setLayout(soilMoisturePanelLayout);
        soilMoisturePanelLayout.setHorizontalGroup(
            soilMoisturePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(soilMoisturePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(soilMoisturePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(soilMoistureChartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, soilMoisturePanelLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(soilMoistShowDataButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(soilMoistureJPEGButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(soilMoistureCSVButton)))
                .addContainerGap())
        );
        soilMoisturePanelLayout.setVerticalGroup(
            soilMoisturePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(soilMoisturePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(soilMoistureChartPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(soilMoisturePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(soilMoistureCSVButton)
                    .addComponent(soilMoistureJPEGButton)
                    .addComponent(soilMoistShowDataButton))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        dashboardDataTabbedPane.addTab("Soil Moisture", soilMoisturePanel);

        allCSVButton.setText("Generate All CSVs");
        allCSVButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allCSVButtonActionPerformed(evt);
            }
        });

        allJPEGButton.setText("Generate All JPEGs");
        allJPEGButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allJPEGButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout dashboardSubPanelLayout = new javax.swing.GroupLayout(dashboardSubPanel);
        dashboardSubPanel.setLayout(dashboardSubPanelLayout);
        dashboardSubPanelLayout.setHorizontalGroup(
            dashboardSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardSubPanelLayout.createSequentialGroup()
                .addGroup(dashboardSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(dashboardDataTabbedPane, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, dashboardSubPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(dashboardSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(ipLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ghLabel)
                            .addComponent(typeLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(dashboardSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(dashboardSubPanelLayout.createSequentialGroup()
                                .addGroup(dashboardSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dashboardSubPanelLayout.createSequentialGroup()
                                        .addComponent(ghComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18))
                                    .addGroup(dashboardSubPanelLayout.createSequentialGroup()
                                        .addComponent(deviceTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(18, 18, 18)))
                                .addGroup(dashboardSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(endDateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(startDateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(dashboardSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(startDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(endDateChooser, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addGroup(dashboardSubPanelLayout.createSequentialGroup()
                                .addComponent(ipComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(allJPEGButton)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dashboardSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dashboardSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(dashboardSubPanelLayout.createSequentialGroup()
                                    .addComponent(endTimeLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(endTimeHrSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(endTimeHrLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(endTimeMinSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(endTimeMinLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(endTimePeriodSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(dashboardSubPanelLayout.createSequentialGroup()
                                    .addComponent(startTimeLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(startTimeHrSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(startTimeHrLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(startTimeMinSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(startTimeMinLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(startTimePeriodSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dashboardSubPanelLayout.createSequentialGroup()
                                .addComponent(allCSVButton)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(viewDataButton)))))
                .addContainerGap())
        );
        dashboardSubPanelLayout.setVerticalGroup(
            dashboardSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardSubPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(dashboardSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dashboardSubPanelLayout.createSequentialGroup()
                        .addGroup(dashboardSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(dashboardSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(ghLabel)
                                .addComponent(ghComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(startDateLabel))
                            .addComponent(startDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dashboardSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(endDateLabel)
                            .addGroup(dashboardSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(typeLabel)
                                .addComponent(deviceTypeComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(endDateChooser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(dashboardSubPanelLayout.createSequentialGroup()
                        .addGroup(dashboardSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(startTimeLabel)
                            .addComponent(startTimeHrLabel)
                            .addComponent(startTimeMinLabel)
                            .addComponent(startTimeHrSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(startTimeMinSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(startTimePeriodSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dashboardSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(endTimeLabel)
                            .addComponent(endTimeHrLabel)
                            .addComponent(endTimeMinLabel)
                            .addComponent(endTimeHrSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(endTimeMinSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(endTimePeriodSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGroup(dashboardSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(dashboardSubPanelLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(dashboardSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(viewDataButton)
                            .addComponent(allCSVButton)
                            .addComponent(allJPEGButton)))
                    .addGroup(dashboardSubPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(dashboardSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(ipComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(ipLabel))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dashboardDataTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        javax.swing.GroupLayout dashboardPanelLayout = new javax.swing.GroupLayout(dashboardPanel);
        dashboardPanel.setLayout(dashboardPanelLayout);
        dashboardPanelLayout.setHorizontalGroup(
            dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, dashboardPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dashboardSubPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        dashboardPanelLayout.setVerticalGroup(
            dashboardPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(dashboardPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(dashboardSubPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        mainTabbedPane.addTab("Dashboard", dashboardPanel);

        controllerSubPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Data", javax.swing.border.TitledBorder.LEFT, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 1, 12))); // NOI18N

        ghLabel2.setText("Green House :");

        ipLabel2.setText("IP :");

        ghControllerComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ghControllerComboBoxActionPerformed(evt);
            }
        });

        allCommandButton.setText("Send Commands");
        allCommandButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                allCommandButtonActionPerformed(evt);
            }
        });

        commandsTextArea.setEditable(false);
        commandsTextArea.setColumns(20);
        commandsTextArea.setRows(5);
        jScrollPane1.setViewportView(commandsTextArea);

        jLabel1.setText("Command :");

        addIpButton.setText("Add IP");
        addIpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addIpButtonActionPerformed(evt);
            }
        });

        removeIpButton.setText("Remove IP");
        removeIpButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                removeIpButtonActionPerformed(evt);
            }
        });

        jLabel2.setText("Data :");

        try {
            dataFormattedTextField.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.MaskFormatter("######")));
        } catch (java.text.ParseException ex) {
            ex.printStackTrace();
        }
        dataFormattedTextField.setText("000000");

        addDataButton.setText("Add Data");
        addDataButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addDataButtonActionPerformed(evt);
            }
        });

        jLabel3.setText("Note : Data field must contains six '1' or '0' values.");

        javax.swing.GroupLayout controllerSubPanelLayout = new javax.swing.GroupLayout(controllerSubPanel);
        controllerSubPanel.setLayout(controllerSubPanelLayout);
        controllerSubPanelLayout.setHorizontalGroup(
            controllerSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controllerSubPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(controllerSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(controllerSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(allCommandButton, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(controllerSubPanelLayout.createSequentialGroup()
                            .addGroup(controllerSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(ghLabel2)
                                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING))
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addGroup(controllerSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 546, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(controllerSubPanelLayout.createSequentialGroup()
                                    .addComponent(ghControllerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(23, 23, 23)
                                    .addComponent(ipLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                    .addComponent(ipControllerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(addIpButton)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(removeIpButton))
                                .addGroup(controllerSubPanelLayout.createSequentialGroup()
                                    .addComponent(dataFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 148, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(addDataButton)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel3)))))
                    .addComponent(jLabel2))
                .addContainerGap(211, Short.MAX_VALUE))
        );
        controllerSubPanelLayout.setVerticalGroup(
            controllerSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(controllerSubPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(controllerSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ghLabel2)
                    .addComponent(ghControllerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ipControllerComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(ipLabel2)
                    .addComponent(addIpButton)
                    .addComponent(removeIpButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(controllerSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(dataFormattedTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(addDataButton)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(controllerSubPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(18, 18, 18)
                .addComponent(allCommandButton)
                .addContainerGap(283, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout controllerPanelLayout = new javax.swing.GroupLayout(controllerPanel);
        controllerPanel.setLayout(controllerPanelLayout);
        controllerPanelLayout.setHorizontalGroup(
            controllerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, controllerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(controllerSubPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        controllerPanelLayout.setVerticalGroup(
            controllerPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, controllerPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(controllerSubPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        mainTabbedPane.addTab("Control Panel", controllerPanel);

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(mainTabbedPane)
                    .addComponent(summaryPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, mainPanelLayout.createSequentialGroup()
                        .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(userLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(logoutButton)))
                .addContainerGap())
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(userLabel)
                        .addComponent(logoutButton))
                    .addComponent(titleLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(summaryPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainTabbedPane, javax.swing.GroupLayout.PREFERRED_SIZE, 543, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        getContentPane().add(mainPanel);

        fileMenu.setText("File");

        newFileMenu.setText("New...");

        newNodeControllerMenuItem.setText("Node/Controller");
        newFileMenu.add(newNodeControllerMenuItem);

        newGreenhouseMenuItem.setText("Greenhouse");
        newGreenhouseMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newGreenhouseMenuItemActionPerformed(evt);
            }
        });
        newFileMenu.add(newGreenhouseMenuItem);

        fileMenu.add(newFileMenu);

        restartFileMenuItem.setText("Restart");
        restartFileMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restartFileMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(restartFileMenuItem);

        exitFileMenuItem.setText("Exit");
        fileMenu.add(exitFileMenuItem);

        menuBar.add(fileMenu);

        editMenu.setText("Edit");

        newFileMenu1.setText("Update");

        updateNodeControllerMenuItem.setText("Node/Controller");
        newFileMenu1.add(updateNodeControllerMenuItem);

        updateGreenhouseMenuItem.setText("Greenhouse");
        newFileMenu1.add(updateGreenhouseMenuItem);

        updatePasswordMenuItem.setText("Password");
        updatePasswordMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updatePasswordMenuItemActionPerformed(evt);
            }
        });
        newFileMenu1.add(updatePasswordMenuItem);

        editMenu.add(newFileMenu1);

        menuBar.add(editMenu);

        jMenu1.setText("Connection");

        serialPortConnMenuItem.setText("Serial Port Connection");
        serialPortConnMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serialPortConnMenuItemActionPerformed(evt);
            }
        });
        jMenu1.add(serialPortConnMenuItem);

        dbConnMenuItem.setText("Database Connection");
        jMenu1.add(dbConnMenuItem);

        menuBar.add(jMenu1);

        setJMenuBar(menuBar);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void viewDataButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewDataButtonActionPerformed
        String ghName = (String) ghComboBox.getSelectedItem();
        lastDashboardGH = ghName;
//        String devType = (String) deviceTypeComboBox.getSelectedItem();
//        String ip = (String) ipComboBox.getSelectedItem();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = startDateChooser.getDate();
        Date endDate = endDateChooser.getDate();

        int startHr = (int) startTimeHrSpinner.getValue();
        int startMin = (int) startTimeMinSpinner.getValue();
        int endHr = (int) endTimeHrSpinner.getValue();
        int endMin = (int) endTimeMinSpinner.getValue();
        String startDateTime = "";
        String endDateTime = "";
        if (startDate != null) {
            startDateTime = dateFormat.format(startDate) + " " + startHr + ":" + startMin + ":00";
        } else {
            startDateTime = "0000-01-01 00:00:00";
        }

        if (endDate != null) {
            endDateTime = dateFormat.format(endDate) + " " + endHr + ":" + endMin + ":00";
        } else {
            endDateTime = "9999-12-31 23:59:59";
        }

        try {
            ArrayList<ChartData> tempData = ChartDataDAO.getChartDataFromTo(DataTypes.TEMPERATURE.columnName(), ghName, startDateTime, endDateTime);
            ArrayList<ChartData> lightData = ChartDataDAO.getChartDataFromTo(DataTypes.LIGHT.columnName(), ghName, startDateTime, endDateTime);
            ArrayList<ChartData> humidityData = ChartDataDAO.getChartDataFromTo(DataTypes.HUMIDITY.columnName(), ghName, startDateTime, endDateTime);
            ArrayList<ChartData> phData = ChartDataDAO.getChartDataFromTo(DataTypes.PH.columnName(), ghName, startDateTime, endDateTime);
            ArrayList<ChartData> pressureData = ChartDataDAO.getChartDataFromTo(DataTypes.PRESSURE.columnName(), ghName, startDateTime, endDateTime);
            ArrayList<ChartData> soilData = ChartDataDAO.getChartDataFromTo(DataTypes.SOIL_MOISTURE.columnName(), ghName, startDateTime, endDateTime);

            tempDataSeries.clear();
            lightDataSeries.clear();
            humidityDataSeries.clear();
            phDataSeries.clear();
            pressureDataSeries.clear();
            soilMoistureDataSeries.clear();

            for (ChartData tdm : tempData) {
                Calendar dateCal = Calendar.getInstance();
                dateCal.setTime(tdm.getDate());
                Calendar timeCal = Calendar.getInstance();
                timeCal.setTime(tdm.getTime());
                dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
                dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
                dateCal.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));
                tempDataSeries.addOrUpdate(new Minute(dateCal.getTime()), Double.parseDouble(tdm.getData()));
            }

            for (ChartData tdm : lightData) {
                Calendar dateCal = Calendar.getInstance();
                dateCal.setTime(tdm.getDate());
                Calendar timeCal = Calendar.getInstance();
                timeCal.setTime(tdm.getTime());
                dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
                dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
                dateCal.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));
                lightDataSeries.addOrUpdate(new Minute(dateCal.getTime()), Double.parseDouble(tdm.getData()));
            }

            for (ChartData tdm : humidityData) {
                Calendar dateCal = Calendar.getInstance();
                dateCal.setTime(tdm.getDate());
                Calendar timeCal = Calendar.getInstance();
                timeCal.setTime(tdm.getTime());
                dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
                dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
                dateCal.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));
                humidityDataSeries.addOrUpdate(new Minute(dateCal.getTime()), Double.parseDouble(tdm.getData()));
            }

            for (ChartData tdm : phData) {
                Calendar dateCal = Calendar.getInstance();
                dateCal.setTime(tdm.getDate());
                Calendar timeCal = Calendar.getInstance();
                timeCal.setTime(tdm.getTime());
                dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
                dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
                dateCal.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));
                phDataSeries.addOrUpdate(new Minute(dateCal.getTime()), Double.parseDouble(tdm.getData()));
            }

            for (ChartData tdm : pressureData) {
                Calendar dateCal = Calendar.getInstance();
                dateCal.setTime(tdm.getDate());
                Calendar timeCal = Calendar.getInstance();
                timeCal.setTime(tdm.getTime());
                dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
                dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
                dateCal.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));
                pressureDataSeries.addOrUpdate(new Minute(dateCal.getTime()), Double.parseDouble(tdm.getData()));
            }

            for (ChartData tdm : soilData) {
                Calendar dateCal = Calendar.getInstance();
                dateCal.setTime(tdm.getDate());
                Calendar timeCal = Calendar.getInstance();
                timeCal.setTime(tdm.getTime());
                dateCal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
                dateCal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));
                dateCal.set(Calendar.SECOND, timeCal.get(Calendar.SECOND));
                soilMoistureDataSeries.addOrUpdate(new Minute(dateCal.getTime()), Double.parseDouble(tdm.getData()));
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_viewDataButtonActionPerformed

    private void ghComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ghComboBoxActionPerformed
        ipComboBox.removeAllItems();
        String gh = (String) ghComboBox.getSelectedItem();
        String devType = (String) deviceTypeComboBox.getSelectedItem();
        System.out.println("ghComboItemStateChanged :: " + gh);

        try {
            ArrayList<Devices> allDevicesForTypeAndGH = DeviceDAO.getAllDevicesForTypeAndGH(gh, devType);
            for (Devices devices : allDevicesForTypeAndGH) {
                ipComboBox.addItem(devices.getIpAddr());
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_ghComboBoxActionPerformed

    private void deviceTypeComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deviceTypeComboBoxActionPerformed
        ipComboBox.removeAllItems();
        String gh = (String) ghComboBox.getSelectedItem();
        String devType = (String) deviceTypeComboBox.getSelectedItem();

        try {
            ArrayList<Devices> allDevicesForTypeAndGH = DeviceDAO.getAllDevicesForTypeAndGH(gh, devType);
            for (Devices devices : allDevicesForTypeAndGH) {
                ipComboBox.addItem(devices.getIpAddr());
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_deviceTypeComboBoxActionPerformed

    private void ghSummaryComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ghSummaryComboBoxActionPerformed
        String gh = (String) ghSummaryComboBox.getSelectedItem();

        try {
            Data d = DataDAO.getSummarizedDataForGH(gh);
            tempTextField.setText(d.getTemperature());
            lightTextField.setText(d.getLight());
            pressureTextField.setText(d.getPressure());
            humidityTextField.setText(d.getHumidity());
            phTextField.setText(d.getPh());
            soilMoistureTextField.setText(d.getSoilMoisture());

//            Double tempD = new Double(d.getTemperature());
//            Double lightD = new Double(d.getLight());
//            Double humidityD = new Double(d.getHumidity());
//            Double phD = new Double(d.getPh());
//            Double pressureD = new Double(d.getPressure());
//            Double soilMD = new Double(d.getSoilMoisture());
//
//            tempTextField.setBackground(getTexFiledBackground(tempD.intValue()));
//            lightTextField.setBackground(getTexFiledBackground(lightD.intValue()));
//            pressureTextField.setBackground(getTexFiledBackground(pressureD.intValue()));
//            humidityTextField.setBackground(getTexFiledBackground(humidityD.intValue()));
//            phTextField.setBackground(getTexFiledBackground(phD.intValue()));
//            soilMoistureTextField.setBackground(getTexFiledBackground(soilMD.intValue()));
            tempTextField.setBackground(getTexFiledBackground(Integer.parseInt(d.getTemperature())));
            lightTextField.setBackground(getTexFiledBackground(Integer.parseInt(d.getLight())));
            pressureTextField.setBackground(getTexFiledBackground(Integer.parseInt(d.getPressure())));
            humidityTextField.setBackground(getTexFiledBackground(Integer.parseInt(d.getHumidity())));
            phTextField.setBackground(getTexFiledBackground(Integer.parseInt(d.getPh())));
            soilMoistureTextField.setBackground(getTexFiledBackground(Integer.parseInt(d.getSoilMoisture())));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_ghSummaryComboBoxActionPerformed

    private void restartFileMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restartFileMenuItemActionPerformed
        this.setVisible(false);
        GreenHouse gh = new GreenHouse();
        gh.start();
    }//GEN-LAST:event_restartFileMenuItemActionPerformed

    private void updatePasswordMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updatePasswordMenuItemActionPerformed
        UpdatePasswordForm updatePasswordForm = new UpdatePasswordForm(this, true);
        updatePasswordForm.setLocationRelativeTo(this);
        updatePasswordForm.setVisible(true);
    }//GEN-LAST:event_updatePasswordMenuItemActionPerformed

    private void logoutButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_logoutButtonActionPerformed
        this.setVisible(false);
        GreenHouse gh = new GreenHouse();
        gh.start();
    }//GEN-LAST:event_logoutButtonActionPerformed

    private void serialPortConnMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serialPortConnMenuItemActionPerformed
        SerialPortConnnectionForm connnectionForm = new SerialPortConnnectionForm(this, true);
        connnectionForm.setLocationRelativeTo(this);
        connnectionForm.setVisible(true);
    }//GEN-LAST:event_serialPortConnMenuItemActionPerformed

    private void ghControllerComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ghControllerComboBoxActionPerformed
        ipControllerComboBox.removeAllItems();
        String gh = (String) ghControllerComboBox.getSelectedItem();
        String devType = "Node";

        try {
            ArrayList<Devices> allDevicesForTypeAndGH = DeviceDAO.getAllDevicesForTypeAndGH(gh, devType);
            for (Devices devices : allDevicesForTypeAndGH) {
                ipControllerComboBox.addItem(devices.getIpAddr());
            }
            String text = commandsTextArea.getText();
            if (text.length() > 5) {
                String[] split = text.split(";");
                split[1] = "ip";
                String cmmnd = "";
                for (String str : split) {
                    cmmnd += str + ";";
                }
                //Q;192.168.1.5;1,2;P
                if (cmmnd.endsWith(";")) {
                    cmmnd = cmmnd.substring(0, cmmnd.length() - 1);
                }
                commandsTextArea.setText(cmmnd);
                addIpButton.setEnabled(true);
                removeIpButton.setEnabled(false);
            }

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_ghControllerComboBoxActionPerformed

    private void newGreenhouseMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newGreenhouseMenuItemActionPerformed
        NewGreenHouseForm newGreenHouseForm = new NewGreenHouseForm(this, true);
        newGreenHouseForm.setLocationRelativeTo(this);
        newGreenHouseForm.setVisible(true);
    }//GEN-LAST:event_newGreenhouseMenuItemActionPerformed

    private void tempJPEGButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tempJPEGButtonActionPerformed
        try {
            createJPEG(tempGHChart, DataTypes.TEMPERATURE.columnName());
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "File write error occured.");
        }
    }//GEN-LAST:event_tempJPEGButtonActionPerformed

    private void pressureJPEGButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pressureJPEGButtonActionPerformed
        try {
            createJPEG(tempGHChart, DataTypes.TEMPERATURE.columnName());
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "File write error occured.");
        }
    }//GEN-LAST:event_pressureJPEGButtonActionPerformed

    private void lightJPEGButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lightJPEGButtonActionPerformed
        try {
            createJPEG(lightGHChart, DataTypes.LIGHT.columnName());
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "File write error occured.");
        }
    }//GEN-LAST:event_lightJPEGButtonActionPerformed

    private void phJPEGButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phJPEGButtonActionPerformed
        try {
            createJPEG(phGHChart, DataTypes.PH.columnName());
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "File write error occured.");
        }
    }//GEN-LAST:event_phJPEGButtonActionPerformed

    private void humidityJPEGButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_humidityJPEGButtonActionPerformed
        try {
            createJPEG(humidityGHChart, DataTypes.HUMIDITY.columnName());
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "File write error occured.");
        }
    }//GEN-LAST:event_humidityJPEGButtonActionPerformed

    private void soilMoistureJPEGButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soilMoistureJPEGButtonActionPerformed
        try {
            createJPEG(soilMoistureGHChart, DataTypes.SOIL_MOISTURE.columnName());
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "File write error occured.");
        }
    }//GEN-LAST:event_soilMoistureJPEGButtonActionPerformed

    private void soilMoistureCSVButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soilMoistureCSVButtonActionPerformed
        try {
            createCSV(soilMoistureDataSeries, DataTypes.SOIL_MOISTURE.columnName());
        } catch (ParseException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "File write error occured.");
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "File write error occured.");
        }
    }//GEN-LAST:event_soilMoistureCSVButtonActionPerformed

    private void humidityCSVButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_humidityCSVButtonActionPerformed
        try {
            createCSV(humidityDataSeries, DataTypes.HUMIDITY.columnName());
        } catch (ParseException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "File write error occured.");
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "File write error occured.");
        }
    }//GEN-LAST:event_humidityCSVButtonActionPerformed

    private void phCSVButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phCSVButtonActionPerformed
        try {
            createCSV(phDataSeries, DataTypes.PH.columnName());
        } catch (ParseException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "File write error occured.");
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "File write error occured.");
        }
    }//GEN-LAST:event_phCSVButtonActionPerformed

    private void lightCSVButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lightCSVButtonActionPerformed
        try {
            createCSV(lightDataSeries, DataTypes.LIGHT.columnName());
        } catch (ParseException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "File write error occured.");
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "File write error occured.");
        }
    }//GEN-LAST:event_lightCSVButtonActionPerformed

    private void pressureCSVButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pressureCSVButtonActionPerformed
        try {
            createCSV(pressureDataSeries, DataTypes.PRESSURE.columnName());
        } catch (ParseException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "File write error occured.");
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "File write error occured.");
        }
    }//GEN-LAST:event_pressureCSVButtonActionPerformed

    private void tempCSVButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tempCSVButtonActionPerformed
        try {
            createCSV(tempDataSeries, DataTypes.TEMPERATURE.columnName());
        } catch (ParseException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "File write error occured.");
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            JOptionPane.showMessageDialog(this, "File write error occured.");
        }
    }//GEN-LAST:event_tempCSVButtonActionPerformed

    private void allCSVButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allCSVButtonActionPerformed
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = jfc.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            FileWriter fileWriter = null;
            try {
                File dir = jfc.getSelectedFile();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.h.mm.ss.a");
                Date d = new Date();
                String fileName = lastDashboardGH + "_" + dateFormat.format(d);
                File csv = new File(dir.getAbsolutePath() + "\\" + fileName + ".csv");
                fileWriter = new FileWriter(csv);
                BufferedWriter bw = new BufferedWriter(fileWriter);
                List<TimeSeriesDataItem> tempItems = tempDataSeries.getItems();
                List<TimeSeriesDataItem> lightItems = lightDataSeries.getItems();
                List<TimeSeriesDataItem> humidityItems = humidityDataSeries.getItems();
                List<TimeSeriesDataItem> phItems = phDataSeries.getItems();
                List<TimeSeriesDataItem> pressureItems = pressureDataSeries.getItems();
                List<TimeSeriesDataItem> soilMItems = soilMoistureDataSeries.getItems();

                dateFormat = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy");
                SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timeSdf = new SimpleDateFormat("h:mm a");

                bw.append("Date,Time,temperature,light,humidity,ph,pressure,soil moisture");
                bw.newLine();

                for (int i = 0; i < tempItems.size(); i++) {
                    d = dateFormat.parse(tempItems.get(i).getPeriod().toString());
                    String date = dateSdf.format(d);
                    String time = timeSdf.format(d);
                    bw.append(date + "," + time + ","
                            + "" + tempItems.get(i).getValue().toString() + ","
                            + "" + lightItems.get(i).getValue().toString() + ","
                            + "" + humidityItems.get(i).getValue().toString() + ","
                            + "" + phItems.get(i).getValue().toString() + ","
                            + "" + pressureItems.get(i).getValue().toString() + ","
                            + "" + soilMItems.get(i).getValue().toString());
                    bw.newLine();
                }
                bw.flush();
                bw.close();
            } catch (IOException ex) {
                Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ParseException ex) {
                Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                try {
                    fileWriter.close();
                } catch (IOException ex) {
                    Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }//GEN-LAST:event_allCSVButtonActionPerformed

    private void allCommandButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allCommandButtonActionPerformed
        try {
            String commands = commandsTextArea.getText();
            System.out.println("Command : " + commands);

            InputStream in = new ByteArrayInputStream(commands.getBytes(StandardCharsets.UTF_8));
            SerialPort port = SerialPortConnection.getConnection();
            SerialWriter serialWriter = new SerialWriter(port.getOutputStream());
            serialWriter.setInputStream(in);
            serialWriter.run();
        } catch (SQLException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_allCommandButtonActionPerformed

    private void reloadGreenhousesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadGreenhousesButtonActionPerformed
        try {
            ghComboBox.removeAllItems();
            ghSummaryComboBox.removeAllItems();
            ghControllerComboBox.removeAllItems();
            ArrayList<Greenhouse> allGreenhouses = GreenhouseDAO.getAllGreenhouses();
            for (Greenhouse g : allGreenhouses) {
                ghComboBox.addItem(g.getName());
                ghSummaryComboBox.addItem(g.getName());
                ghControllerComboBox.addItem(g.getName());
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_reloadGreenhousesButtonActionPerformed

    private void addIpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addIpButtonActionPerformed
        String ip = (String) ipControllerComboBox.getSelectedItem();
        String text = commandsTextArea.getText();
        String[] split = text.split(";");
        split[1] = ip;
        String cmmnd = "";
        for (String str : split) {
            cmmnd += str + ";";
        }
        //Q;192.168.1.5;1,2;P
        if (cmmnd.endsWith(";")) {
            cmmnd = cmmnd.substring(0, cmmnd.length() - 1);
        }
        commandsTextArea.setText(cmmnd);
        addIpButton.setEnabled(false);
        removeIpButton.setEnabled(true);
    }//GEN-LAST:event_addIpButtonActionPerformed

    private void removeIpButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_removeIpButtonActionPerformed
        String text = commandsTextArea.getText();
        String[] split = text.split(";");
        split[1] = "ip";
        String cmmnd = "";
        for (String str : split) {
            cmmnd += str + ";";
        }
        //Q;192.168.1.5;1,2;P
        if (cmmnd.endsWith(";")) {
            cmmnd = cmmnd.substring(0, cmmnd.length() - 1);
        }
        commandsTextArea.setText(cmmnd);
        addIpButton.setEnabled(true);
        removeIpButton.setEnabled(false);
    }//GEN-LAST:event_removeIpButtonActionPerformed

    private void addDataButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addDataButtonActionPerformed
        String text = commandsTextArea.getText();
        String data = dataFormattedTextField.getText().trim();
        data = data.replaceAll("\\s+", "");
        if (data.length() == 6) {
            String[] split = text.split(";");
            String[] dataArr = split[2].trim().split(",");

            split[2] = data;
            String cmmnd = "";
            for (String string : split) {
                cmmnd += string + ";";
            }
            if (cmmnd.endsWith(";")) {
                cmmnd = cmmnd.substring(0, cmmnd.length() - 1);
            }
            commandsTextArea.setText(cmmnd);
        } else {
            JOptionPane.showMessageDialog(this, "You should enter six didgit binary valule for data.", "Error", JOptionPane.ERROR_MESSAGE);
            dataFormattedTextField.setText(data);
        }

    }//GEN-LAST:event_addDataButtonActionPerformed

    private void tempDataShowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_tempDataShowButtonActionPerformed
        ViewDataDialog vdd = new ViewDataDialog(this, true, tempDataSeries, DataTypes.TEMPERATURE.columnName());
        vdd.setLocationRelativeTo(this);
        vdd.setVisible(true);
    }//GEN-LAST:event_tempDataShowButtonActionPerformed

    private void pressureDataShowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pressureDataShowButtonActionPerformed
        ViewDataDialog vdd = new ViewDataDialog(this, true, pressureDataSeries, DataTypes.PRESSURE.columnName());
        vdd.setLocationRelativeTo(this);
        vdd.setVisible(true);
    }//GEN-LAST:event_pressureDataShowButtonActionPerformed

    private void lightShowDataButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lightShowDataButtonActionPerformed
        ViewDataDialog vdd = new ViewDataDialog(this, true, lightDataSeries, DataTypes.LIGHT.columnName());
        vdd.setLocationRelativeTo(this);
        vdd.setVisible(true);
    }//GEN-LAST:event_lightShowDataButtonActionPerformed

    private void phShowDataButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_phShowDataButtonActionPerformed
        ViewDataDialog vdd = new ViewDataDialog(this, true, phDataSeries, DataTypes.PH.columnName());
        vdd.setLocationRelativeTo(this);
        vdd.setVisible(true);
    }//GEN-LAST:event_phShowDataButtonActionPerformed

    private void humidityShowDataButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_humidityShowDataButtonActionPerformed
        ViewDataDialog vdd = new ViewDataDialog(this, true, humidityDataSeries, DataTypes.HUMIDITY.columnName());
        vdd.setLocationRelativeTo(this);
        vdd.setVisible(true);
    }//GEN-LAST:event_humidityShowDataButtonActionPerformed

    private void soilMoistShowDataButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_soilMoistShowDataButtonActionPerformed
        ViewDataDialog vdd = new ViewDataDialog(this, true, soilMoistureDataSeries, DataTypes.SOIL_MOISTURE.columnName());
        vdd.setLocationRelativeTo(this);
        vdd.setVisible(true);
    }//GEN-LAST:event_soilMoistShowDataButtonActionPerformed

    private void allJPEGButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_allJPEGButtonActionPerformed
        DateAxis domainAxis = new DateAxis("Date/Time");
        NumberAxis rangeAxis = new NumberAxis("Value");
        AbstractXYItemRenderer renderer = new CandlestickRenderer();

        XYPlot mainPlot = new XYPlot();
        mainPlot.setDomainAxis(domainAxis);
        mainPlot.setRangeAxis(rangeAxis);
        mainPlot.setRenderer(renderer);

        // Create a line series
        TimeSeriesCollection timecollection = new TimeSeriesCollection();
        timecollection.addSeries(tempDataSeries);
        timecollection.addSeries(lightDataSeries);
        timecollection.addSeries(humidityDataSeries);
        timecollection.addSeries(phDataSeries);
        timecollection.addSeries(pressureDataSeries);
        timecollection.addSeries(soilMoistureDataSeries);
        mainPlot.setDataset(1, timecollection);
        mainPlot.setRenderer(1, new XYLineAndShapeRenderer(true, false));
        mainPlot.getRenderer(1).setSeriesPaint(0, Color.blue);

        CombinedDomainXYPlot combinedplot = new CombinedDomainXYPlot(domainAxis);
        combinedplot.setDomainGridlinePaint(Color.white);
        combinedplot.setDomainGridlinesVisible(true);
        combinedplot.add(mainPlot, 3);

        // Do some setting up, see the API Doc
        renderer.setSeriesPaint(0, Color.RED);
        renderer.setSeriesPaint(1, Color.YELLOW);
        renderer.setSeriesPaint(2, Color.BLUE);
        renderer.setSeriesPaint(3, Color.GREEN);
        renderer.setSeriesPaint(4, Color.ORANGE);
        renderer.setSeriesPaint(5, Color.BLACK);
        rangeAxis.setAutoRangeIncludesZero(false);

        LegendItemCollection legendItemCollection = new LegendItemCollection();
        LegendItem tempLegend = new LegendItem("Temperature", Color.RED);
        LegendItem lightLegend = new LegendItem("Light", Color.YELLOW);
        LegendItem humidityLegend = new LegendItem("Humidity", Color.BLUE);
        LegendItem phLegend = new LegendItem("PH", Color.GREEN);
        LegendItem pressureLegend = new LegendItem("Pressure", Color.ORANGE);
        LegendItem soilMLegend = new LegendItem("Soil Moisture", Color.BLACK);

        legendItemCollection.add(tempLegend);
        legendItemCollection.add(lightLegend);
        legendItemCollection.add(humidityLegend);
        legendItemCollection.add(phLegend);
        legendItemCollection.add(pressureLegend);
        legendItemCollection.add(soilMLegend);

        mainPlot.setFixedLegendItems(legendItemCollection);
        renderer.setBaseItemLabelsVisible(true);

        JFreeChart chart = new JFreeChart(lastDashboardGH + " Data Graph", null, combinedplot, true);

        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = jfc.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            try {
                File dir = jfc.getSelectedFile();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.h.mm.ss.a");
                Date d = new Date();
                String fileName = lastDashboardGH + " Data Graph" + "_" + dateFormat.format(d);

                File img = new File(dir.getAbsolutePath() + "\\" + fileName + ".jpeg");
                ChartUtilities.saveChartAsJPEG(img, chart, 1280, 720);
                Desktop.getDesktop().open(img);
            } catch (IOException ex) {
                Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_allJPEGButtonActionPerformed

    private void createCSV(TimeSeries data, String chartName) throws ParseException, IOException {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = jfc.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File dir = jfc.getSelectedFile();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.h.mm.ss.a");
            Date d = new Date();
            String fileName = lastDashboardGH + "_" + chartName + "_" + dateFormat.format(d);
            File csv = new File(dir.getAbsolutePath() + "\\" + fileName + ".csv");

            FileWriter fileWriter = new FileWriter(csv);
            BufferedWriter bw = new BufferedWriter(fileWriter);

            List<TimeSeriesDataItem> items = data.getItems();
            dateFormat = new SimpleDateFormat("EEE MMM dd kk:mm:ss z yyyy");
            SimpleDateFormat dateSdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat timeSdf = new SimpleDateFormat("h:mm a");
            bw.append("Date,Time,Value");
            bw.newLine();
            for (TimeSeriesDataItem item : items) {
                d = dateFormat.parse(item.getPeriod().toString());
                String date = dateSdf.format(d);
                String time = timeSdf.format(d);
                String val = item.getValue().toString();
                bw.append(date + "," + time + "," + val);
                bw.newLine();
            }
            bw.flush();
            bw.close();
        }
    }

    private void createJPEG(GHChart gHChart, String chartName) throws IOException {
        JFileChooser jfc = new JFileChooser();
        jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int option = jfc.showSaveDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File dir = jfc.getSelectedFile();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd.h.mm.ss.a");
            Date d = new Date();
            String fileName = chartName + "_" + dateFormat.format(d);

            File img = new File(dir.getAbsolutePath() + "\\" + fileName + ".jpeg");
            ChartUtilities.saveChartAsJPEG(img, gHChart.getJFreeChart(), 1280, 720);
            Desktop.getDesktop().open(img);
        }
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
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addDataButton;
    private javax.swing.JButton addIpButton;
    private javax.swing.JButton allCSVButton;
    private javax.swing.JButton allCommandButton;
    private javax.swing.JButton allJPEGButton;
    private javax.swing.JTextArea commandsTextArea;
    private javax.swing.JPanel controllerPanel;
    private javax.swing.JPanel controllerSubPanel;
    private javax.swing.JTabbedPane dashboardDataTabbedPane;
    private javax.swing.JPanel dashboardPanel;
    private javax.swing.JPanel dashboardSubPanel;
    private javax.swing.JFormattedTextField dataFormattedTextField;
    private javax.swing.JMenuItem dbConnMenuItem;
    private javax.swing.JComboBox<String> deviceTypeComboBox;
    private javax.swing.JMenu editMenu;
    private com.toedter.calendar.JDateChooser endDateChooser;
    private javax.swing.JLabel endDateLabel;
    private javax.swing.JLabel endTimeHrLabel;
    private javax.swing.JSpinner endTimeHrSpinner;
    private javax.swing.JLabel endTimeLabel;
    private javax.swing.JLabel endTimeMinLabel;
    private javax.swing.JSpinner endTimeMinSpinner;
    private javax.swing.JSpinner endTimePeriodSpinner;
    private javax.swing.JMenuItem exitFileMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JComboBox<String> ghComboBox;
    private javax.swing.JComboBox<String> ghControllerComboBox;
    private javax.swing.JLabel ghLabel;
    private javax.swing.JLabel ghLabel2;
    private javax.swing.JComboBox<String> ghSummaryComboBox;
    private javax.swing.JLabel ghSummaryLabel;
    private javax.swing.JButton humidityCSVButton;
    private javax.swing.JPanel humidityChartPanel;
    private javax.swing.JPanel humidityDataPanel;
    private javax.swing.JButton humidityJPEGButton;
    private javax.swing.JLabel humidityLabel;
    private javax.swing.JButton humidityShowDataButton;
    private javax.swing.JTextField humidityTextField;
    private javax.swing.JComboBox<String> ipComboBox;
    private javax.swing.JComboBox<String> ipControllerComboBox;
    private javax.swing.JLabel ipLabel;
    private javax.swing.JLabel ipLabel2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton lightCSVButton;
    private javax.swing.JPanel lightChartPanel;
    private javax.swing.JPanel lightDataPanel;
    private javax.swing.JButton lightJPEGButton;
    private javax.swing.JLabel lightLabel;
    private javax.swing.JButton lightShowDataButton;
    private javax.swing.JTextField lightTextField;
    private javax.swing.JButton logoutButton;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTabbedPane mainTabbedPane;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenu newFileMenu;
    private javax.swing.JMenu newFileMenu1;
    private javax.swing.JMenuItem newGreenhouseMenuItem;
    private javax.swing.JMenuItem newNodeControllerMenuItem;
    private javax.swing.JButton phCSVButton;
    private javax.swing.JPanel phChartPanel;
    private javax.swing.JPanel phDataPanel;
    private javax.swing.JButton phJPEGButton;
    private javax.swing.JLabel phLabel;
    private javax.swing.JButton phShowDataButton;
    private javax.swing.JTextField phTextField;
    private javax.swing.JButton pressureCSVButton;
    private javax.swing.JPanel pressureChartPanel;
    private javax.swing.JPanel pressureDataPanel;
    private javax.swing.JButton pressureDataShowButton;
    private javax.swing.JButton pressureJPEGButton;
    private javax.swing.JLabel pressureLabel;
    private javax.swing.JTextField pressureTextField;
    private javax.swing.JButton reloadGreenhousesButton;
    private javax.swing.JButton removeIpButton;
    private javax.swing.JMenuItem restartFileMenuItem;
    private javax.swing.JMenuItem serialPortConnMenuItem;
    private javax.swing.JButton soilMoistShowDataButton;
    private javax.swing.JButton soilMoistureCSVButton;
    private javax.swing.JPanel soilMoistureChartPanel;
    private javax.swing.JButton soilMoistureJPEGButton;
    private javax.swing.JLabel soilMoistureLabel;
    private javax.swing.JPanel soilMoisturePanel;
    private javax.swing.JTextField soilMoistureTextField;
    private com.toedter.calendar.JDateChooser startDateChooser;
    private javax.swing.JLabel startDateLabel;
    private javax.swing.JLabel startTimeHrLabel;
    private javax.swing.JSpinner startTimeHrSpinner;
    private javax.swing.JLabel startTimeLabel;
    private javax.swing.JLabel startTimeMinLabel;
    private javax.swing.JSpinner startTimeMinSpinner;
    private javax.swing.JSpinner startTimePeriodSpinner;
    private javax.swing.JPanel summaryPanel;
    private javax.swing.JButton tempCSVButton;
    private javax.swing.JPanel tempChartPanel;
    private javax.swing.JPanel tempDataPanel;
    private javax.swing.JButton tempDataShowButton;
    private javax.swing.JButton tempJPEGButton;
    private javax.swing.JLabel tempLabel;
    private javax.swing.JTextField tempTextField;
    private javax.swing.JLabel titleLabel;
    private javax.swing.JLabel typeLabel;
    private javax.swing.JMenuItem updateGreenhouseMenuItem;
    private javax.swing.JMenuItem updateNodeControllerMenuItem;
    private javax.swing.JMenuItem updatePasswordMenuItem;
    public static javax.swing.JLabel userLabel;
    private javax.swing.JButton viewDataButton;
    // End of variables declaration//GEN-END:variables

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            Data d = DataDAO.getSummarizedDataForGH((String) ghSummaryComboBox.getSelectedItem());
            int temp = Integer.parseInt(d.getTemperature());
            if (temp > 35) {
                String command = "Q;192.169.1.7;1111;P";
                System.out.println("Command : " + command);

                InputStream in = new ByteArrayInputStream(command.getBytes(StandardCharsets.UTF_8));
                SerialPort port = SerialPortConnection.getConnection();
                SerialWriter serialWriter = new SerialWriter(port.getOutputStream());
                serialWriter.setInputStream(in);
                serialWriter.run();
            }
            tempTextField.setText(d.getTemperature());
            lightTextField.setText(d.getLight());
            pressureTextField.setText(d.getPressure());
            humidityTextField.setText(d.getHumidity());
            phTextField.setText(d.getPh());
            soilMoistureTextField.setText(d.getSoilMoisture());

            tempTextField.setBackground(getTexFiledBackground(Integer.parseInt(d.getTemperature())));
            lightTextField.setBackground(getTexFiledBackground(Integer.parseInt(d.getLight())));
            pressureTextField.setBackground(getTexFiledBackground(Integer.parseInt(d.getPressure())));
            humidityTextField.setBackground(getTexFiledBackground(Integer.parseInt(d.getHumidity())));
            phTextField.setBackground(getTexFiledBackground(Integer.parseInt(d.getPh())));
            soilMoistureTextField.setBackground(getTexFiledBackground(Integer.parseInt(d.getSoilMoisture())));
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(MainForm.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
