/*=============================================================================
 |   Assignment:  Project 2 Database - Milestone 3
 |       Author:  [Zengxiaoran Kang (zk2487@tamu.edu)]
 |                [Yifei Liang (yifei.liang@tamu.edu)]
 |                [Taowei Ji (davidtaoweiji@tamu.edu)]
                  [Yuqi Sun (ysun102@tamu.edu)]
 |       Course:  CSCE 315 - 915
 |   Instructor:  Yoonsuck Choe
 |
 |  Description:  GUI display
 |
 |     Language:  Java,
 | Ex. Packages:  Graphviz...
 |                Xchart...
 |
 | Deficiencies:  https://stackoverflow.com/questions/42864580/how-to-not-close-whole-jvm-when-closing-xchart-chart
 |                when closing the xchart, the whole program exits. 
 |                
 *===========================================================================*/

// Resource acknoldgement:
// https://stackoverflow.com/questions/1879091/jsplitpane-setdividerlocation-problem
// https://docs.oracle.com/javase/tutorial/uiswing/components/tabbedpane.html
// https://stackoverflow.com/questions/5621338/how-to-add-jtable-in-jpanel-with-null-layout#:~:text=You%20can%20make%20use%20of,To%20add%20JTable%20to%20JPanel.&text=JPanel%20panel%20%3D%20new%20JPanel()%3B,add(scrollPane%2C%20BorderLayout.
// https://stackoverflow.com/questions/20021139/converting-resultset-to-multidimensional-string-array
// https://stackoverflow.com/questions/24634047/closeable-jtabbedpane-alignment-of-the-close-button
// https://stackoverflow.com/questions/42864580/how-to-not-close-whole-jvm-when-closing-xchart-chart

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.plaf.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.border.TitledBorder;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.GridBagConstraints;

import java.sql.*;
import java.util.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors; 
import java.util.stream.Stream; 
import org.knowm.xchart.*;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.markers.SeriesMarkers;
import org.knowm.xchart.demo.charts.ExampleChart;
import org.knowm.xchart.demo.charts.date.DateChart04;
import org.knowm.xchart.demo.charts.bar.BarChart05;
import org.knowm.xchart.PieSeries.PieSeriesRenderStyle;
import org.knowm.xchart.style.PieStyler.AnnotationType;
import java.awt.Color;
import java.util.Random;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
//all the import package

class Gui implements ActionListener{ //overload the action, to link the phase two
  public JFrame frame;
  public JPanel main_panel;
  public JPanel top_panel;
  public JTabbedPane center_panel;
  public JPanel bot_panel;
  public JButton sendtael = new JButton("Send");
  public JButton reset = new JButton("Reset");
  public JLabel tabellabel1 = new JLabel();
  public JTextField tf1 = new JTextField(10);
  public JTextField tf2 = new JTextField(10);
  public JTextField tf3 = new JTextField(10);
  public String command;
  public JDBC jdbc;
  public LinkedList<String> allTableNames;
  //declare all the global variable
  //including some of the key components for the GUI


  //when return from jdbc, all outcomes are result set, thus needed to be converted to wanted types,(linklist in our case)
  private static LinkedList<String> resultSetToLinkList(ResultSet rs, String tableName) throws SQLException{
    LinkedList<String> returnList = new LinkedList<>();
    while(rs.next()){
     returnList.add(rs.getString(tableName));
   }
   return returnList;
 }
  public static int sizeOfResultSet(ResultSet rs)throws SQLException{
    rs.last();
    int size = rs.getRow();
    rs.beforeFirst();
    return size;
  }

  public Gui() throws SQLException{
    jdbc = new JDBC();
    jdbc.Connect();
    frame = new JFrame("MySQL Query");
    allTableNames = jdbc.showTables();
    main_panel = new JPanel(new BorderLayout(5,5));
    top_panel = new JPanel( new FlowLayout(FlowLayout.LEADING, 3,3));
    bot_panel = new JPanel( new FlowLayout(FlowLayout.RIGHT, 3,3));
    center_panel = new JTabbedPane();
    initMainPanel();
    initTopPanel();
    initCenterPanel();
    initBotPanel();
    initFrame();
  }

  // function that initialize the main panel
  public void initCenterPanel() throws SQLException{
    main_panel.add(new JLayer<JTabbedPane>(center_panel, new CloseableTabbedPaneLayerUI()),BorderLayout.CENTER );
    JPanel imagePanel = new JPanel(new GridBagLayout());
    ImageIcon icon = new ImageIcon("1.png");
    JLabel label = new JLabel(icon);
    imagePanel.add(label);
    center_panel.addTab("1.png",label);
    
  } // end center panel


