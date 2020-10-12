package SecAgent.utils.SqlLoggerHelper;

import SecAgent.utils.AgentClassLoader;
import SecAgent.utils.Resources;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

@SuppressWarnings("unused")
public class MySqlConnectionPool {
  private final int size = Integer.parseInt(Resources.getProperty("MAX_CONNECTION"));
  private ArrayList<Connection> connectionArrayList;

  public MySqlConnectionPool() {
    this.connectionArrayList = new ArrayList<Connection>(size);

    initialize();
  }

  private void initialize() {
    System.out.println("initialize");
    try {
      Class.forName(
          "com.mysql.cj.jdbc.Driver",
          true,
          new AgentClassLoader(Thread.currentThread().getContextClassLoader()));

      for (int i = 0; i < this.size; i++) {
        try {
          System.out.println("try to getConnection");
          Connection connection =
              DriverManager.getConnection(
                  String.format(
                      "jdbc:mysql://%s:%s/%s?useSSL=%s&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                      Resources.getProperty("DB_HOST"),
                      Resources.getProperty("DB_PORT"),
                      Resources.getProperty("DB_NAME"),
                      Resources.getProperty("DB_SSL")),
                  Resources.getProperty("DB_USER"),
                  Resources.getProperty("DB_PASSWORD"));

          this.connectionArrayList.add(connection);
        } catch (SQLException e) {
        }
      }

    } catch (ClassNotFoundException e) {
      this.connectionArrayList = null;
      //      e.printStackTrace();
    }

    if (this.connectionArrayList.size() == 0) {
      this.connectionArrayList = null;
    }

    System.out.println("initialize done");
  }

  public synchronized Connection getConnection() throws SQLException {
    if (this.connectionArrayList == null) {
      System.out.println("connectionArrayList is null");
      throw new SQLException("cannot get Connection");
    }
    while (this.connectionArrayList.isEmpty()) {
      try {
        System.out.println("waiting");
        this.wait();
      } catch (Exception e) {
      }
    }

    return this.connectionArrayList.remove(0);
  }

  public synchronized void returnConnection(Connection conn) {
    if (this.connectionArrayList == null) return;
    if (conn == null) return;
    System.out.println("release");
    this.connectionArrayList.add(conn);
    this.notifyAll();
  }
}
