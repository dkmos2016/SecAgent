package SecAgent.Utils.SecAsm.Stub;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

public class testStub extends AdviceAdapter implements Opcodes {
  protected testStub(
      int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
    super(api, methodVisitor, access, name, descriptor);
  }

  @Override
  public void visitCode() {
    super.visitCode();
  }

  @Override
  protected void onMethodEnter() {
    super.onMethodEnter();
    mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
    mv.visitVarInsn(LSTORE, 1);
  }

  @Override
  protected void onMethodExit(int opcode) {
    super.onMethodExit(opcode);
    mv.visitMethodInsn(INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
    mv.visitVarInsn(LLOAD, 1);
    mv.visitInsn(LSUB);
    mv.visitVarInsn(LSTORE, 3);
    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
    mv.visitLdcInsn("used %ds");
    mv.visitInsn(ICONST_1);
    mv.visitTypeInsn(ANEWARRAY, "java/lang/Object");
    mv.visitInsn(DUP);
    mv.visitInsn(ICONST_0);
    mv.visitVarInsn(LLOAD, 3);
    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
    mv.visitInsn(AASTORE);
    mv.visitMethodInsn(
        INVOKESTATIC,
        "java/lang/String",
        "format",
        "(Ljava/lang/String;[Ljava/lang/Object;)Ljava/lang/String;",
        false);
    mv.visitMethodInsn(
        INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
    mv.visitInsn(RETURN);
  }

  @Override
  public void visitEnd() {
    super.visitEnd();
  }
}
