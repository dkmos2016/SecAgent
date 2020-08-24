package SecAgent.SecAsm.utils;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;


/**
 *
 */
public class AsmReqLocalOp implements Opcodes {
  /**
   * todo not belong ASMReqInfo
   * get saved ReqInfo from global ThreadLocal, just store in stack
   *
   * @param mv: MethodVisitor
   */


  /**
   * todo not belong ASMReqInfo
   * get saved ReqInfo from global ThreadLocal, store in dst_idx
   *
   * @param mv
   * @param dst_idx
   */

  @Deprecated
  public static void getReqInfo_old(MethodVisitor mv, int dst_idx) {
    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "SecAgent/utils/ReqLocal", "getReqInfo", "()LSecAgent/utils/ReqInfo;", false);
    mv.visitVarInsn(Opcodes.ASTORE, dst_idx);
  }

  public static void getReqInfo(MethodVisitor mv, int dst_idx)
  {
    Label try_start = new Label();
    Label try_end = new Label();
    Label try_excep1 = new Label();
    mv.visitTryCatchBlock(try_start, try_end, try_excep1, "java/lang/ClassNotFoundException");
    Label try_excep2 = new Label();
    mv.visitTryCatchBlock(try_start, try_end, try_excep2, "java/lang/NoSuchMethodException");
    Label try_excep3 = new Label();
    mv.visitTryCatchBlock(try_start, try_end, try_excep3, "java/lang/IllegalAccessException");
    Label try_excep4 = new Label();
    mv.visitTryCatchBlock(try_start, try_end, try_excep4, "java/lang/reflect/InvocationTargetException");
    mv.visitLabel(try_start);
    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false);
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Thread", "getContextClassLoader", "()Ljava/lang/ClassLoader;", false);
    mv.visitLdcInsn("SecAgent.utils.ReqLocal");
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/ClassLoader", "loadClass", "(Ljava/lang/String;)Ljava/lang/Class;", false);
    mv.visitLdcInsn("getReqInfo");
    mv.visitInsn(ACONST_NULL);
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getDeclaredMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", false);
    mv.visitInsn(ACONST_NULL);
    mv.visitInsn(ACONST_NULL);
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/reflect/Method", "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", false);

    mv.visitVarInsn(ASTORE, dst_idx);

    mv.visitLabel(try_end);
    Label RET = new Label();
    mv.visitJumpInsn(GOTO, RET);

    mv.visitLabel(try_excep1);
    mv.visitFrame(F_SAME1, 0, null, 1, new Object[]{"java/lang/ClassNotFoundException"});
    mv.visitVarInsn(ASTORE, 1);
    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/ClassNotFoundException", "printStackTrace", "()V", false);
    mv.visitJumpInsn(GOTO, RET);
    mv.visitLabel(try_excep2);

    mv.visitFrame(F_SAME1, 0, null, 1, new Object[]{"java/lang/NoSuchMethodException"});
    mv.visitVarInsn(ASTORE, 1);
    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/NoSuchMethodException", "printStackTrace", "()V", false);
    mv.visitJumpInsn(GOTO, RET);
    mv.visitLabel(try_excep3);

    mv.visitFrame(F_SAME1, 0, null, 1, new Object[]{"java/lang/IllegalAccessException"});
    mv.visitVarInsn(ASTORE, 1);
    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/IllegalAccessException", "printStackTrace", "()V", false);
    mv.visitJumpInsn(GOTO, RET);
    mv.visitLabel(try_excep4);

    mv.visitFrame(F_SAME1, 0, null, 1, new Object[]{"java/lang/reflect/InvocationTargetException"});
    mv.visitVarInsn(ASTORE, 1);
    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/reflect/InvocationTargetException", "printStackTrace", "()V", false);

    mv.visitLabel(RET);
    mv.visitFrame(F_SAME, 0, null, 0, null);
  }

  @Deprecated
  /**
   * not use
   */
  public static void setReqInfo(MethodVisitor mv, int src_idx) {
    mv.visitVarInsn(Opcodes.ALOAD, src_idx);
    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "SecAgent/utils/ReqLocal", "setReqInfo", "(LSecAgent/utils/ReqInfo;)", false);
  }

  /**
   * remove threadlocal of request
   *
   * @param mv
   */
  public static void clearReqInfo(MethodVisitor mv) {
    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "SecAgent/utils/ReqLocal", "clear", "()V", false);
  }
}
