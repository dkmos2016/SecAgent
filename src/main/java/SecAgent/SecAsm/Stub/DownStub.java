package SecAgent.SecAsm.Stub;


import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.utils.ParamsInfo;
import org.objectweb.asm.MethodVisitor;


public class DownStub extends CommonStub {

  public DownStub(
    int api, MethodVisitor methodVisitor, int access, String name, String descriptor, ParamsInfo paramsInfo) {
    super(api, methodVisitor, access, name, descriptor, paramsInfo);

  }


  private void process() {
    debug_print_offline(
      String.format(
        "[DEBUG] [DownStub]: %s", this.paramsInfo.toString()));

    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/File", "getAbsolutePath", "()Ljava/lang/String;", false);
    mv.visitVarInsn(ASTORE, res_idx);

    debug_print_online(T_OBJECT, res_idx);
  }

  @Override
  protected void onMethodEnter() {
    super.onMethodEnter();
    System.out.println(String.format("stub into %s, params %d", paramsInfo, paramsInfo.getSize()));
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
