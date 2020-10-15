package SecAgent.SecAgent_Core.SecAsm.Stub.Container.Tomcat;


import SecAgent.SecAgent_Core.SecAsm.Common.CommonStub;
import SecAgent.SecAgent_Core.utils.ParamsInfo;
import org.objectweb.asm.MethodVisitor;

// for request.getInputSteam;
public class TomcatStub1 extends CommonStub {
  private final ParamsInfo paramsInfo;

  public TomcatStub1(
      int api,
      MethodVisitor methodVisitor,
      int access,
      String name,
      String descriptor,
      ParamsInfo paramsInfo) {
    super(api, methodVisitor, access, name, descriptor, paramsInfo);

    this.paramsInfo = paramsInfo;
  }

  private void process() {
    //    findAndGetSecProxyInstance(0, inst_idx);

    debug_print_offline(String.format("[DEBUG] [TomcatStub1]: %s", this.paramsInfo.toString()));

    findAndGetSecProxyInstance(1, inst_idx);
    mv.visitVarInsn(ALOAD, inst_idx);
    mv.visitVarInsn(ASTORE, 1);
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
}
