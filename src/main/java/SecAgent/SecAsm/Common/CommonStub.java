package SecAgent.SecAsm.Common;


import SecAgent.SecAsm.utils.AsmLogger;
import SecAgent.SecAsm.utils.AsmReqInfoOp;
import SecAgent.SecAsm.utils.AsmReqLocalOp;
import SecAgent.utils.ParamsInfo;
import SecAgent.utils.ReqInfo;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import java.util.HashMap;


public class CommonStub extends AdviceAdapter implements Opcodes {
  protected final static int T_OBJECT = 1111111;
  protected final int sb_idx = newLocal(Type.getType(StringBuilder.class));
  protected final int tmp_sb = newLocal(Type.getType(StringBuilder.class));
  protected final int tmp_arr = newLocal(Type.getType("[Ljava/lang/Object;"));
  protected final int tmp_len = newLocal(Type.getType(int.class));
  protected final int tmp_idx = newLocal(Type.getType(int.class));
  protected final int tmp_obj = newLocal(Type.getType(Object.class));
  protected final int res_idx = newLocal(Type.getType(Object.class));
  protected final int hmap_idx = newLocal(Type.getType(HashMap.class));
  protected final int reqinfo_idx = newLocal(Type.getType(ReqInfo.class));
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

  protected void debug_print_online(int type, int obj_idx) {
    String desc;
    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
    switch (type) {
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
   * sb.append(obj.toString());
   *
   * @param sb_idx:  index of StringBuilder's instance
   * @param obj_idx: index of target
   */
  protected void append(int sb_idx, int obj_idx) {
    mv.visitVarInsn(ALOAD, sb_idx);
    mv.visitVarInsn(ALOAD, obj_idx);
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;", false);
    mv.visitMethodInsn(
      INVOKEVIRTUAL,
      "java/lang/StringBuilder",
      "append",
      "(Ljava/lang/Object;)Ljava/lang/StringBuilder;",
      false);
    mv.visitInsn(POP);
  }

  /**
   * sb.append(str);
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
   * sb.appent(o)
   *
   * @param type
   * @param obj_idx
   */
  protected void append(int sb_idx, int type, int obj_idx) {
    mv.visitVarInsn(ALOAD, sb_idx);

    String desc = "(Ljava/lang/Object;)Ljava/lang/StringBuilder;";
    switch (type) {
      case T_BYTE:
      case T_SHORT:
      case T_INT:
        desc = "(I)Ljava/lang/StringBuilder;";
        mv.visitVarInsn(ILOAD, obj_idx);
        break;

      case T_BOOLEAN:
        desc = "(Z)Ljava/lang/StringBuilder;";
        mv.visitVarInsn(ILOAD, obj_idx);
        break;

      case T_CHAR:
        desc = "(C)Ljava/lang/StringBuilder;";
        mv.visitVarInsn(ILOAD, obj_idx);
        break;

      case T_LONG:
        desc = "(J)Ljava/lang/StringBuilder;";
        mv.visitVarInsn(LLOAD, obj_idx);
        break;

      case T_DOUBLE:
        desc = "(J)Ljava/lang/StringBuilder;";
        mv.visitVarInsn(DLOAD, obj_idx);
        break;

      case T_FLOAT:
        desc = "(J)Ljava/lang/StringBuilder;";
        mv.visitVarInsn(FLOAD, obj_idx);
        break;

      case T_OBJECT:
      default:
        desc = "(Ljava/lang/Object;)Ljava/lang/StringBuilder;";
        mv.visitVarInsn(ALOAD, obj_idx);
        break;
    }

    mv.visitMethodInsn(
      INVOKEVIRTUAL, "java/lang/StringBuilder", "append", desc, false);
    mv.visitInsn(POP);
  }

  /**
   * sb.appent(o)
   *
   * @param type
   * @param obj_idx
   */
  protected void setValue(int tar_idx, int type, Object obj_idx) {
    mv.visitLdcInsn(obj_idx);
    switch (type) {
      case T_BYTE:
      case T_SHORT:
      case T_INT:
      case T_BOOLEAN:
      case T_CHAR:
        mv.visitVarInsn(ISTORE, tar_idx);
        break;

      case T_LONG:
        mv.visitVarInsn(LSTORE, tar_idx);
        break;

      case T_DOUBLE:
        mv.visitVarInsn(DSTORE, tar_idx);
        break;

      case T_FLOAT:
        mv.visitVarInsn(FSTORE, tar_idx);
        break;

      case T_OBJECT:
      default:
        mv.visitVarInsn(ASTORE, tar_idx);
        break;
    }

  }

  /**
   * dst = sb.toString();
   *
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


  protected void getGlobalReqInfo(int reqinfo_idx) {
    AsmReqLocalOp.getReqInfo(mv, reqinfo_idx);
  }

  @Deprecated
  protected void setType(int reqinfo_idx, String src) {
    AsmReqInfoOp.setType(mv, reqinfo_idx, src);
  }


  @Deprecated
  protected void setStubData(int reqinfo_idx, int src_idx) {
    AsmReqInfoOp.setStubData(mv, reqinfo_idx, src_idx);
  }


  protected void setHttpServletRequest(int reqinfo_idx, int src_idx) {
    AsmReqInfoOp.setHttpServletRequest(mv, reqinfo_idx, src_idx);
  }


  @Deprecated
  protected void req2str(int reqinfo_idx, int dst_src) {
    AsmReqInfoOp.toStr(mv, reqinfo_idx, dst_src);
  }

  /**
   * Logger.info
   */
  protected void info(int obj_idx) {
    AsmLogger.info(mv, obj_idx);
  }

  /**
   * Logger.debug
   */
  protected void debug(int obj_idx) {
    AsmLogger.debug(mv, obj_idx);
  }

  /**
   * Logger.warn
   */
  protected void warn(int obj_idx) {
    AsmLogger.warn(mv, obj_idx);
  }

  /**
   * Logger.error
   */
  protected void error(int obj_idx) {
    AsmLogger.error(mv, obj_idx);
  }
}
