package SecAgent.SecAsm.utils;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


/**
 *
 */
public class AsmReqLocalOp {
  /**
   * todo not belong ASMReqInfo
   * get saved ReqInfo from global ThreadLocal, just store in stack
   *
   * @param mv: MethodVisitor
   */
  static void getReqInfo(MethodVisitor mv) {
    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "SecAgent/utils/ReqLocal", "getReqInfo", "()LSecAgent/utils/ReqInfo;", false);
  }


  /**
   * todo not belong ASMReqInfo
   * get saved ReqInfo from global ThreadLocal, store in dst_idx
   *
   * @param mv
   * @param dst_idx
   */
  public static void getReqInfo(MethodVisitor mv, int dst_idx) {
    mv.visitMethodInsn(Opcodes.INVOKESTATIC, "SecAgent/utils/ReqLocal", "getReqInfo", "()LSecAgent/utils/ReqInfo;", false);
    mv.visitVarInsn(Opcodes.ASTORE, dst_idx);
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
