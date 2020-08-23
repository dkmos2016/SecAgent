package SecAgent.SecAsm.Common;


import SecAgent.utils.ParamsInfo;
import SecAgent.utils.ReqInfo;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import java.util.HashMap;


public class CommonStub extends AdviceAdapter implements Opcodes {
  protected final int sb_idx = newLocal(Type.getType(StringBuilder.class));
  protected final int tmp_sb = newLocal(Type.getType(StringBuilder.class));
  protected final int tmp_arr = newLocal(Type.getType("[Ljava/lang/Object;"));
  protected final int tmp_len = newLocal(Type.getType(int.class));
  protected final int tmp_idx = newLocal(Type.getType(int.class));
  protected final int tmp_obj = newLocal(Type.getType(Object.class));

  protected final int res_idx = newLocal(Type.getType(Object.class));

  protected final int hmap_idx = newLocal(Type.getType(HashMap.class));
  protected final int reqinfo_idx = newLocal(Type.getType(ReqInfo.class));

  protected final static int T_OBJECT = 1111111;

  protected final ParamsInfo paramsInfo;


  /**
   * Constructs a new {@link AdviceAdapter}.
   *
   * @param api           the ASM API version implemented by this visitor. Must be one of {@link
   *                      Opcodes#ASM4}, {@link Opcodes#ASM5}, {@link Opcodes#ASM6} or {@link Opcodes#ASM7}.
   * @param methodVisitor the method visitor to which this adapter delegates calls.
   * @param access        the method's access flags (see {@link Opcodes}).
   * @param name          the method's name.
   * @param descriptor    the method's descriptor (see {@link Type Type}).
   */
  public CommonStub(int api, MethodVisitor methodVisitor, int access, String name, String descriptor, ParamsInfo paramsInfo) {
    super(api, methodVisitor, access, name, descriptor);
    this.paramsInfo = paramsInfo;
  }

