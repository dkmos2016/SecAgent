package SecAgent.utils.SqlLoggerHelper;

import SecAgent.utils.AgentClassLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class MySqlConnectionPool {
  private ArrayList<Connection> connectionArrayList;

  public MySqlConnectionPool(int size) {
    this.connectionArrayList = new ArrayList<>(size);

    try {
      Class.forName("com.mysql.cj.jdbc.Driver", true, new AgentClassLoader());
      for (int i = 0; i < size; i++) {
        Connection connection =
            DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/test?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC",
                "root",
                "123456");
      }

    } catch (ClassNotFoundException e) {
      connectionArrayList = null;
      e.printStackTrace();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public synchronized Connection getConnection() throws SQLException {
    if (connectionArrayList == null) throw new SQLException("cannot get Connection");
    while (connectionArrayList.isEmpty()) {
      try {
        System.out.println("waiting");
        this.wait();
      } catch (Exception e) {
      }
    }

    return connectionArrayList.remove(0);
  }

  public synchronized void returnConnection(Connection conn) throws SQLException {
    if (connectionArrayList == null) throw new SQLException("cannot get Connection");
    if (conn == null) return;
    System.out.println("release");
    connectionArrayList.add(conn);
    this.notifyAll();
  }
}
