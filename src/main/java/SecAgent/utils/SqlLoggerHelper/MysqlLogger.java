package SecAgent.utils.SqlLoggerHelper;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlLogger {
  private static final MySqlConnectionPool pool; // = new MySqlConnectionPool(5);

  static {
    pool = new MySqlConnectionPool(5);
  }

  public static ResultSet execute(String sql) {
    Connection conn = null;
    try {
      conn = pool.getConnection();
      Statement statement = conn.createStatement();
      if (statement != null) {
        return statement.executeQuery(sql);
      }
      pool.returnConnection(conn);
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static void test() {}
}
