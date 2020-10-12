package SecAgent.utils.HttpClientLoggerHelper;

import SecAgent.utils.HttpClientHelper.HttpClientHelper;

public class HttpLogger implements Runnable {
  // java8 don't include httpclient
  private final Exception exception;
  private final String url;

  public HttpLogger(String url, Exception e) {
    this.exception = e;
    this.url = url;
  }

  @Override
  public void run() {
    System.out.println("HttpLogger async start");
    try {
      System.out.println(exception.getMessage());
      Thread.sleep(500);
      HttpClientHelper.doGet(url);

    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    System.out.println("HttpLogger async end");
  }
}
