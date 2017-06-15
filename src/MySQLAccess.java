import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

public class MySQLAccess {

  private Connection connect = null;
  public Statement statement = null;
  private PreparedStatement preparedStatement = null;
  private ResultSet resultSet = null;

  final private String host = "sql11.freesqldatabase.com";
  final private String user = "sql11180061";
  final private String passwd = "3v7GwsU4Dr";

  public void readDataBase() throws Exception {
    try {
      // This will load the MySQL driver, each DB has its own driver
      Class.forName("com.mysql.jdbc.Driver");

      // Setup the connection with the DB
      connect = DriverManager.getConnection(
          "jdbc:mysql://" + host + "/" + user + "?" + "user=" + user + "&password=" + passwd);

      // Statements allow to issue SQL queries to the database
      statement = connect.createStatement();
      // Result set get the result of the SQL query

    } catch (Exception e) {
      throw e;
    }

  }

  public void writeResultSet(ResultSet resultSet) throws SQLException {
    // ResultSet is initially before the first data set
    int count = 1;
    while (resultSet.next()) {
      // It is possible to get the columns via name
      // also possible to get the columns via the column number
      // which starts at 1
      // e.g. resultSet.getSTring(2);
      String authorFirst = resultSet.getString("AuthorFirst");
      String authorLast = resultSet.getString("AuthorLast");
      String bookTitle = resultSet.getString("BookTitle");
      String dateAdded = "" + resultSet.getString("DateAdded");
      String member = resultSet.getString("Member");
      System.out.println(count + " " + authorFirst + "\t \t" + authorLast + "\t \t" + bookTitle
          + "\t \t" + member + "\t \t" + dateAdded);
      count++;
    }
  }

  // You need to close the resultSet
  private void close() {
    try {
      if (resultSet != null) {
        resultSet.close();
      }

      if (statement != null) {
        statement.close();
      }

      if (connect != null) {
        connect.close();
      }
    } catch (Exception e) {

    }
  }

}