  //initialize the top panel, which includes the combobox that contains all the tasking function
  public void initTopPanel(){
      top_panel.setPreferredSize(new Dimension(400, 100));
      // An array that contains all the tasking function
      String[] languages = new String[] {"","jdb-show-related-tables <table-name>", "jdb-show-all-primary-keys", "jdb-find-column <column-name>", "jdb-search-path <table1> <table2>"
      , "jdb-search-and-join <table1> <table2>","jdb-get-view <view-name> '(' < sql query > ')'", "jdb-stat <table> (or <view-name>) <column_name>", "jdb-slice <tableName> <index1> <index2>",
      "jdb-show-head <table name> <number of rows print out>",  "jdb-product-to-where <city>", "jdb-info-of-subtotalitem <the first number of the ordering>", "jdb-delete-view <viewName>", "jdb-see-views","jdb-show-tables","jdb-draw",
      "jdb-join-table","jdb-show-columns-of-a-table","custome sql command","Analytical Dashboard"};

      //insert a combobox to make the dtop down box possible
      JComboBox cb=new JComboBox(languages);
      top_panel.add(cb);
      JPanel textpanel = new JPanel();

      //overload the action to make it interactive
      cb.addActionListener(new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent event) {
          JComboBox<String> combo = (JComboBox<String>) event.getSource();
          String selectedfunction = (String) combo.getSelectedItem();

          //below are all the tasking branches, different Jtextfield , Jbutton need to be adressed
          //the layout is really similar
          //have to remove all each time to update the new view for each branch, and validate after all the update
          // the command is used to store for overloading the action the comes from sned button


          if (selectedfunction.equals("jdb-show-related-tables <table-name>")) //performing show related table
          {
                      
            tf1.setColumns(10);
            command = "jdb-show-related-tables ";
            textpanel.removeAll();
            tabellabel1.setText("table name");
            
            textpanel.add(tabellabel1);
            textpanel.add(tf1);
            textpanel.add(sendtael);
            textpanel.add(reset);

            top_panel.add(BorderLayout.NORTH, textpanel);
            frame.validate();
          }
          else if (selectedfunction.equals("jdb-show-all-primary-keys")) // performing show all primary keys
          {
            textpanel.removeAll();

            command = "jdb-show-all-primary-keys";
            textpanel.add(sendtael);
            top_panel.add(BorderLayout.NORTH, textpanel);
            frame.validate();
            System.out.println("Show all primary keys");
          }
          else if (selectedfunction.equals("jdb-find-column <column-name>"))// performing find column
          {
            textpanel.removeAll();
            tabellabel1.setText("column name");
            tf1.setColumns(10);
            command = "jdb-find-column ";
            textpanel.add(tabellabel1);
            textpanel.add(tf1);
            textpanel.add(sendtael);
            textpanel.add(reset);
            top_panel.add(BorderLayout.NORTH, textpanel);
            frame.validate();
          }
          else if (selectedfunction.equals("jdb-search-path <table1> <table2>"))// performing search path
          {
            textpanel.removeAll();
            tabellabel1.setText("table 1");
            JLabel tabellabel2 = new JLabel("table2");
            tf1.setColumns(10);
            command = "jdb-search-path ";
            textpanel.add(tabellabel1);
            textpanel.add(tf1);
            textpanel.add(tabellabel2);
            textpanel.add(tf2);
            textpanel.add(sendtael);
            textpanel.add(reset);
            top_panel.add(BorderLayout.NORTH, textpanel);
            frame.validate();
          }
          else if (selectedfunction.equals("jdb-search-and-join <table1> <table2>"))// performing seach and join
          {
            textpanel.removeAll();
            tabellabel1.setText("table 1");
            JLabel tabellabel2 = new JLabel("table2");
            tf1.setColumns(10);
            command = "jdb-search-and-join ";
            textpanel.add(tabellabel1);
            textpanel.add(tf1);
            textpanel.add(tabellabel2);
            textpanel.add(tf2);
            textpanel.add(sendtael);
            textpanel.add(reset);
            top_panel.add(BorderLayout.NORTH, textpanel);
            frame.validate();
          }

          //preforming get view, more instruction are given in the readme
          else if(selectedfunction.equals("jdb-get-view <view-name> '(' < sql query > ')'")) 
          {
            textpanel.removeAll();
            tabellabel1.setText("view name");
            JLabel tabellabel2 = new JLabel("< sql query >");
            tf2.setColumns(30);
            tf1.setColumns(10);
            command = "jdb-get-view ";
            textpanel.add(tabellabel1);
            textpanel.add(tf1);
            textpanel.add(tabellabel2);
            textpanel.add(tf2);
            textpanel.add(sendtael);
            textpanel.add(reset);
            top_panel.add(BorderLayout.NORTH, textpanel);
            frame.validate();
          }
          else if(selectedfunction.equals("jdb-stat <table> (or <view-name>) <column_name>"))// performing showing the stat of a view
          {
            textpanel.removeAll();
                      //textpanel = new JPanel();
            tabellabel1.setText("table or view name");
            tf1.setColumns(10);
            tf2.setColumns(10);
            command = "jdb-stat ";

            JLabel tabellabel2 = new JLabel("column_name");
            textpanel.add(tabellabel1);
            textpanel.add(tf1);
            textpanel.add(tabellabel2);
            textpanel.add(tf2);
            textpanel.add(sendtael);
            textpanel.add(reset);
            top_panel.add(BorderLayout.NORTH, textpanel);
            frame.validate();
          }
          // below are some custom commands that we decide to implements
          else if(selectedfunction.equals("jdb-slice <tableName> <index1> <index2>"))// performing the slicing of a table
          {
            textpanel.removeAll();
            tabellabel1.setText("table Name");
            JLabel tabellabel2 = new JLabel("index1");
            JLabel tabellabel3 = new JLabel("index2");
            tf1.setColumns(10);
            tf2.setColumns(10);
            command = "jdb-slice ";
            textpanel.add(tabellabel1);
            textpanel.add(tf1);
            textpanel.add(tabellabel2);
            textpanel.add(tf2);
            textpanel.add(tabellabel3);
            textpanel.add(tf3);
            textpanel.add(sendtael);
            textpanel.add(reset);
            top_panel.add(BorderLayout.NORTH, textpanel);
            frame.validate();
          }
          else if(selectedfunction.equals("jdb-show-head <table name> <number of rows print out>"))// performing showing the heading rows of a table
          {
            textpanel.removeAll();
                      //textpanel = new JPanel();
            tabellabel1.setText("table Name");

                      //JTextField tf1 = new JTextField(10);
            JLabel tabellabel2 = new JLabel("number of rows");

            tf1.setColumns(10);
            tf2.setColumns(10);
            command = "jdb-show-head ";

            textpanel.add(tabellabel1);
            textpanel.add(tf1);
            textpanel.add(tabellabel2);
            textpanel.add(tf2);
            textpanel.add(sendtael);
            textpanel.add(reset);
            top_panel.add(BorderLayout.NORTH, textpanel);
            frame.validate();
          }
          else if(selectedfunction.equals("jdb-product-to-where <city>"))// performing how many prodcuts selled to what city
          {
            textpanel.removeAll();
            tabellabel1.setText("City");
            tf1.setColumns(10);
            command = "jdb-product-to-where ";
            textpanel.add(tabellabel1);
            textpanel.add(tf1);
            textpanel.add(sendtael);
            textpanel.add(reset);
            top_panel.add(BorderLayout.NORTH, textpanel);
            frame.validate();
          }
          else if (selectedfunction.equals("jdb-info-of-subtotalitem <the first number of the ordering>"))// performing showing the first nuber of ordering
          {
            textpanel.removeAll();
            tabellabel1.setText("the first number of the ordering");
            tf1.setColumns(10);
            command = "jdb-info-of-subtotalitem ";
            textpanel.add(tabellabel1);
            textpanel.add(tf1);
            textpanel.add(sendtael);
            textpanel.add(reset);
            top_panel.add(BorderLayout.NORTH, textpanel);
            frame.validate();
          }
          else if (selectedfunction.equals("jdb-delete-view <viewName>"))// performing deletion fot the view that was created earlier, leave the database un-modified
          {
            textpanel.removeAll();
            tabellabel1.setText("view name");
            tf1.setColumns(10);
            command = "jdb-delete-view ";
            textpanel.add(tabellabel1);
            textpanel.add(tf1);
            textpanel.add(sendtael);
            textpanel.add(reset);
            top_panel.add(BorderLayout.NORTH, textpanel);

            frame.validate();
          }
          else if (selectedfunction.equals("jdb-see-views"))// showing the view that is possibly created earlier, or last time
          {
            textpanel.removeAll();
            command = "jdb-see-views ";
            textpanel.add(sendtael);
            top_panel.add(BorderLayout.NORTH, textpanel);
            frame.validate();
          }
          else if(selectedfunction.equals("custome sql command"))// let user input their own custom commands, which will be send to JDBC and execute
          {
            textpanel.removeAll();
            tabellabel1.setText("sql");
            tf1.setColumns(30);
            command = "";
            textpanel.add(tabellabel1);
            textpanel.add(tf1);
            textpanel.add(sendtael);
            textpanel.add(reset);
            top_panel.add(BorderLayout.NORTH, textpanel);
            frame.validate();
          }
          else if (selectedfunction.equals("jdb-show-tables")) //performing showing of the tables
          {
            textpanel.removeAll();
            command = "jdb-show-tables";
            textpanel.add(sendtael);
            top_panel.add(BorderLayout.NORTH, textpanel);
            frame.validate();

          } 
          else if(selectedfunction.equals("jdb-draw")){ //performing the graphifz, atually drawed and stored
            textpanel.removeAll();
            command = "jdb-draw";
            textpanel.add(sendtael);
            top_panel.add(BorderLayout.NORTH, textpanel);
            frame.validate();
          } 
          else if(selectedfunction.equals("jdb-show-columns-of-a-table")){ // showing the wanted columns of a table
            tabellabel1.setText("table name");
            JLabel tabellabel2 = new JLabel("columns name, enter * for all, or column names seperated by commas");
            tf2.setColumns(30);
            textpanel.removeAll();
            command = "jdb-show-columns-of-a-table";
            textpanel.add(tabellabel1);
            textpanel.add(tf1);
            textpanel.add(tabellabel2);
            textpanel.add(tf2);
            textpanel.add(sendtael);
            textpanel.add(reset);
            top_panel.add(BorderLayout.NORTH, textpanel);
            frame.validate();
          } 
          else if(selectedfunction.equals("jdb-join-table")){// performing the join of couple table
            JLabel tabellabel = new JLabel("table names seperated by commas");
            tf1.setColumns(30);
            textpanel.removeAll();
            command = "jdb-join-table";
            textpanel.add(tabellabel);
            textpanel.add(tf1);
            textpanel.add(sendtael);
            textpanel.add(reset);
            top_panel.add(BorderLayout.NORTH, textpanel);
            frame.validate();
          }
          else if(selectedfunction.equals("Analytical Dashboard")){// data graph 
            textpanel.removeAll();
            //top_panel.remove(textpanel);
            command = "Dashboard-analysis";
            textpanel.add(sendtael);
            top_panel.add(BorderLayout.NORTH, textpanel);
            frame.validate();
          }

        }
      });

        // add the action perceived here
        sendtael.addActionListener(this);
        reset.addActionListener(this);
        //set the outfir of the top panel
        top_panel.setBorder(new TitledBorder("Instruction Menu") );
        main_panel.add(top_panel, BorderLayout.PAGE_START);

    } // end top panel


    // the main overloading function that linked all the button to JDBC to perfrom the desired task
    public void actionPerformed(java.awt.event.ActionEvent evt){
      Object src = evt.getSource();
      //if the action resource is from the send button
      if(src == sendtael){

        //access all the possible parameter
        String parameter1 = tf1.getText().trim();
        String parameter2 = tf2.getText().trim();
        String parameter3 = tf3.getText().trim();
        String sql = command;
        System.out.println(sql);

        // implement a try catch, if it task the achieveable, do it
        // it it is not, then a error window will pop up


      try{

      if(sql.equals("quit;"))
      { // Quit
        System.exit(0);
      }

      if(sql.contains("Dashboard-analysis")){ 
        //System.out.println("Drawing the dashborad");
        Dashboard dashborad = new Dashboard();
        ResultSet rs = jdbc.getSaleByYear();
        String[][] salebyyear = resultSetTo2DArray(rs);
        //double[] doubleArray = Arrays.stream(stringArray).mapToDouble(Double::parseDouble).toArray();
        String[] yearsString = new String[salebyyear.length];
        String[] saleyearSumString = new String[salebyyear.length];
        for(int i = 0; i < salebyyear.length; i++)
        {
          yearsString[i] = salebyyear[i][0];
          saleyearSumString[i] = salebyyear[i][1];
        }
        double[] years = Arrays.stream(yearsString).mapToDouble(Double::parseDouble).toArray();
        double[] saleyearSum = Arrays.stream(saleyearSumString).mapToDouble(Double::parseDouble).toArray();

        XYChart yearSaleGraph = dashborad.dateTimeGraph(years,saleyearSum,"year vs sale");

//-----------------------------------------------------------------------------------

        rs = jdbc.getEmployeeAgeDistribution();
        String[][] employage = resultSetTo2DArray(rs);
        String[] emplyeCountString = new String[employage.length];
        String[] emplyeeRange = new String[employage.length];
        for(int i = 0; i < employage.length; i++)
        {
          emplyeCountString[i] = employage[i][0];
          emplyeeRange[i] = employage[i][1];
        }
        double[] emplyeCount = Arrays.stream(emplyeCountString).mapToDouble(Double::parseDouble).toArray();

        PieChart employAgeGraph = dashborad.topTen(emplyeeRange, emplyeCount, "Employee Age Distribution");
//-----------------------------------------------------------------------------------

        rs = jdbc.getEmployeeMarryStatus();
        String[][] employMarry = resultSetTo2DArray(rs);
        String[] employMarryNumberString = new String[employMarry.length];
        String[] employMarryState = new String[employMarry.length];
        for(int i = 0; i < employMarry.length; i++)
        {
          employMarryState[i] = employMarry[i][0];
          employMarryNumberString[i] = employMarry[i][1];
        }

        double[] employMarryNumber = Arrays.stream(employMarryNumberString).mapToDouble(Double::parseDouble).toArray();

        CategoryChart emplymarryGraph = dashborad.histChart( employMarryState,  employMarryNumber,"Employee Marry Status");
//-----------------------------------------------------------------------------------

        rs = jdbc.getEmployeeGenderDistribution();
        String[][] employGender = resultSetTo2DArray(rs);
        String[] employGenderNumberString = new String[employGender.length];
        String[] employGenderState = new String[employGender.length];
        for(int i = 0; i < employGender.length; i++)
        {
          employGenderState[i] = employGender[i][0];
          employGenderNumberString[i] = employGender[i][1];
        }

        double[] employGenderNumber = Arrays.stream(employGenderNumberString).mapToDouble(Double::parseDouble).toArray();

        CategoryChart emplyGenderGraph = dashborad.histChart( employGenderState,  employGenderNumber,"EmployeeGenderDistribution");
//-----------------------------------------------------------------------------------

        rs = jdbc.getSaleByCountry();
        String[][] SaleByCountry = resultSetTo2DArray(rs);
        String[] countrys = new String[6];
        String[] year = new String[4];
        String[] country1 = new String[4];
        String[] country2 = new String[4];
        String[] country3 = new String[4];
        String[] country4 = new String[4];
        String[] country5 = new String[4];
        String[] country6 = new String[4];
        for(int i = 0; i <6; i++ )
          countrys[i] = SaleByCountry[i*4][0];

        for(int i = 0; i < SaleByCountry.length; i++)
        {
          if(SaleByCountry[i][0].equals(countrys[0]))
          {
            year[i] = SaleByCountry[i][1];
            country1[i] = SaleByCountry[i][2];
            //System.out.println(country1[i]);
          }
          if(SaleByCountry[i][0].equals(countrys[1]))
          {
            country2[i%4] = SaleByCountry[i][2];
          }
          if(SaleByCountry[i][0].equals(countrys[2]))
          {
            country3[i%4] = SaleByCountry[i][2];
          }
          if(SaleByCountry[i][0].equals(countrys[3]))
          {
            country4[i%4] = SaleByCountry[i][2];
          }
          if(SaleByCountry[i][0].equals(countrys[4]))
          {
            country5[i%4] = SaleByCountry[i][2];
          }
          if(SaleByCountry[i][0].equals(countrys[5]))
          {
            country6[i%4] = SaleByCountry[i][2];
          }
        }
        double[] numcountry1 = Arrays.stream(country1).mapToDouble(Double::parseDouble).toArray();
        double[] numcountry2 = Arrays.stream(country2).mapToDouble(Double::parseDouble).toArray();
        double[] numcountry3 = Arrays.stream(country3).mapToDouble(Double::parseDouble).toArray();
        double[] numcountry4 = Arrays.stream(country4).mapToDouble(Double::parseDouble).toArray();
        double[] numcountry5 = Arrays.stream(country5).mapToDouble(Double::parseDouble).toArray();
        double[] numcountry6 = Arrays.stream(country6).mapToDouble(Double::parseDouble).toArray();


        CategoryChart countrySaleGraph = dashborad.histChart(year,numcountry1, numcountry2,numcountry3,numcountry4,numcountry5,numcountry6, "Sale by country") ;

//-----------------------------------------------------------------------------------
        rs = jdbc.getCustomerByCountry();
        String[][] customercountry = resultSetTo2DArray(rs);
        String[] customercountryString = new String[customercountry.length];
        String[] country = new String[customercountry.length];
        for(int i = 0; i < customercountry.length; i++)
        {
          country[i] = customercountry[i][0];
          customercountryString[i] = customercountry[i][1];
        }

        double[] customercountrynum = Arrays.stream(customercountryString).mapToDouble(Double::parseDouble).toArray();

        PieChart regionGraph = dashborad.getRegionPiechart( country,  customercountrynum);

//-----------------------------------------------------------------------------------

        List<Chart> charts = new ArrayList<Chart>();
        charts.add(yearSaleGraph);
        charts.add(employAgeGraph);
        charts.add(emplymarryGraph);
        charts.add(emplyGenderGraph);
        charts.add(countrySaleGraph);
        charts.add(regionGraph);
        JFrame xframe = new SwingWrapper<Chart>(charts).displayChartMatrix();
        xframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        xframe.pack();
        xframe.setVisible(true);
        //new SwingWrapper<Chart>(charts).displayChartMatrix();


      }
      else if(sql.contains("jdb")){  // jdb commands

        sql = sql.replace(";","");
        sql = sql.trim();
        if(sql.contains("jdb-show-related-tables")){ // performing the action

          String tablename = parameter1;
          tablename = tablename.trim();
          if(allTableNames.contains(tablename)){// if the table name is valid

            //do the task here
            ResultSet rs = jdbc.showRelatedTables(tablename);
            DBTablePrinter.printResultSet(rs);
            rs = jdbc.showRelatedTables(tablename);
            String[][] data = resultSetTo2DArray(rs);
            String[] header =  resultSetToHeader(rs);
            // add the resulting panel
            addTabPanel(header, data, "jdb-show-related-tables");

          } else { // possible a invalid table name is given, error window will pop up
            JOptionPane.showMessageDialog(null,"Invalid tablename","Error!",JOptionPane.INFORMATION_MESSAGE);
          }
        } else if(sql.contains("jdb-show-all-primary-keys")){ // performing the action

          String[][] ls =  jdbc.showAllPrimaryKeys();// the return type is 2d array
          for(int i = 0 ; i < ls.length;i++){
            String arr[] = ls[i];
            System.out.println("("+arr[0].trim()+","+arr[1].trim()+")");
          }
          String header[] = new String[]{"Table Name", "Primary keys"};
          // add the resulting panel
          addTabPanel(header,ls, "jdb-show-all-primary-keys");

        } else if (sql.contains("jdb-find-column")){ //3 performing the action

         String columnName = parameter1;
         columnName = columnName.trim();
         //do the task here
         ResultSet rs = jdbc.findColumn(columnName);

         if(sizeOfResultSet(rs) == 0){// if no tbale has the column, error window will pop up
          JOptionPane.showMessageDialog(null,"No table have this column","Error!",JOptionPane.INFORMATION_MESSAGE);
        } else {
          // everything went through, output the result
          DBTablePrinter.printResultSet(rs);
          rs = jdbc.findColumn(columnName);
          String[][] data = resultSetTo2DArray(rs);
          String[] header =  resultSetToHeader(rs);
          //adding the resulting panel
          addTabPanel(header, data, "jdb-find-column");

        }
        } else if(sql.contains("jdb-search-path")){//4 performing the task

        //access the table name from the parameter
         String table1 = parameter1;
         String table2 = parameter2;
         if(!allTableNames.contains(table1) || !allTableNames.contains(table2)){// in any of those table anme does not exist, error window will pop up
          JOptionPane.showMessageDialog(null,"Invalid tablename","Error!",JOptionPane.INFORMATION_MESSAGE);
        } else {
          //do the job here
          String[] header = new String[]{"path"};
          LinkedList<LinkedList<String>> ls = jdbc.searchPath(table1,table2);
          String[][] data = new String[ls.get(0).size()][1];
          //prin to the terminal
          for(LinkedList<String> list : ls){
           System.out.println(list);
         }
         int i = 0;
         for(String str:ls.get(0))
         {
            data[i][0] = str;
            i++;
         }
         // adding the reslting panel
         addTabPanel(header, data, "jdb-search-path");


       }
     } else if (sql.contains("jdb-search-and-join")){//5 performing the task
      //access the table name from the parameter
       String table1 = parameter1;
       String table2 = parameter2;
       if(!allTableNames.contains(table1) || !allTableNames.contains(table2)){// if any of those two table names does not exit
        JOptionPane.showMessageDialog(null,"Invalid tablename","Error!",JOptionPane.INFORMATION_MESSAGE);
      } else {
        //do the job here
        DBTablePrinter.printResultSet(jdbc.searchPathAndJoin(table1,table2));
        ResultSet rs = jdbc.searchPathAndJoin(table1,table2);
        String[][] data = resultSetTo2DArray(rs);
        String[] header =  resultSetToHeader(rs);
        //adding the result panel
        addTabPanel(header, data, "jdb-search-and-join");
      }
    } else if (sql.contains("jdb-get-view")){//6 performing the task

    //access the view name
     String viewName = parameter1;
     try{// try and catch
     jdbc.getView(viewName, parameter2);
   }catch(SQLException se){ // the this veiw does not exist in the database and error window will show up
        JOptionPane.showMessageDialog(null,se,"Error!",JOptionPane.INFORMATION_MESSAGE);

      }
       //need regex
    } else if(sql.contains("jdb-stat")) { // 7 performing the task
     LinkedList<String> allViewsName = resultSetToLinkList(jdbc.getAllView(),"Tables_in_adventureworks");
     String[] arr = sql.split(" ");
     String tableOrView = parameter1;
     String columnName = parameter2;
     //accessing the table anme and column name
     if(allTableNames.contains(tableOrView) || allViewsName.contains(tableOrView)){ // this will only went through if both inputs are valid
      Stats stats = jdbc.getStats(tableOrView,columnName);
      stats.displayHistogram();
      String[][] a = resultSetTo2DArray(stats.rs);
      String[] header =  resultSetToHeader(stats.rs);
      String[][] b = {{"min,max",Float. toString(stats.Min), Float. toString(stats.Max)},{"mean,median",Float. toString(stats.Mean), Float. toString(stats.Median)}};
      String[][] data = new String[a.length + b.length][];
      System.arraycopy(a, 0, data, 0, a.length);
      System.arraycopy(b, 0, data, a.length, b.length);
      //add the resulting panel
      addTabPanel(header, data, "jdb-stat");

    } else { //any of those inputs are invalid, error window will pop up
      JOptionPane.showMessageDialog(null,"Invalid table/view name","Error!",JOptionPane.INFORMATION_MESSAGE);
    }
    } else if(sql.contains("jdb-slice")){ //8 performing the tasks
     String[] arr = sql.split(" ");
     String tableName = parameter1;
     // check if the inputs are valid
     if(parameter2.chars().allMatch(Character::isDigit) && parameter3.chars().allMatch(Character::isDigit) && !parameter3.equals("") && !parameter2.equals("")){
     int from = Integer.parseInt(parameter2);
     int to = Integer.parseInt(parameter3);

     if(!allTableNames.contains(tableName)){// if this tablename does not exist, error window will pop up
      JOptionPane.showMessageDialog(null,"Invalid tablename","Error!",JOptionPane.INFORMATION_MESSAGE);
    }else {
      //do the job here
     DBTablePrinter.printResultSet(jdbc.Slicing(tableName,from,to ));
      ResultSet rs = jdbc.Slicing(tableName,from,to );
      String[][] data = resultSetTo2DArray(rs);
      String[] header =  resultSetToHeader(rs);
      //adding the reuslt panel
      addTabPanel(header, data, "jdb-slice");

   }
      }else{// catch the sql exception that is implemented in jdbc, error window wil pop up
      JOptionPane.showMessageDialog(null,"not an int","Error!",JOptionPane.INFORMATION_MESSAGE);
   }
    // below are the custom commands
     }else if(sql.contains("jdb-show-head")){ //9 oerforming the task
      // jdb-show-head tableName num
       String[] arr = sql.split(" ");
       String tableName = parameter1;
       if(parameter2.chars().allMatch(Character::isDigit) && !parameter2.equals("")){ // if the charater is a number
       int num = Integer.parseInt(parameter2);

       if(!allTableNames.contains(tableName)){ // if the table name is not one of the tables, it return the error message
        JOptionPane.showMessageDialog(null,"Invalid table/view name","Error!",JOptionPane.INFORMATION_MESSAGE);
      }else { // performing task
        DBTablePrinter.printResultSet(jdbc.showHead(tableName,num));
        ResultSet rs = jdbc.showHead(tableName,num);
        String[][] data = resultSetTo2DArray(rs);
        String[] header =  resultSetToHeader(rs);
        addTabPanel(header, data, "jdb-show-head");
      }
    }else{// if the charter is not a number, return error
      JOptionPane.showMessageDialog(null,"not an int","Error!",JOptionPane.INFORMATION_MESSAGE);
    }
    }else if (sql.contains("jdb-product-to-where")){//10 performing task
         // jdb-product-to-where city
   //how many product have been sold to this city
     String[] arr = sql.split(" ");
     String city = parameter1;
     DBTablePrinter.printResultSet(jdbc.NumOfProductToWhere(city));
      ResultSet rs = jdbc.NumOfProductToWhere(city);
      String[][] data = resultSetTo2DArray(rs);
      String[] header =  resultSetToHeader(rs);
      addTabPanel(header, data, "jdb-product-to-where");
     } else if (sql.contains("jdb-info-of-subtotalitem")){ //11 performing task
   //print the first number of product that has most subtotal items, show the total value, and price
       //String[] arr = sql.split(" ");
       if(parameter1.chars().allMatch(Character::isDigit) && !parameter1.equals("")){ // if the input is a number
       int number = Integer.parseInt(parameter1);
       DBTablePrinter.printResultSet(jdbc.InfoOfMostSubtotalItem(number));
       ResultSet rs = jdbc.InfoOfMostSubtotalItem(number);
      String[][] data = resultSetTo2DArray(rs);
      String[] header =  resultSetToHeader(rs);
      addTabPanel(header, data, "jdb-info-of-subtotalitem");
    }else // if it is not a number, return error
    {
      JOptionPane.showMessageDialog(null,"not an int","Error!",JOptionPane.INFORMATION_MESSAGE);
    }

     } else if (sql.contains("jdb-delete-view")){  //12 performing task
         //jdb-delete-view viewName
       LinkedList<String> allViewsName = resultSetToLinkList(jdbc.getAllView(),"Tables_in_adventureworks");
       String[] arr = sql.split(" ");
       String viewName = parameter1;

       if(allViewsName.contains(viewName)){ // if found the viewname
        jdbc.deleteView(viewName);
        System.out.println("Deleted "+ viewName);
      } else { // if it cannot be deleted, return error
        //System.out.println("Invalid table/view name");
        JOptionPane.showMessageDialog(null,"Invalid table/view name","Error!",JOptionPane.INFORMATION_MESSAGE);
      }
    } else if(sql.contains("jdb-see-views")){ // 13 performing task

      ResultSet rs = jdbc.getAllView();
      DBTablePrinter.printResultSet(rs);
      rs = jdbc.getAllView();
      String[][] data = resultSetTo2DArray(rs);
      String[] header =  resultSetToHeader(rs);
      //System.out.println("here");
      addTabPanel(header, data, "jdb-see-views");

    }// continue...*/
    else if (sql.contains("jdb-show-tables")){  //14 performing task
         //jdb-delete-view viewName
      ResultSet rs = jdbc.showTables1();
      DBTablePrinter.printResultSet(rs);
      rs = jdbc.showTables1();
      String[][] data = resultSetTo2DArray(rs);
      String[] header =  resultSetToHeader(rs);
      addTabPanel(header, data, "jdb-show-tables");

    } else if (sql.contains("jdb-draw")){ // 15 performing task
      jdbc.draw();
      addTabPanel("outEX1.png");
    }else if (sql.contains("jdb-show-columns-of-a-table")){  //16 performing task
         //jdb-delete-view viewName
      String tableName = parameter1;
      String columnNames = parameter2;
      ResultSet rs = jdbc.selectMutColumnNames(tableName,columnNames);
      //DBTablePrinter.printResultSet(rs);
      rs = jdbc.selectMutColumnNames(tableName,columnNames);
      String[][] data = resultSetTo2DArray(rs);
      String[] header =  resultSetToHeader(rs);
      addTabPanel(header, data, "jdb-show-columns-of-a-table");

    } else if (sql.contains("jdb-join-table")){  //17 performing task
         //jdb-delete-view viewName
      String tableName = parameter1;
      //DBTablePrinter.printResultSet(rs);
      ResultSet rs = jdbc.joinTables(tableName);
      String[][] data = resultSetTo2DArray(rs);
      String[] header =  resultSetToHeader(rs);
      addTabPanel(header, data, "jdb-join-table");
    }
      // end all jdb commands
  }else { // 18 performing task
      // All MySQL commands
    sql = parameter1;
    ResultSet rs = jdbc.SQLCommand(sql);
    DBTablePrinter.printResultSet(rs);
    rs = jdbc.SQLCommand(sql);
    String[][] data = resultSetTo2DArray(rs);
    String[] header =  resultSetToHeader(rs);
    addTabPanel(header, data, "jdb-see-views");

  }
}catch(SQLException se){ // catch all the sql error execption, returning pop up message
  JOptionPane.showMessageDialog(null,se,"Error!",JOptionPane.INFORMATION_MESSAGE);
      } // end Try-catch

    }
    else if (src == reset){ // reset every thing inside the text pannel
      tf1.setText("");
      tf2.setText("");
      tf3.setText("");
    }


  }

  public void initMainPanel(){// set main screen panel
    main_panel.setBorder( new TitledBorder("Main Screen") );
  }

  public void initBotPanel(){
    final UIManager.LookAndFeelInfo[] plafInfos = UIManager.getInstalledLookAndFeels();
    String[] plafNames = new String[plafInfos.length];
    for (int ii=0; ii<plafInfos.length; ii++) {
      plafNames[ii] = plafInfos[ii].getName();
    }
    final JComboBox plafChooser = new JComboBox(plafNames);
    bot_panel.add(plafChooser);
    plafChooser.addActionListener( new ActionListener(){
      public void actionPerformed(ActionEvent ae) {
        int index = plafChooser.getSelectedIndex();
        try {
          UIManager.setLookAndFeel( plafInfos[index].getClassName() );
          SwingUtilities.updateComponentTreeUI(frame);
        } catch(Exception e) {
          e.printStackTrace();
        }
      }
    } );
    main_panel.add(bot_panel, BorderLayout.PAGE_END);
  }

  public void initFrame(){ // set the main panel
      //Creating the MenuBar and adding components
    JMenuBar mb = new JMenuBar();// top bar
    JMenu m1 = new JMenu("FILE");
    JMenu m2 = new JMenu("Help");
    mb.add(m1);// add to top bar
    mb.add(m2);
    JMenuItem m11 = new JMenuItem("Open");
    JMenuItem m22 = new JMenuItem("Save as");
    m1.add(m11);
    m1.add(m22);
    frame.setJMenuBar(mb);
    frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); // exit close
    frame.setResizable(true);
    frame.setPreferredSize(new Dimension(1200, 1000)); //size of the panel
    frame.setVisible(true);
    frame.setContentPane(main_panel);
    frame.pack();
    frame.setLocationRelativeTo(null);
    try { // 
          // 1.6+
      frame.setLocationByPlatform(true);
      frame.setMinimumSize(frame.getSize());
    } catch(Throwable ignoreAndContinue) {}
    } // end JFrame


    @SuppressWarnings("all")
    public static void main(String args[]) throws SQLException{
      Gui gui = new Gui();
    }//end main

