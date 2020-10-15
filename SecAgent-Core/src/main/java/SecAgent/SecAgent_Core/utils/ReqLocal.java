package SecAgent.SecAgent_Core.utils;

/*
 * when use DefaultLogger, alway cause NullPointException
 * */
public class ReqLocal {
  //  public static final DefaultLogger logger =
  //      DefaultLogger.getLogger(ReqLocal.class, Config.EXCEPTION_PATH);
  private static final ThreadLocal<ReqInfo> reqLocal = new ThreadLocal<ReqInfo>();

  public static ReqInfo getReqInfo() {
    ReqInfo reqInfo = reqLocal.get();
    if (reqInfo == null) {
      //      if (logger != null)
      //        logger.debug(String.format("tid %d create ReqInfo",
      // Thread.currentThread().getId()));
      reqInfo = new ReqInfo();
      reqLocal.set(reqInfo);
    } else {
      //      if (logger != null)
      //        logger.debug(String.format("tid %d got ReqInfo", Thread.currentThread().getId()));
    }

    return reqInfo;
  }

  public static void setReqLocal(ReqLocal reqLocal) {
    reqLocal = reqLocal;
  }

  public static void clear() {
    //    if (logger != null)
    //      logger.debug(String.format("tid %d destroy ReqInfo", Thread.currentThread().getId()));

    reqLocal.remove();
  }
}
