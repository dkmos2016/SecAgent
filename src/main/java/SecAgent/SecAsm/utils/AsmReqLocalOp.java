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
    Label try_excep = new Label();
    mv.visitTryCatchBlock(try_start, try_end, try_excep, "java/lang/Exception");
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
    mv.visitJumpInsn(GOTO, try_end);

    mv.visitLabel(try_excep);
    mv.visitFrame(F_SAME1, 0, null, 1, new Object[]{"java/lang/Exception"});
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception", "printStackTrace", "()V", false);

    mv.visitLabel(try_end);
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
