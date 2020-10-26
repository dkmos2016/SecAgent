package SecAgent.SecAsm.Stub;

import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.Utils.utils.ParamsInfo;
import org.objectweb.asm.MethodVisitor;

/** cannot use AsmReq*, use invoke instead stub for upload file test */
public class UploadStub extends CommonStub {
  public UploadStub(
      int api,
      MethodVisitor methodVisitor,
      int access,
      String name,
      String descriptor,
      ParamsInfo paramsInfo) {
    super(api, methodVisitor, access, name, descriptor, paramsInfo);
  }

  private void process() {
    newArrayList(params_idx);
    addListElement(params_idx, T_OBJECT, 1);

    putStubData("UPLOAD", T_OBJECT, params_idx);
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
