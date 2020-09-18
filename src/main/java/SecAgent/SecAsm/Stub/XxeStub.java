package SecAgent.SecAsm.Stub;

import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.utils.ParamsInfo;
import org.objectweb.asm.MethodVisitor;

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
    debug_print_offline(String.format("[DEBUG] [XxeStub]: %s", this.paramsInfo.toString()));

    // replace by putStubData
    //        newArrayList(params_idx);
    //        addListElement(params_idx, T_OBJECT, 2);
    //
    //        findAndExecute(
    //                "SecAgent.utils.ReqInfo",
    //                "setInputStream",
    //                new Class[] {InputStream.class},
    //                reqinfo_idx,
    //                params_idx,
    //                tmp_obj);
    //
    //        setNull(params_idx);

    putStubData("XXE", T_OBJECT, 2);
  }

  @Override
  protected void onMethodExit(int opcode) {
    super.onMethodExit(opcode);
  }
}
