package SecAgent.utils;

public class ReqInfoState {
  public final static int PUTTED_METHOD;
  public final static int PUTTED_QUERYSTRING;
  public final static int PUTTED_QUERIES;
  public final static int PUTTED_INPUTSTREAM;
  public final static int PUTTED_OUTPUTSTREAM;

  public final static int PUTTED_SQL;
  public final static int PUTTED_EXEC;
  public final static int PUTTED_URI;
  public final static int PUTTED_XXE;
  public final static int PUTTED_DOWN;
  public final static int PUTTED_UPLOAD;

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
