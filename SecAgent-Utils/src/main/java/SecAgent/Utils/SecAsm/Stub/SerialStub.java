package SecAgent.Utils.SecAsm.Stub;

import SecAgent.Utils.utils.ParamsInfo;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

public class SerialStub extends AdviceAdapter implements Opcodes {
  protected ParamsInfo paramsInfo;

  public SerialStub(
      int api,
      MethodVisitor methodVisitor,
      int access,
      String name,
      String descriptor,
      ParamsInfo paramsInfo) {
    super(api, methodVisitor, access, name, descriptor);

    this.paramsInfo = paramsInfo;
  }

  public void debug_print_offline(String msg) {
    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
    mv.visitLdcInsn(msg);
    mv.visitMethodInsn(
        INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
  }

  public void debug_print_online(int opcode, int idx) {
    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
    mv.visitVarInsn(opcode, idx);
    mv.visitMethodInsn(
        INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V", false);
  }

  private void process() {

    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(
        INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V", false);
  }

  @Override
  protected void onMethodEnter() {
    super.onMethodEnter();

    process();
  }

  @Override
  protected void onMethodExit(int opcode) {
    super.onMethodExit(opcode);
  }

  @Override
  public void visitEnd() {
    super.visitEnd();
  }
}
