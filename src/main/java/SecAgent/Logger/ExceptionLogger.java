package SecAgent.Logger;

public class ExceptionLogger {

  public static void doExpLog(Exception e) {
    System.out.println("doExpLog: ");
    e.printStackTrace();
  }
}
