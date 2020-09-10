package SecAgent.utils.HttpClientLoggerHelper;

public class HttpLogger implements Runnable {
  // java8 don't include httpclient
  private final Exception exception;

  public HttpLogger(Exception e) {
    this.exception = e;
  }

  @Override
  public void run() {
    System.out.println("HttpLogger async start");
    try {
      System.out.println(exception.getMessage());
      Thread.sleep(500);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("HttpLogger async end");
  }
}
