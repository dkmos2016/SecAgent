package SecAgent.SecAsm.Stub;

import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.utils.ParamsInfo;
import org.objectweb.asm.MethodVisitor;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class XxeStub extends CommonStub {

  public XxeStub(
      int api,
      MethodVisitor methodVisitor,
      int access,
      String name,
      String descriptor,
      ParamsInfo paramsInfo) {
    super(api, methodVisitor, access, name, descriptor, paramsInfo);
  }

  @Override
  protected void onMethodEnter() {
    super.onMethodEnter();
    process();
  }

  private void process() {
    //    debug_print_offline(String.format("[DEBUG] [XxeStub]: %s", this.paramsInfo.toString()));

    newArrayList(params_idx);
    addListElement(params_idx, T_OBJECT, 2);

    findAndExecute(
        "SecAgent.utils.Common",
        "transferTo",
        new Class[] {InputStream.class},
        null_idx,
        params_idx,
        res_idx);

    newArrayList(params_idx);
    addListElement(params_idx, T_OBJECT, res_idx);
    findAndExecute(
        "SecAgent.utils.Common",
        "transferFrom",
        new Class[] {ByteArrayOutputStream.class},
        null_idx,
        params_idx,
        res_idx);

    mv.visitVarInsn(ALOAD, res_idx);
    mv.visitVarInsn(ASTORE, 2);

    findAndExecute(
        "SecAgent.utils.Common",
        "transferFrom",
        new Class[] {ByteArrayOutputStream.class},
        null_idx,
        params_idx,
        res_idx);

    putStubData("XXE", T_OBJECT, res_idx);
  }

  @Override
  protected void onMethodExit(int opcode) {
    super.onMethodExit(opcode);
  }
}
