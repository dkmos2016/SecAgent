package SecAgent.utils;

public class ReqLocal {
  private final static ThreadLocal<ReqInfo> reqLocal = new ThreadLocal<ReqInfo>();

  public static ReqInfo getReqInfo() {

    ReqInfo reqInfo = reqLocal.get();
    if (reqInfo == null) {
      System.out.println(
        String.format("tid: %d, create new ReqInfo", Thread.currentThread().getId())
      );
      reqInfo = new ReqInfo();
      reqLocal.set(reqInfo);
    } else {
      System.out.println(
        String.format("tid: %d, return saved ReqInfo", Thread.currentThread().getId())
      );
    }

    return reqInfo;
  }

  public static void clear() {
    reqLocal.remove();
  }

}