public void addTabPanel(String[] header, String[][] data, String panelname){ // add a tab on the panel
    // ResultSet rs = jdbc.showRelatedTables("customer");
    // String[][] arr = resultSetTo2DArray(rs);
    DefaultTableModel model = new DefaultTableModel(data, header);
    JTable table = new JTable(model);
    try {
          // 1.6+
      table.setAutoCreateRowSorter(true); 
    }
    catch(Exception continuewithNoSort) {
    }
    JScrollPane tableScroll = new JScrollPane(table); // add the new tab to the panel
    Dimension tablePreferred = tableScroll.getPreferredSize();
    tableScroll.setPreferredSize( new Dimension(tablePreferred.width, tablePreferred.height/3) ); // set to size
    tableScroll.setOpaque(false);
    center_panel.addTab(panelname,tableScroll);

  }

  public void addTabPanel(String img){ // add image panel, using pop up window
    Desktop desktop = Desktop.getDesktop();
    try {File file = new File(img);
  desktop.open(file);
            } catch (IOException ex) {
                // no application registered for PDFs
            }

  }


  public static String[][] resultSetTo2DArray(ResultSet rslt){ // put reselt set into a 2d array
    int rowSize = 0;
    int columnSize = 0;
    try { // get size and last and first
      rslt.last();
      rowSize = rslt.getRow();
      rslt.beforeFirst();
    }
    catch(SQLException sq) {
      System.out.println(sq);
    }

    try{

      ResultSetMetaData rsmd = rslt.getMetaData();
      columnSize = rsmd.getColumnCount();
        /////////////////////////////////////
    }catch(SQLException sq)
    {}

    String[][] arr = new String[rowSize][columnSize]; // set the 2d array size 
    //System.out.println(rowSize);
    int i =0;
    try{
      while(rslt.next() && i < rowSize) // get the data
      {
        for(int j=0;j<columnSize;j++){
          arr[i][j] = rslt.getString(j+1);
        }
        i++;
      }
    } catch(SQLException sq){
      System.out.println(sq);
    }
    return arr;
    } // end resultSetTo2DArray

    public static String[] resultSetToHeader(ResultSet rslt)throws SQLException{ // get header array
      ResultSetMetaData rsMetaData = rslt.getMetaData();
      int numberOfColumns = rsMetaData.getColumnCount();
      String[] header = new String[numberOfColumns]; // get header
      // get the column names; column indexes start from 1
      for (int i = 0; i < numberOfColumns; i++) {
        header[i] = rsMetaData.getColumnName(i+1);
      }
      /* Print Check
      for(String p : header){
        System.out.print(p+" ");
      }*/
      return header;
  } // end resultSetToHeader

}// end GUI

