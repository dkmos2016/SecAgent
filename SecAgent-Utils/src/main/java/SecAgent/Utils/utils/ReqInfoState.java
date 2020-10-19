package SecAgent.Utils.utils;

public class ReqInfoState {
  public static final int PUTTED_METHOD;
  public static final int PUTTED_QUERYSTRING;
  public static final int PUTTED_QUERIES;
  public static final int PUTTED_INPUTSTREAM;
  public static final int PUTTED_OUTPUTSTREAM;

  public static final int PUTTED_SQL;
  public static final int PUTTED_EXEC;
  public static final int PUTTED_URI;
  public static final int PUTTED_XXE;
  public static final int PUTTED_DOWN;
  public static final int PUTTED_UPLOAD;

  static {
    PUTTED_METHOD = 1;
    PUTTED_QUERYSTRING = 2;
    PUTTED_QUERIES = 4;
    PUTTED_INPUTSTREAM = 8;
    PUTTED_OUTPUTSTREAM = 16;

    PUTTED_URI = 32;
    PUTTED_SQL = 64;
    PUTTED_EXEC = 128;
    PUTTED_XXE = 256;
    PUTTED_DOWN = 512;
    PUTTED_UPLOAD = 1024;
  }

  public static boolean isSetURI(int code) {
    return (code & (PUTTED_METHOD | PUTTED_QUERYSTRING | PUTTED_QUERIES | PUTTED_INPUTSTREAM)) > 0;
  }

  public static boolean isSetEXEC(int code) {
    return (code & PUTTED_EXEC) > 0;
  }

  public static boolean isSetXXE(int code) {
    return (code & PUTTED_XXE) > 0;
  }

  public static boolean isSetDOWN(int code) {
    return (code & PUTTED_DOWN) > 0;
  }

  public static boolean isSetUPLOAD(int code) {
    return (code & PUTTED_UPLOAD) > 0;
  }

  public static boolean isSetInputStream(int code) {
    return (code & PUTTED_INPUTSTREAM) > 0;
  }
}