  protected void debug_print_offline(String msg) {
    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
    mv.visitLdcInsn(msg);
    mv.visitMethodInsn(
      INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
  }

  protected void debug_print_online1(int opcode, int idx) {
    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

    mv.visitVarInsn(opcode, idx);
    mv.visitMethodInsn(
      INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V", false);
  }

  protected void debug_print_online(int type, int obj_idx) {
    String desc;
    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
    switch(type) {
      case T_BYTE:
      case T_SHORT:
      case T_INT:
        desc = "(I)V";
        mv.visitVarInsn(ILOAD, obj_idx);
        break;
      case T_BOOLEAN:
        desc = "(Z)V";
        mv.visitVarInsn(ILOAD, obj_idx);
        break;

      case T_CHAR:
        desc = "(C)V";
        mv.visitVarInsn(ILOAD, obj_idx);
        break;

      case T_LONG:
        desc = "(J)V";
        mv.visitVarInsn(LLOAD, obj_idx);
        break;

      case T_DOUBLE:
        desc = "(J)V";
        mv.visitVarInsn(DLOAD, obj_idx);
        break;

      case T_FLOAT:
        desc = "(J)V";
        mv.visitVarInsn(FLOAD, obj_idx);
        break;

      case T_OBJECT:
      default:
        desc = "(Ljava/lang/Object;)V";
        mv.visitVarInsn(ALOAD, obj_idx);
        break;
    }
    mv.visitMethodInsn(
      INVOKEVIRTUAL, "java/io/PrintStream", "println", desc, false);
  }

  /**
   * generate new instance of cls, and saved to target
   *
   * @param cls:    classname, ex: java/lang/StringBuilder
   * @param target: index of instance, ex: tmp_sb
   */
  protected void newInstance(String cls, int target) {
    mv.visitTypeInsn(NEW, cls);
    mv.visitInsn(DUP);
    mv.visitMethodInsn(INVOKESPECIAL, cls, "<init>", "()V", false);
    mv.visitVarInsn(ASTORE, target);
  }

  protected void newHashMap(int obj_idx) {
//        mv.visitTypeInsn(NEW, "java/lang/HashMap");
//        mv.visitInsn(DUP);
//        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/HashMap", "<init>", "()V", false);
//        mv.visitVarInsn(ASTORE, obj_idx);
    newInstance("java/lang/HashMap", obj_idx);
  }

  /**
   * new StringBuilder()
   *
   * @param sb_idx
   */
  protected void newStringBuilder(int sb_idx) {
//        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
//        mv.visitInsn(DUP);
//        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
//        mv.visitVarInsn(ASTORE, sb_idx);
    this.newInstance("java/lang/StringBuilder", sb_idx);
  }

  /**
   * sb.append(obj);
   *
   * @param sb_idx:  index of StringBuilder's instance
   * @param obj_idx: index of target
   */
  protected void append(int sb_idx, int obj_idx) {
    mv.visitVarInsn(ALOAD, sb_idx);
    mv.visitVarInsn(ALOAD, obj_idx);
    mv.visitMethodInsn(
      INVOKEVIRTUAL,
      "java/lang/StringBuilder",
      "append",
      "(Ljava/lang/Object;)Ljava/lang/StringBuilder;",
      false);
    mv.visitInsn(POP);
  }

  /**
   * sb.append(obj);
   *
   * @param sb_idx: index of StringBuilder's instance
   * @param sep:    index of target
   */
  protected void append(int sb_idx, String sep) {
    mv.visitVarInsn(ALOAD, sb_idx);
    mv.visitLdcInsn(sep);
    mv.visitMethodInsn(
      INVOKEVIRTUAL,
      "java/lang/StringBuilder",
      "append",
      "(Ljava/lang/Object;)Ljava/lang/StringBuilder;",
      false);
    mv.visitInsn(POP);
  }

  /**
   * dst = sb.toString();
   * @param sb_idx
   * @param dst_idx
   */
  protected void toStr(int sb_idx, int dst_idx) {
    mv.visitVarInsn(ALOAD, sb_idx);
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
    mv.visitVarInsn(ASTORE, dst_idx);
  }

  /**
   * print trace stack
   */
  protected void stackTrack() {
    debug_print_offline(
      String.format(
        "[DEBUG] [SpringUrlStub]: %s", this.paramsInfo.toString()));

    // StackTraceElement[] tmp_arr = new Throwable().getStackTrace();
    mv.visitTypeInsn(NEW, "java/lang/Throwable");
    mv.visitInsn(DUP);
    mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Throwable", "<init>", "()V", false);
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Throwable", "getStackTrace", "()[Ljava/lang/StackTraceElement;", false);
    mv.visitVarInsn(ASTORE, tmp_arr);

    Label if_empty = new Label();
    mv.visitVarInsn(ALOAD, tmp_arr);
    mv.visitJumpInsn(IFNULL, if_empty);

    // StringBuilder sb = new StringBuilder();
    newStringBuilder(tmp_sb);

    // tmp_len = tmp_arr.length
    // tmp_idx = 0;
    mv.visitVarInsn(ALOAD, tmp_arr);
    mv.visitInsn(ARRAYLENGTH);
    mv.visitVarInsn(ISTORE, tmp_len);
    mv.visitInsn(ICONST_0);
    mv.visitVarInsn(ISTORE, tmp_idx);

    Label loop_start = new Label();
    Label loop_end = new Label();
    mv.visitLabel(loop_start);

    // if tmp_idx < tmp_len
    mv.visitVarInsn(ILOAD, tmp_idx);
    mv.visitVarInsn(ILOAD, tmp_len);
    mv.visitJumpInsn(IF_ICMPGE, loop_end);

    // tmp_obj = tmp_arr[tmp_idx]
    mv.visitVarInsn(ALOAD, tmp_arr);
    mv.visitVarInsn(ILOAD, tmp_idx);
    mv.visitInsn(AALOAD);
    mv.visitVarInsn(ASTORE, tmp_obj);

    // tmp_sb.append(tmp_obj.toString());
    append(tmp_sb, tmp_obj);
    append(tmp_sb, "\n");

    // loop
    mv.visitIincInsn(tmp_idx, 1);
    mv.visitJumpInsn(GOTO, loop_start);

    // loop done
    mv.visitLabel(loop_end);

    // do something else  (ex log, upload, print,..)
    debug_print_online(ALOAD, tmp_sb);

    mv.visitLabel(if_empty);
  }

  public void ReqTest() {
    debug_print_offline("====ReqTest====");
    ASMReqOp.getReqInfo(mv, reqinfo_idx);

    ASMReqOp.setUrl(mv, reqinfo_idx, res_idx);
    ASMReqOp.getUrl(mv, reqinfo_idx, tmp_obj);

    debug_print_online(ALOAD, tmp_obj);
  }

    protected void getGlobalReqInfo(int reqinfo_idx) {
      ASMReqOp.getReqInfo(mv, reqinfo_idx);
    }

  @Deprecated
  protected void setUrl(int reqinfo_idx, int src_idx) {
    ASMReqOp.setUrl(mv, reqinfo_idx, src_idx);
  }

  protected void setType(int reqinfo_idx, int src_idx) {
    ASMReqOp.setType(mv, reqinfo_idx, src_idx);
  }

  @Deprecated
  protected void setMethod(int reqinfo_idx, int src_idx) {
    ASMReqOp.setMethod(mv, reqinfo_idx, src_idx);
  }

  protected void setStubData(int reqinfo_idx, int src_idx) {
    ASMReqOp.setStubData(mv, reqinfo_idx, src_idx);
  }

  @Deprecated
  protected void putQuery(int reqinfo_idx, int key_idx, int value_idx) {
    ASMReqOp.putQuery(mv, reqinfo_idx, key_idx, value_idx);
  }

  @Deprecated
  protected void setQueries(int reqinfo_idx, int hmap_idx) {
    ASMReqOp.setQueries(mv, reqinfo_idx, hmap_idx);
  }

  @Deprecated
  protected void setHeaders(int reqinfo_idx, int hmap_idx) {
    ASMReqOp.setHeaders(mv, reqinfo_idx, hmap_idx);
  }

  protected void setHttpServletRequest(int reqinfo_idx, int src_idx) {
    ASMReqOp.setHttpServletRequest(mv, reqinfo_idx, src_idx);
  }

  protected void req2str(int reqinfo_idx, int dst_src) {
    ASMReqOp.toStr(mv, reqinfo_idx, dst_src);
  }

  public void ReqTest4Sql() {
    debug_print_offline("====ReqTest====");
    ASMReqOp.getReqInfo(mv, reqinfo_idx);
    ASMReqOp.getUrl(mv, reqinfo_idx, tmp_obj);

    debug_print_online(ALOAD, tmp_obj);
  }



  /**
   * for operate ReqInfo (ex: getUrl, setUrl, toString...)
   */
  public static class ASMReqOp {

    /**
     * get saved ReqInfo from global ThreadLocal, just store in stack
     *
     * @param mv: MethodVisitor
     */
    static void setHttpServletRequest(MethodVisitor mv, int reqinfo_idx, int src_idx) {
      mv.visitVarInsn(ALOAD, reqinfo_idx);
      mv.visitVarInsn(ALOAD, src_idx);
      mv.visitMethodInsn(INVOKEVIRTUAL, "SecAgent/utils/ReqInfo", "setHttpServletRequest", "(Ljavax/servlet/http/HttpServletRequest;)V", false);
    }

    /**
     *
     * todo not belong ASMReqInfo
     * get saved ReqInfo from global ThreadLocal, just store in stack
     *
     * @param mv: MethodVisitor
     */
    static void getReqInfo(MethodVisitor mv) {
      mv.visitMethodInsn(INVOKEVIRTUAL, "SecAgent/utils/ReqLocal", "getReqInfo", "()LSecAgent/utils/ReqInfo;", false);
    }


    /**
     *
     * todo not belong ASMReqInfo
     * get saved ReqInfo from global ThreadLocal, store in dst_idx
     *
     * @param mv
     * @param dst_idx
     */
    static void getReqInfo(MethodVisitor mv, int dst_idx) {
      mv.visitMethodInsn(INVOKESTATIC, "SecAgent/utils/ReqLocal", "getReqInfo", "()LSecAgent/utils/ReqInfo;", false);
      mv.visitVarInsn(ASTORE, dst_idx);
    }


    @Deprecated
    static void setUrl(MethodVisitor mv, int reqinfo_idx, int src_idx) {
      mv.visitVarInsn(ALOAD, reqinfo_idx);
      mv.visitVarInsn(ALOAD, src_idx);
      mv.visitMethodInsn(INVOKEVIRTUAL, "SecAgent/utils/ReqInfo", "setUrl", "(Ljava/lang/String;)V", false);
    }

    @Deprecated
    static void getUrl(MethodVisitor mv, int reqinfo_idx, int dst_idx) {
      mv.visitVarInsn(ALOAD, reqinfo_idx);
      mv.visitMethodInsn(INVOKEVIRTUAL, "SecAgent/utils/ReqInfo", "getUrl", "()Ljava/lang/String;", false);
      mv.visitVarInsn(ASTORE, dst_idx);
    }

    @Deprecated
    static void setMethod(MethodVisitor mv, int reqinfo_idx, int src_idx) {
      mv.visitVarInsn(ALOAD, reqinfo_idx);
      mv.visitVarInsn(ALOAD, src_idx);
      mv.visitMethodInsn(INVOKEVIRTUAL, "SecAgent/utils/ReqInfo", "setMethod", "(Ljava/lang/String;)V", false);
    }

    @Deprecated
    static void getMethod(MethodVisitor mv, int reqinfo_idx, int dst_idx) {
      mv.visitVarInsn(ALOAD, reqinfo_idx);
      mv.visitMethodInsn(INVOKEVIRTUAL, "SecAgent/utils/ReqInfo", "getMethod", "()Ljava/lang/String;", false);
      mv.visitVarInsn(ASTORE, dst_idx);
    }

    static void setStubData(MethodVisitor mv, int reqinfo_idx, int src_idx) {
      mv.visitVarInsn(ALOAD, reqinfo_idx);
      mv.visitVarInsn(ALOAD, src_idx);
      mv.visitMethodInsn(INVOKEVIRTUAL, "SecAgent/utils/ReqInfo", "setStubData", "(Ljava/lang/String;)V", false);
    }

    static void getStubData(MethodVisitor mv, int reqinfo_idx, int dst_idx) {
      mv.visitVarInsn(ALOAD, reqinfo_idx);
      mv.visitMethodInsn(INVOKEVIRTUAL, "SecAgent/utils/ReqInfo", "getStubData", "()Ljava/lang/String;", false);
      mv.visitVarInsn(ASTORE, dst_idx);
    }

    static void setType(MethodVisitor mv, int reqinfo_idx, int src_idx) {
      mv.visitVarInsn(ALOAD, reqinfo_idx);
      mv.visitVarInsn(ALOAD, src_idx);
      mv.visitMethodInsn(INVOKEVIRTUAL, "SecAgent/utils/ReqInfo", "setType", "(Ljava/lang/String;)V", false);
    }

    static void getType(MethodVisitor mv, int reqinfo_idx, int dst_idx) {
      mv.visitVarInsn(ALOAD, reqinfo_idx);
      mv.visitMethodInsn(INVOKEVIRTUAL, "SecAgent/utils/ReqInfo", "getType", "()Ljava/lang/String;", false);
      mv.visitVarInsn(ASTORE, dst_idx);
    }

    @Deprecated
    static void putQuery(MethodVisitor mv, int reqinfo_idx, int key_idx, int value_idx) {
      mv.visitVarInsn(ALOAD, reqinfo_idx);
      mv.visitVarInsn(ALOAD, key_idx);
      mv.visitVarInsn(ALOAD, value_idx);
      mv.visitMethodInsn(INVOKEVIRTUAL, "SecAgent/utils/ReqInfo", "setQuery", "(Ljava/lang/String;Ljava/lang/String;)V", false);
    }

    @Deprecated
    static void getQuery(MethodVisitor mv, int reqinfo_idx, int key_idx, int dst_idx) {
      mv.visitVarInsn(ALOAD, reqinfo_idx);
      mv.visitVarInsn(ALOAD, key_idx);
      mv.visitMethodInsn(INVOKEVIRTUAL, "SecAgent/utils/ReqInfo", "getQuery", "(Ljava/lang/String;)Ljava/lang/String;", false);
      mv.visitVarInsn(ASTORE, dst_idx);
    }

    @Deprecated
    static void setQueries(MethodVisitor mv, int reqinfo_idx, int hmap_idx) {
      mv.visitVarInsn(ALOAD, reqinfo_idx);
      mv.visitVarInsn(ALOAD, hmap_idx);
      mv.visitMethodInsn(INVOKEVIRTUAL, "SecAgent/utils/ReqInfo", "setQueries", "(Ljava/util/HashMap;)V", false);
    }

    @Deprecated
    static void getQueries(MethodVisitor mv, int reqinfo_idx, int dst_idx) {
      mv.visitVarInsn(ALOAD, reqinfo_idx);
      mv.visitMethodInsn(INVOKEVIRTUAL, "SecAgent/utils/ReqInfo", "getQueries", "()Ljava/util/HashMap;", false);
      mv.visitVarInsn(ASTORE, dst_idx);
    }

    @Deprecated
    static void setHeaders(MethodVisitor mv, int reqinfo_idx, int hmap_idx) {
      mv.visitVarInsn(ALOAD, reqinfo_idx);
      mv.visitVarInsn(ALOAD, hmap_idx);
      mv.visitMethodInsn(INVOKEVIRTUAL, "SecAgent/utils/ReqInfo", "setHeaders", "(Ljava/util/HashMap;)V", false);
    }

    @Deprecated
    static void getHeaders(MethodVisitor mv, int reqinfo_idx, int dst_idx) {
      mv.visitVarInsn(ALOAD, reqinfo_idx);
      mv.visitMethodInsn(INVOKEVIRTUAL, "SecAgent/utils/ReqInfo", "getHeaders", "()Ljava/lang/String;", false);
      mv.visitVarInsn(ASTORE, dst_idx);
    }

    static void toStr(MethodVisitor mv, int reqinfo_idx, int dst_src){
      mv.visitVarInsn(ALOAD, reqinfo_idx);
      mv.visitMethodInsn(INVOKEVIRTUAL, "SecAgent/utils/ReqInfo", "toString", "()Ljava/lang/String;", false);
      mv.visitVarInsn(ASTORE, dst_src);
    }

  }

  /**
   * Logger.info
   */
  protected void info(int obj_idx) {
    LOGGER.info(mv, obj_idx);
  }

  /**
   * Logger.debug
   */
  protected void debug(int obj_idx) {
    LOGGER.debug(mv, obj_idx);
  }

  /**
   * Logger.warn
   */
  protected void warn(int obj_idx) {
    LOGGER.warn(mv, obj_idx);
  }

  /**
   * Logger.error
   */
  protected void error(int obj_idx) {
    LOGGER.error(mv, obj_idx);
  }

  /**
   * todo other function
   */

  protected static class LOGGER {

    protected static void info(MethodVisitor mv, int obj_idx) {
      mv.visitVarInsn(ALOAD, obj_idx);
      mv.visitMethodInsn(INVOKESTATIC, "SecAgent/Logger/DefaultLogger", "info", "(Ljava/lang/Object;)V", false);
    }

    protected static void debug(MethodVisitor mv, int obj_idx) {
      mv.visitVarInsn(ALOAD, obj_idx);
      mv.visitMethodInsn(INVOKESTATIC, "SecAgent/Logger/DefaultLogger", "debug", "(Ljava/lang/Object;)V", false);
    }

    protected static void warn(MethodVisitor mv, int obj_idx) {
      mv.visitVarInsn(ALOAD, obj_idx);
      mv.visitMethodInsn(INVOKESTATIC, "SecAgent/Logger/DefaultLogger", "warn", "(Ljava/lang/Object;)V", false);
    }

    protected static void error(MethodVisitor mv, int obj_idx) {
      mv.visitVarInsn(ALOAD, obj_idx);
      mv.visitMethodInsn(INVOKESTATIC, "SecAgent/Logger/DefaultLogger", "error", "(Ljava/lang/Object;)V", false);
    }

  }
}