// https://stackoverflow.com/questions/24634047/closeable-jtabbedpane-alignment-of-the-close-button
class CloseableTabbedPaneLayerUI extends LayerUI<JTabbedPane> { // add the x to close a panel. 
  private final JPanel p = new JPanel();
  private final Point pt = new Point(-100, -100);
  private final JButton button = new JButton("x") {
    @Override public Dimension getPreferredSize() {
      return new Dimension(16, 16);
    }
  };
  public CloseableTabbedPaneLayerUI() { // set button
    super();
    button.setBorder(BorderFactory.createEmptyBorder());
    button.setFocusPainted(false);
    button.setBorderPainted(false);
    button.setContentAreaFilled(false);
    button.setRolloverEnabled(false);
  }
  @Override public void paint(Graphics g, JComponent c) { // paint the x on the penel tab
    super.paint(g, c);
    if (c instanceof JLayer) {
      JLayer jlayer = (JLayer) c;
      JTabbedPane tabPane = (JTabbedPane) jlayer.getView();
      for (int i = 0; i < tabPane.getTabCount(); i++) {
        Rectangle rect = tabPane.getBoundsAt(i);
        Dimension d = button.getPreferredSize();
        int x = rect.x + rect.width - d.width - 2;
        int y = rect.y + (rect.height - d.height) / 2;
        Rectangle r = new Rectangle(x, y, d.width, d.height);
        button.setForeground(r.contains(pt) ? Color.RED : Color.BLACK); // set color to red
        SwingUtilities.paintComponent(g, button, p, r);
      }
    }
  }
  @Override public void installUI(JComponent c) {
    super.installUI(c);
    ((JLayer)c).setLayerEventMask(AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK); // mouse action
  }
  @Override public void uninstallUI(JComponent c) {
    ((JLayer)c).setLayerEventMask(0);
    super.uninstallUI(c);
  }
  @Override protected void processMouseEvent(MouseEvent e, JLayer<? extends JTabbedPane> l) {
    if (e.getID() == MouseEvent.MOUSE_CLICKED) { // if the mouse clicked
      pt.setLocation(e.getPoint()); //find the location
      JTabbedPane tabbedPane = (JTabbedPane) l.getView();
      int index = tabbedPane.indexAtLocation(pt.x, pt.y);
      if (index >= 0) { 
        Rectangle rect = tabbedPane.getBoundsAt(index);
        Dimension d = button.getPreferredSize();
        int x = rect.x + rect.width - d.width - 2;
        int y = rect.y + (rect.height - d.height) / 2;
        Rectangle r = new Rectangle(x, y, d.width, d.height);
        if (r.contains(pt)) { // if it is at the right location, delete that pabel
          tabbedPane.removeTabAt(index);
        }
      }
      l.getView().repaint();
    }
  }
  @Override protected void processMouseMotionEvent(MouseEvent e, JLayer<? extends JTabbedPane> l) { // mouse pressed. 
    pt.setLocation(e.getPoint());
    JTabbedPane tabbedPane = (JTabbedPane) l.getView();
    int index = tabbedPane.indexAtLocation(pt.x, pt.y);
    if (index >= 0) {
      tabbedPane.repaint(tabbedPane.getBoundsAt(index));
    } else {
      tabbedPane.repaint();
    }
  }
} //CloseableTabbedPaneLayerUI
