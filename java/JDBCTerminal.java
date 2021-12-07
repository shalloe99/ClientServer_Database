/*=============================================================================
 | JDBC Terminal
 *===========================================================================*/
 import java.sql.*;
 import java.util.*;
 import java.io.File;
 import java.io.FileOutputStream;
 import java.util.ArrayList;
 import java.util.List;
 import java.util.Random;


 public class JDBCTerminal{
  public static void main(String args[])throws SQLException{
    JDBC jdbc = new JDBC();
    jdbc.Connect();
    LinkedList<String> allTableNames = jdbc.showTables();
    JdbcTerminal(jdbc, allTableNames);
    jdbc.Close();
  }

  /**
   * Open running Jdbc Terminal
   *
   * @param jdbc JDBC object class
   * @return int error keys
   */
  private static LinkedList<String> resultSetToLinkList(ResultSet rs, String tableName) throws SQLException{
    LinkedList<String> returnList = new LinkedList<>();
    while(rs.next()){
     returnList.add(rs.getString(tableName));
   }
   return returnList;
 }// end resultSetToLinkList

  /**
   * get the result set size
   * @param rs the result set 
   * @return int size of the result set
   */
  public static int sizeOfResultSet(ResultSet rs)throws SQLException{
    rs.last();
    int size = rs.getRow();
    rs.beforeFirst();
    return size;
  }// end sizeOfResultSet

  /**
   * get the result set size
   * @param jdbc JDBC object class
   * @param allTableNames linked list with all table names
   * @return int size of the result set
   */
  public static int JdbcTerminal(JDBC jdbc,LinkedList<String> allTableNames) throws SQLException {
  /**************** EMBEDIED TERMINAL ******************/
  Scanner sc = new Scanner(System.in);
  Boolean run = true;
  while(run == true){
    try{
     System.out.print("jdb> ");
     String sql = sc.nextLine();

      if(sql.equals("quit;")){ // Quit
        System.out.println("ByeBye");
        run = false;
        continue;
      }
      if(sql.contains("jdb")){  // jdb commands
        if(sql.trim().charAt(sql.trim().length()- 1)!= ';'){ // format check
          System.out.println("Missing semicolon at the end");
          continue;
        }
        sql = sql.replace(";","");
        sql = sql.trim();
        if(sql.contains("jdb-show-related-tables")){ //1
          String arr[] = sql.split(" ");
          String tablename = arr[1];
          tablename = tablename.replace(";","");
          tablename = tablename.trim();
          if(allTableNames.contains(tablename)){
            ResultSet rs = jdbc.showRelatedTables(tablename);
            DBTablePrinter.printResultSet(rs);
          } else {
            System.out.println("Invalid Table Name");
            continue;
          }
        } else if(sql.contains("jdb-show-all-primary-keys")){ //2
          String[][] ls =  jdbc.showAllPrimaryKeys();
          for(int i = 0 ; i < ls.length;i++){
            String arr[] = ls[i];
            System.out.println("("+arr[0].trim()+","+arr[1].trim()+")");
          }
        } else if (sql.contains("jdb-find-column")){ //3
         String arr[] = sql.split(" ");
         String columnName = arr[1];
         columnName = columnName.replace(";","");
         columnName = columnName.trim();
         ResultSet rs = jdbc.findColumn(columnName);
         if(sizeOfResultSet(rs) == 0){
          System.out.println("No table have this column");
        } else {

          DBTablePrinter.printResultSet(rs);
        }
        } else if(sql.contains("jdb-search-path")){//4
         String[] arr = sql.split(" ");
         String table1 = arr[1];
         String table2 = arr[2];
         if(!allTableNames.contains(table1) || !allTableNames.contains(table2)){
          System.out.println("Invalid tablename");
        } else {
          LinkedList<LinkedList<String>> ls = jdbc.searchPath(table1,table2);
          for(LinkedList<String> list : ls){
           System.out.println(list);
         }
       }
     } else if (sql.contains("jdb-search-and-join")){//5
         String[] arr = sql.split(" ");
         String table1 = arr[1];
         String table2 = arr[2];
         if(!allTableNames.contains(table1) || !allTableNames.contains(table2)){
          System.out.println("Invalid tablename");
        } else {
          DBTablePrinter.printResultSet(jdbc.searchPathAndJoin(table1,table2));
        }
    } else if (sql.contains("jdb-get-view")){//6
         String txt = sql;
         String array1[]= txt.split(" ");
         String viewName = array1[1];
         StringBuilder builder = new StringBuilder();
         for(int i = 2; i < array1.length;i++) {
          builder.append(array1[i]+" ");
        }
        String mysql = builder.toString();
        mysql = mysql.trim();
        mysql = mysql.substring(1,mysql.length()-1);
        mysql = mysql.trim();
        jdbc.getView(viewName, mysql);
       //need regex
    } else if(sql.contains("jdb-stat")) { // 7
       LinkedList<String> allViewsName = resultSetToLinkList(jdbc.getAllView(),"Tables_in_adventureworks");
       String[] arr = sql.split(" ");
       String tableOrView = arr[1];
       String columnName = arr[2];
       if(allTableNames.contains(tableOrView) || allViewsName.contains(tableOrView)){
        Stats stats = jdbc.getStats(tableOrView,columnName);
        stats.displayHistogram();
      } else {
        System.out.println("Invalid table/view name");
      }
    } else if(sql.contains("jdb-slice")){ //8
          //jdb-slice tablename from to
         String[] arr = sql.split(" ");
         String tableName = arr[1];
         int from = Integer.parseInt(arr[2]);
         int to = Integer.parseInt(arr[3]);
         if(!allTableNames.contains(tableName)){
          System.out.println("Invalid tablename");
          }else {
         DBTablePrinter.printResultSet(jdbc.Slicing(tableName,from,to ));
       }
     }else if(sql.contains("jdb-show-head")){ //9
      // jdb-show-head tableName num
       String[] arr = sql.split(" ");
       String tableName = arr[1];
       int num = Integer.parseInt(arr[2]);
       if(!allTableNames.contains(tableName)){
        System.out.println("Invalid tablename");
      }else {
        DBTablePrinter.printResultSet(jdbc.showHead(tableName,num));
      }
    }else if (sql.contains("jdb-product-to-where")){//10
         // jdb-product-to-where city
   //how many product have been sold to this city
       String[] arr = sql.split(" ");
       String city = arr[1];
       DBTablePrinter.printResultSet(jdbc.NumOfProductToWhere(city));
     } else if (sql.contains("jdb-info-of-subtotalitem")){ //11
   //print the first number of product that has most subtotal items, show the total value, and price
       String[] arr = sql.split(" ");
       int number = Integer.parseInt(arr[1]);
       DBTablePrinter.printResultSet(jdbc.InfoOfMostSubtotalItem(number));
     } else if(sql.contains("jdb-see-views")){ // 12
        LinkedList<String> ls = resultSetToLinkList(jdbc.getAllView(),"Tables_in_adventureworks");
        for(String str: ls){
          System.out.println(str);
        }
     } else if (sql.contains("jdb-delete-view")){  //12
         //jdb-delete-view viewName
     LinkedList<String> allViewsName = resultSetToLinkList(jdbc.getAllView(),"Tables_in_adventureworks");
     String[] arr = sql.split(" ");
     String viewName = arr[1];

     if(allViewsName.contains(viewName)){
      jdbc.deleteView(viewName);
      System.out.println("Deleted "+ viewName);
    } else {
      System.out.println("Invalid table/view name");
    }
  } else if(sql.contains("jdb-see-views")){

    ResultSet rs = jdbc.getAllView();
    DBTablePrinter.printResultSet(rs);
    }else if (sql.contains("jdb-draw")){
      jdbc.draw();
    }// continue...*/
      // end all jdb commands
    } else {
      // All MySQL commands
      ResultSet rs = jdbc.SQLCommand(sql);
      DBTablePrinter.printResultSet(rs);
    }
  }catch(SQLException se){
        System.out.println(se);
      } // end Try-catch
  } // end while-loop
  return 0;
  } // end JDBC Terminal
}
