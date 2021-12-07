/*=============================================================================
 |   Assignment:  Project 2 Database - Milestone 2
 |       Author:  [Zengxiaoran Kang (zk2487@tamu.edu)]
 |                [Yifei Liang (yifei.liang@tamu.edu)]
 |                [Taowei Ji (davidtaoweiji@tamu.edu)]
                  [Yuqi Sun (ysun102@tamu.edu)]
                  

 |                 ...
 |       Course:  CSCE 315 - 915
 |   Instructor:  Yoonsuck Choe
 |
 |  Description:  JDBC-based client setup
 |
 |     Language:  Java, MySQL
 | Ex. Packages:  Graph.Java - ...
 |                DBTablePrinter - ...
 |
 | Deficiencies:  [If you know of any problems with the code, provide
 |                details here, otherwise clearly state that you know
 |                of no unsatisfied requirements and no logic errors.]
 *===========================================================================*/

 import java.sql.*;
 import java.util.*;
 import java.io.File;
 import java.io.FileOutputStream;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Random;

 import org.enoir.graphvizapi.*;

 public class JDBC {
   // JDBC driver name and database URL
   final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
   final String DB_URL = "jdbc:mysql://localhost:3306";
   private String tmpPath = ".";
   //  Database credentials
   public String USER;
   public String PASS;
   LinkedList<String> allTableNames;
   Connection conn;
   Statement stmt;
   public JDBC() throws SQLException{
     USER = "root";
     PASS = "password";
     conn = null;
     stmt = null;
   }

  /**
  * Connect to MySQL Server
  * @param  conn established MySQL connection
  * @param  stmt executive MySQL statement
  *
  */
  public void Connect() throws SQLException {
    try{
      //STEP 2: Register JDBC driver
      Class.forName("com.mysql.jdbc.Driver");
      //STEP 3: Open a connection
      System.out.println("Connecting to database...");
      conn = DriverManager.getConnection(DB_URL,USER,PASS);
      //STEP 4: Execute a query & import adventureworks
      System.out.println("Creating statement...");
      stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
      String sql;
      sql = "USE adventureworks;";
      ResultSet rs;
      rs = stmt.executeQuery(sql);
    }catch(SQLException se){
        //Handle errors for JDBC
      se.printStackTrace();
    }catch(Exception e){
        //Handle errors for Class.forName
      e.printStackTrace();
    }
    allTableNames = showTables();
  }//end Connect

  /**
   * Close MySQL Connection
   * @param  conn established MySQL connection
   * @param  stmt executive MySQL statement
   */
  public void Close() throws SQLException{
      //block used to close resources
    try{
      if(stmt!=null)
        stmt.close();
      }catch(SQLException se2){}// nothing we can do
      try{
        if(conn!=null)
          conn.close();
      }catch(SQLException se){
        se.printStackTrace();
      }//end try
    } // end Close


  /**
   * list all tables in MySQL database
   *
   * @return A LinkedList of related Table Name
   */
  public LinkedList<String> showTables() throws SQLException{
   String sql = "SHOW tables";
   LinkedList<String> tables = new LinkedList<>();
   ResultSet rs = stmt.executeQuery(sql);
   while(rs.next()){
    tables.add(rs.getString("Tables_in_adventureworks"));
  }
  return tables;
}// end show tables

  /**
   * list all other tables that have in their column one or more of the primary keys of the table <table-name>.
   *
   * @param  tableName  the queried table name
   * @return A LinkedList of related Table Name
   */
  public ResultSet showRelatedTables(String tableName) throws SQLException {

   String sql = "show columns from "+tableName+" where `Key` = \"PRI\"";
   ResultSet rs  = stmt.executeQuery(sql);
   String pkName = "";
   while(rs.next()) {
     pkName = rs.getString("Field");
   }
   sql = "SELECT DISTINCT TABLE_NAME, COLUMN_NAME  FROM INFORMATION_SCHEMA.COLUMNS WHERE COLUMN_NAME = "+"\'"+pkName+"\'"+" AND TABLE_NAME != "+"\'"+tableName+"\'";
   rs = stmt.executeQuery(sql);
   return rs;
   }//end showRelatedTables

  /**
   * Show all primary keys from all tables. Print the list of (table_name, column_name).
   *
   * @return A 2D array of (tablename,primary key)
   */
  public String[][] showAllPrimaryKeys() throws SQLException{
    LinkedList<String[]> tablesAndPK = new LinkedList<String[]>();
    LinkedList<String> tables = new LinkedList<String>();
    String sql = "show tables";
    ResultSet rs  = stmt.executeQuery(sql);
    String tableName = "";
    while(rs.next()){
     tableName = rs.getString("Tables_in_adventureworks");
     tables.add(tableName);
   }
   for(String tablename : tables){
     sql = "show columns from "+tablename+" where `Key` = \"PRI\"";
     rs  = stmt.executeQuery(sql);
     while(rs.next()){
      String pkName = rs.getString("Field");

      String tableAndPK[] = {tablename,pkName};
      tablesAndPK.add(tableAndPK);
    }
  }
  String arr[][]  = new String[tablesAndPK.size()][2];
  int i = 0;
  for (String[] temp: tablesAndPK){
    arr[i][0] = temp[0];
    arr[i][1] = temp[1];
    i++;
  }
  return arr;
   }//end showAllPrimaryKeys

  /**
   * Show all tables conatin the column name
   * @param  colName  the queried column name
   * @return A LinkedList of all tables conatin the column name
   */
  public ResultSet findColumn(String colName)throws SQLException{
    LinkedList<String> tanleNames = new LinkedList<>();
    String sql = "SELECT DISTINCT TABLE_NAME, COLUMN_NAME  FROM INFORMATION_SCHEMA.COLUMNS WHERE COLUMN_NAME = "+"\'"+colName+"\'";
    ResultSet rs = stmt.executeQuery(sql);

    return rs;
    } // end findColumn


    /* helper function*/
    /**
   * helper function 
   * @param  resultset  result set 
   * @return A LinkedList of the resultset
   */
    private LinkedList<String> resultSetToLinkList(ResultSet rs) throws SQLException{
      LinkedList<String> returnList = new LinkedList<>();
      while(rs.next()){
       returnList.add(rs.getString("TABLE_NAME"));
     }
     return returnList;
   }// end of result set to linklist

  /**
   * Show all paths from table1 to table2
   * @param  table1  name of the starting table
   * @param  table2  name of the ending table
   * @return String[] of one path.
   */
  public LinkedList<LinkedList<String>> searchPath(String table1,String table2)throws SQLException{
   LinkedList<LinkedList<String>> paths = new LinkedList<>();
   Hashtable<Integer, String> int_String = new Hashtable<Integer, String>();
   Hashtable<String, Integer> string_Int = new Hashtable<String, Integer>();
   String sql = "show tables";
   ResultSet rs  = stmt.executeQuery(sql);
   int index = 0;
   while(rs.next()){
    String tableName = rs.getString("Tables_in_adventureworks");
    int_String.put(index,tableName);
    string_Int.put(tableName,index);
    index++;
  }
  BidirectionalGraph g = new BidirectionalGraph(int_String.size(),0);
  for(int i = 0 ; i < int_String.size();i++){
    String currentTable = int_String.get(i);
    LinkedList<String> relatedTable = resultSetToLinkList(showRelatedTables(currentTable));
    for(int j = 0; j < relatedTable.size();j++){
     g.add_edge(i,string_Int.get(relatedTable.get(j)));
   }
 }
 String path = BidirectionalSearch.BidirectionalSearchPath(g, string_Int.get(table1), string_Int.get(table2));
 LinkedList<Integer> temp = new LinkedList<Integer>();
 String[] arr = path.split(" ");
 for(int i = 0 ; i < arr.length; i ++){
  temp.add(Integer.parseInt(arr[i]));
}
LinkedList<LinkedList<Integer>> allpath = new LinkedList<LinkedList<Integer>>();
allpath.add(temp);

for(List<Integer> list: allpath){
  LinkedList<String> tableNamePath = new LinkedList<>();
  for(int i = 0 ; i < list.size();i++){
   tableNamePath.add(int_String.get(list.get(i)));
 }
 paths.add(tableNamePath);
}
return paths;
    } // end searchPath

  /**
   * Show a table joined all tables in a path from table1 to table2
   * @param  table1  name of the starting table
   * @param  table2  name of the ending table
   * @return A table joined all tables in a path from table1 to table2
   */
  public ResultSet searchPathAndJoin(String table1,String table2)throws SQLException{
    String[][] list= showAllPrimaryKeys();
    Hashtable<String, String> tableName_PrimaryKey = new Hashtable<String, String>();
    for(int i = 0 ; i < list.length;i++){

      String arr[] = list[i];
      tableName_PrimaryKey.put(arr[0],arr[1]);
    }
    LinkedList<LinkedList<String>> paths = searchPath(table1,table2);
    LinkedList<String>path = paths.get(0);
    String sql = "select * from " + table1;
    for(int i = 1 ; i < path.size(); i++){
      String command = " left join "+path.get(i)+" on "+path.get(i-1)+"."+tableName_PrimaryKey.get(path.get(i)) + " = "+path.get(i) + "."+tableName_PrimaryKey.get(path.get(i)) + " ";
      sql += command;
    }
    sql+=";";
    ResultSet rs = stmt.executeQuery(sql);

    return rs;
  } // end of search path and join

  /**
   * Create a view with user defined sql script
   * @param  viewname  name of the creating view
   * @param  sql_in  user defined sql script
   */
  public void getView(String viewname, String sql_in)throws SQLException{
    String sql = "CREATE VIEW "+ viewname +" AS "+sql_in;
      //System.out.println(sql);
    stmt.executeUpdate(sql);

  } // end getView

  /**
   * Get Stats
   * @param  tableName  name of the table
   * @param  colName  name of the column in the table
   */
  public Stats getStats(String tableName, String colName)throws SQLException{
    String sql = "select count("+colName+") from "+tableName+" where "+ colName +" REGEXP '^-?[0-9]+$';";

    ResultSet rs  = stmt.executeQuery(sql);
    float col_max = 0;
    float col_min = 0;
    float col_mean = 0;
    float col_median = 0;
    rs.next();

    if(Integer.parseInt(rs.getString("count("+colName+")")) == 0){
      System.out.println("Categorical Data==> Stats Not Available;");
      return null;
    }else{
      sql = "select count(*) as `# of Data points`, max("+colName+") as `MAX`, min("+colName+") as `MIN`, avg("+colName+") as `MEAN` from "+tableName;
      rs  = stmt.executeQuery(sql);
      rs.next();
      col_max = Float.parseFloat(rs.getString("MAX"));
      col_min = Float.parseFloat(rs.getString("MIN"));
      col_mean = Float.parseFloat(rs.getString("MEAN"));
      // find Median
      sql = "SET @rowindex := -1";
      rs  = stmt.executeQuery(sql);
      sql = "SELECT AVG(d.value) as `MEDIAN` FROM (SELECT @rowindex:=@rowindex + 1 AS rowindex, "+tableName+"."+colName+" AS value FROM "+tableName+" ORDER BY "+tableName+"."+colName+") AS d WHERE d.rowindex IN (FLOOR(@rowindex / 2), CEIL(@rowindex / 2))";
      rs  = stmt.executeQuery(sql);
      rs.next();
      col_median = Float.parseFloat(rs.getString("MEDIAN"));
      // Histogram
      sql = "SELECT ROUND("+colName+", -2) AS bucket, COUNT(*) AS COUNT, RPAD('', LN(COUNT(*)), '*') AS bar FROM  "+tableName+" GROUP  BY bucket";
      Stats mystats= new Stats(col_max,col_min,col_mean,col_median,sql,stmt);
      return mystats;
    }
  } // end getStats

  /**
   * Show a table joined all tables in a path from table1 to table2
   * @param  city  name of the city
   * @return A table consist number of product to a particular city
   */
  public ResultSet NumOfProductToWhere(String city) throws SQLException{
    String sql = "SELECT   SUM(salesorderdetail.OrderQty) "
    +"FROM   productcategory   JOIN     product    JOIN     salesorderdetail "+
    "  ON product.ProductID = salesorderdetail.ProductID   JOIN     salesorderheader  "+
    "   ON salesorderdetail.SalesOrderID = salesorderheader.SalesorderID  "
    +" JOIN     address     ON salesorderheader.ShipToAddressID = address.AddressID "+
    "WHERE   address.City ="+"\'"+city+"\';";
    ResultSet rs  = stmt.executeQuery(sql);
    DBTablePrinter.printResultSet(rs);
    return rs;
  }// end get num of product to where

  /**
   * Show a table joined all tables in a path from table1 to table2
   * @param  number top number of item order subtotal item
   * @return A table joined all tables in a path from table1 to table2
   */
  public ResultSet InfoOfMostSubtotalItem(int number) throws SQLException{
    String sql = "SELECT salesorderheader.SalesOrderID,"+" salesorderheader.SubTotal as subtotal_compare,  SUM(salesorderdetail.OrderQty * salesorderdetail.UnitPrice), SUM(salesorderdetail.OrderQty * product.ListPrice)"+
    "FROM salesorderheader JOIN salesorderdetail ON salesorderheader.SalesOrderID = salesorderdetail.SalesOrderID JOIN product ON salesorderdetail.ProductID = product.ProductID GROUP BY salesorderheader.SalesOrderID, salesorderheader.SubTotal ORDER BY subtotal_compare DESC limit " + number+";";
    ResultSet rs  = stmt.executeQuery(sql);
    DBTablePrinter.printResultSet(rs);
    return rs;
  }// end info of most subtotal item

  /**
   * Show a table joined all tables in a path from table1 to table2
   * @param  tables A list of table to be join
   * @param  columnjoin A list of column to be join on
   * @return A table joined all tables in  from multiple table
   */
  public ResultSet joinTables(String tables) throws SQLException {
    String sql = "SELECT * From " + tables;
  /*  for(int i = 1 ; i < tables.size(); i++){
    sql = sql + " JOIN " + tables.get(i) + " On " + tables.get(i-1)+ "." + columnjoin.get(i-1).getLast() + " = " + tables.get(i)+ "." + columnjoin.get(i).getFirst() + " ";
    }
    sql = sql + "limit " + num;*/
    ResultSet rs  = stmt.executeQuery(sql);
  //  DBTablePrinter.printResultSet(rs);
    return rs;
  } // end joinTables

  /**
   * Show first few rows of a table
   * @param  tableName The name of table to be shown
   * @param  num number of row to be shown
   * @return A table that have num of rows shown
   */
  public ResultSet showHead(String tableName,int num) throws SQLException{
   LinkedList<LinkedList<String>> tablenumhead = new LinkedList<LinkedList<String>>();
   String sql = "SELECT * FROM " + tableName + " LIMIT " + num ;
   ResultSet rs  = stmt.executeQuery(sql);
   ResultSetMetaData rsmd = rs.getMetaData();
   DBTablePrinter.printResultSet(rs);
   return rs;
  }//end table head

  /**
   * Show all tables
   * @return A table that have num of rows shown result set
   */
  public ResultSet showTables1() throws SQLException{
   String sql = "show tables";
   LinkedList<String> tables = new LinkedList<>();
   ResultSet rs = stmt.executeQuery(sql);
   return rs;
 }// end of show tables 1

  /**
   * Show table's columns
    * @param  tableName The name of table to be shown
   * @param  columnNames the columns to be shown
   * @return A table that have num of rows shown result set
   */
 public ResultSet selectMutColumnNames(String tableName,String columnNames) throws SQLException{
   String sql = "SELECT "+ columnNames +" FROM " + tableName;
   ResultSet rs  = stmt.executeQuery(sql);
   //DBTablePrinter.printResultSet(rs);
   return rs;
  }//end table head


  /**
   * Show rows from starting row to ending row
   * @param  tableName The name of table to be shown
   * @param  from starting row
   * @param  to ending row
   * @return A subtable of a table from starting row to ending row
   */
  public ResultSet Slicing(String tableName,int from,int to ) throws SQLException{
   String sql = "select * from "+tableName+"  ORDER BY 'column_id 'DESC  LIMIT "+(from-1)+","+(to-from+1)+";";
   ResultSet rs = stmt.executeQuery(sql);
   DBTablePrinter.printResultSet(rs);
   return rs;
   }// end Slicing

  /**
   * Show the names of all views
   * @return A subtable of a table from starting row to ending row
   */
  public ResultSet getAllView() throws SQLException{
    String sql = "SHOW FULL TABLES WHERE table_type = 'VIEW';";

    ResultSet rs = stmt.executeQuery(sql);
    return rs;
  }// end getAllView

  /**
   * Delete the view with name viewName
   * @param viewName name of the view to be deleted
   */
  public void deleteView(String viewName)throws SQLException{
    try{
      String sql = "DROP VIEW IF EXISTS "+viewName+";";
      stmt.executeUpdate(sql);
    }catch(SQLException se){
      //Handle errors for JDBC
      se.printStackTrace();
    }
  } // end delete views
  /**
   * Return the table generated from default sql commands
   * @param sql sql default command
   */
  public ResultSet SQLCommand(String sql)throws SQLException{
    ResultSet rs = stmt.executeQuery(sql);
    return rs;
  }// end of sqlcommand

  /**
   * Return the ResultSet with taotal sale by years
   */
  public ResultSet getSaleByYear()throws SQLException{
    String sql = "select year(DueDate),SUM(TotalDue) from salesorderheader Group by YEAR(DueDate);";
    ResultSet rs = stmt.executeQuery(sql);
    return rs;
  }// end getSaleByYear

  /**
   * Return the ResultSet with count of single and marry employee
   */
  public ResultSet getEmployeeMarryStatus()throws SQLException{
    String sql = "select MaritalStatus, count(*) from employee Group by MaritalStatus; ";
    ResultSet rs = stmt.executeQuery(sql);
    return rs;
  } // end getEmployeeMarryStatus

    /**
   * Return the ResultSet with age distribution for employee
   */
  public ResultSet getEmployeeAgeDistribution()throws SQLException{
    String sql = "select count(*), case when datediff(now(), BirthDate) / 365.25 -20> 50 then 'above 51' when datediff(now(), BirthDate) / 365.25 -20> 40 then '41 - 50' when datediff(now(), BirthDate) / 365.25-20>  30 then '31 - 40' when datediff(now(), BirthDate) / 365.25 -20> 18 then '18 - 30' else 'under 20' end as age_group from employee group by age_group;";
    ResultSet rs = stmt.executeQuery(sql);
    return rs;
  }// end getEmployeeAgeDistribution
  /**
   * Return the ResultSet with Gender distribution employee
   */
  public ResultSet getEmployeeGenderDistribution()throws SQLException{
    String sql =  "select gender,count(*) from employee group by gender";
    ResultSet rs = stmt.executeQuery(sql);
    return rs;
  } //getEmployeeGenderDistribution
  /**
   * Return the ResultSet with total sale by country each year
   */
  public ResultSet getSaleByCountry()throws SQLException{
    String sql =  "select CountryRegionCode,year(DueDate) ,SUM(TotalDue) from salesorderheader  left join address On ShipToAddressID = AddressID  left join stateprovince on stateprovince.StateProvinceID = address.StateProvinceID Group by CountryRegionCode, YEAR(DueDate);";
    ResultSet rs = stmt.executeQuery(sql);
    return rs;
  }// end getSaleByCountry
   /**
   * Return the ResultSet with total number of customer by country 
   */
   public ResultSet getCustomerByCountry()throws SQLException{
    String sql =  "Select CountryRegionCode,count(*) from customer left join salesterritory on customer.TerritoryID = salesterritory.TerritoryID Group BY CountryRegionCode;";
    ResultSet rs = stmt.executeQuery(sql);
    return rs;
  } // end getCustomerByCountry


  /**
   * Generate a png file that consist the relation of all tables.
   */
  public void draw()throws SQLException{
    allTableNames = showTables();
    Graphviz gv = new Graphviz();
    Graph graph = new Graph("g1", GraphType.GRPAH);
    Hashtable<String, Node> string_Node = new Hashtable<String, Node>();
    for(int i = 0 ; i < allTableNames.size(); i++){
      String currentTable = allTableNames.get(i);

      Node node = new Node(currentTable);
      string_Node.put(currentTable, node);
      graph.addNode(node);
    }
    for(int i = 0; i < allTableNames.size();i++){
      String currentTable = allTableNames.get(i);
      ResultSet rs = showRelatedTables(currentTable);

      LinkedList<String> relatedTable = resultSetToLinkList(rs);

      for(int j = 0 ; j < relatedTable.size();j++){
        String relatedTableName = relatedTable.get(j);
        graph.addEdge(new Edge("",string_Node.get(currentTable),string_Node.get(relatedTableName)));
      }
    }
    String type = "png";
    File out = new File(tmpPath+"/outEX1."+ type);
    writeGraphToFile( gv.getGraphByteArray(graph, type, "100"), out );
  } // end draw


  /*helper function*/
  private int writeGraphToFile(byte[] img, File to){
    try {
      FileOutputStream fos = new FileOutputStream(to);
      fos.write(img);
      fos.close();
    } catch (java.io.IOException ioe) { return -1; }
    return 1;
  } // end writeGraphToFile

 }// end JDBC


  // Stats helper class
 class Stats{
  public float Max;
  public float Min;
  public float Mean;
  public float Median;
  public ResultSet rs;
  public String sql;
  public Statement stmt;
  LinkedList<Integer> count;
  LinkedList<String> bin;
    public int y_bins = 10; // default
    public Stats(float Max,float Min, float Mean, float Median, String sql, Statement stmt) throws SQLException {
      this.Min = Min;
      this.Max = Max;
      this.Mean = Mean;
      this.Median = Median;
      this.sql = sql;
      this.stmt = stmt;
      rs = stmt.executeQuery(sql);
      count = new LinkedList<Integer>();
      bin = new LinkedList<String>();
      createHistogram();
    }
    public void createHistogram() throws SQLException {
      // Extract Histogram info
      while(rs.next()){
       Integer count_temp = Integer.parseInt(rs.getString("COUNT"));
       String bin_temp = rs.getString("bucket");
       count.add(count_temp);
       bin.add(bin_temp);
     }
    } // end CreateHistogram

    public void displayHistogram() throws SQLException {
      // Calculate x-axis freq *
     float freq_interval = (Collections.max(count)-Collections.min(count))/y_bins;
     LinkedList<Integer> star = new LinkedList<Integer>();
     for(Integer temp:count){
      star.add(Math.round(temp/freq_interval));
    }
    System.out.print("  ");
    for(int i = 0;i <= y_bins*freq_interval; i += freq_interval){
      System.out.print(i+"__");
    }
    System.out.println();
    for(int i = 0;i < bin.size(); i++){
      System.out.print(i + " |");
      for(int j = 0;j < Math.ceil(freq_interval*star.get(i)/6.0); j++){
       System.out.print("*");

     }
     System.out.println();
   }
   System.out.println("y_bins value:"+ bin);
        // Display Histogram
   rs = stmt.executeQuery(sql);
   DBTablePrinter.printResultSet(rs);
    }// end displayHistogram

  }// end Stats
