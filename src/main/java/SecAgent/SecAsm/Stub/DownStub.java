package SecAgent.SecAsm.Stub;

import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.utils.ParamsInfo;
import org.objectweb.asm.MethodVisitor;

/** cannot use AsmReq*, use invoke instead for Arbitrary_File_Read */
public class DownStub extends CommonStub {

  public DownStub(
      int api,
      MethodVisitor methodVisitor,
      int access,
      String name,
      String descriptor,
      ParamsInfo paramsInfo) {
    super(api, methodVisitor, access, name, descriptor, paramsInfo);
  }

  private void process() {
    debug_print_offline(String.format("[DEBUG] [DownStub]: %s", this.paramsInfo.toString()));

    putStubData("DOWN", T_OBJECT, 1);

    debug_print_offline("DownStub done");
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
