package SecAgent.SecAsm.utils;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * for operate ReqInfo (ex: getUrl, setUrl, toString...)
 */
public class AsmReqInfoOp {

  /**
   * get saved ReqInfo from global ThreadLocal, just store in stack
   *
   * @param mv: MethodVisitor
   */
  public static void setHttpServletRequest(MethodVisitor mv, int reqinfo_idx, int src_idx) {
    mv.visitVarInsn(Opcodes.ALOAD, reqinfo_idx);
    mv.visitVarInsn(Opcodes.ALOAD, src_idx);
    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "SecAgent/utils/ReqInfo", "setHttpServletRequest", "(Ljavax/servlet/http/HttpServletRequest;)V", false);
  }

  public static void setStubData(MethodVisitor mv, int reqinfo_idx, int src_idx) {
    mv.visitVarInsn(Opcodes.ALOAD, reqinfo_idx);
    mv.visitVarInsn(Opcodes.ALOAD, src_idx);
    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "SecAgent/utils/ReqInfo", "setStubData", "(Ljava/lang/String;)V", false);
  }

  public static void getStubData(MethodVisitor mv, int reqinfo_idx, int dst_idx) {
    mv.visitVarInsn(Opcodes.ALOAD, reqinfo_idx);
    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "SecAgent/utils/ReqInfo", "getStubData", "()Ljava/lang/String;", false);
    mv.visitVarInsn(Opcodes.ASTORE, dst_idx);
  }


  public static void setStubDatas(MethodVisitor mv, int reqinfo_idx, int src_idx) {
    mv.visitVarInsn(Opcodes.ALOAD, reqinfo_idx);
    mv.visitVarInsn(Opcodes.ASTORE, src_idx);
    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "SecAgent/utils/ReqInfo", "setStubDatas", "([Ljava/lang/Object;)V", false);
  }

  public static void getStubDatas(MethodVisitor mv, int reqinfo_idx, int dst_idx, String sep) {
    mv.visitVarInsn(Opcodes.ALOAD, reqinfo_idx);
    mv.visitLdcInsn(sep);
    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "SecAgent/utils/ReqInfo", "getStubDatas", "(Ljava/lang/String;)Ljava/lang/String;", false);
    mv.visitVarInsn(Opcodes.ASTORE, dst_idx);
  }

  public static void setType(MethodVisitor mv, int reqinfo_idx, String type) {
    mv.visitVarInsn(Opcodes.ALOAD, reqinfo_idx);
    mv.visitLdcInsn(type);
    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "SecAgent/utils/ReqInfo", "setType", "(Ljava/lang/String;)V", false);
  }

  public static void getType(MethodVisitor mv, int reqinfo_idx, int dst_idx) {
    mv.visitVarInsn(Opcodes.ALOAD, reqinfo_idx);
    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "SecAgent/utils/ReqInfo", "getType", "()Ljava/lang/String;", false);
    mv.visitVarInsn(Opcodes.ASTORE, dst_idx);
  }

  public static void toStr(MethodVisitor mv, int reqinfo_idx, int dst_src) {
    mv.visitVarInsn(Opcodes.ALOAD, reqinfo_idx);
    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "SecAgent/utils/ReqInfo", "toString", "()Ljava/lang/String;", false);
    mv.visitVarInsn(Opcodes.ASTORE, dst_src);
  }

}