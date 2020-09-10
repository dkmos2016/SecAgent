package SecAgent.utils.SqlLoggerHelper;

import SecAgent.utils.AgentClassLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlLogger {
  private static Statement statement;

  static {
    try {
      //        Class.forName("", true, new AgentClassLoader());
      Class.forName("com.mysql.cj.jdbc.Driver", true, new AgentClassLoader());
      Connection connection =
          DriverManager.getConnection(
              "jdbc:mysql://localhost:3306/test?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
              "root",
              "123456");
      statement = connection.createStatement();
      statement.setQueryTimeout(3);
      statement.setPoolable(true);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void execute(String sql) {
    try {
      if (statement != null) {
        statement.execute(sql);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public static void test() {}
}
