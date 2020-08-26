package SecAgent.SecAsm.utils;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

/**
 * for operate ReqInfo (ex: getUrl, setUrl, toString...)
 */
public class AsmReqInfoOp implements Opcodes {

  /**
   * get saved ReqInfo from global ThreadLocal, just store in stack
   *
   * @param mv: MethodVisitor
   */
  public static void setHttpServletRequest(MethodVisitor mv, int reqinfo_idx, int src_idx) {
    Label try_start = new Label();
    Label try_end = new Label();
    Label try_excep = new Label();
    mv.visitTryCatchBlock(try_start, try_end, try_excep, "java/lang/Exception");
    mv.visitLabel(try_start);

    mv.visitVarInsn(Opcodes.ALOAD, reqinfo_idx);
    mv.visitVarInsn(Opcodes.ALOAD, src_idx);
    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "SecAgent/utils/ReqInfo", "setHttpServletRequest", "(Ljavax/servlet/http/HttpServletRequest;)V", false);
    mv.visitJumpInsn(GOTO, try_end);
    
    mv.visitLabel(try_excep);
    mv.visitFrame(F_SAME1, 0, null, 1, new Object[]{"java/lang/Exception"});
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception", "printStackTrace", "()V", false);

    mv.visitLabel(try_end);

  }

  public static void putStubData(MethodVisitor mv, int reqinfo_idx, String type, int stk_idx, int src_idx) {
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
    mv.visitLdcInsn("SecAgent.utils.ReqInfo");
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/ClassLoader", "loadClass", "(Ljava/lang/String;)Ljava/lang/Class;", false);
    mv.visitLdcInsn("putStubData");
    mv.visitInsn(ICONST_3);
    mv.visitTypeInsn(ANEWARRAY, "java/lang/Class");
    mv.visitInsn(DUP);
    mv.visitInsn(ICONST_0);
    mv.visitLdcInsn(Type.getType("Ljava/lang/String;"));
    mv.visitInsn(AASTORE);
    mv.visitInsn(DUP);
    mv.visitInsn(ICONST_1);
    mv.visitLdcInsn(Type.getType("Ljava/lang/Throwable;"));
    mv.visitInsn(AASTORE);
    mv.visitInsn(DUP);
    mv.visitInsn(ICONST_2);
    mv.visitLdcInsn(Type.getType("Ljava/lang/Object;"));
    mv.visitInsn(AASTORE);
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getDeclaredMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", false);

    mv.visitVarInsn(ALOAD, reqinfo_idx);
    mv.visitInsn(ICONST_3);
    mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
    mv.visitInsn(DUP);
    mv.visitInsn(ICONST_0);
    mv.visitLdcInsn(type);
    mv.visitInsn(AASTORE);
    mv.visitInsn(DUP);
    mv.visitInsn(ICONST_1);
    mv.visitVarInsn(ALOAD, stk_idx);
    mv.visitInsn(AASTORE);
    mv.visitInsn(DUP);
    mv.visitInsn(ICONST_2);
    mv.visitVarInsn(ALOAD, src_idx);
    mv.visitInsn(AASTORE);

    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/reflect/Method", "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", false);
    mv.visitInsn(POP);

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

  public static void doJob(MethodVisitor mv, int reqinfo_idx) {
    mv.visitVarInsn(Opcodes.ALOAD, reqinfo_idx);
    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "SecAgent/utils/ReqInfo", "doJob", "()V", false);
  }

}