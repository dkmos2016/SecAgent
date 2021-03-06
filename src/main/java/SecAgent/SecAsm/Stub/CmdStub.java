package SecAgent.SecAsm.Stub;

import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.utils.ParamsInfo;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

/** cannot use AsmReq*, use invoke instead for CMD inject */
public class CmdStub extends CommonStub {
  int tid_idx = newLocal(Type.getType(long.class));

  public CmdStub(
      int api,
      MethodVisitor methodVisitor,
      int access,
      String name,
      String descriptor,
      ParamsInfo paramsInfo) {
    super(api, methodVisitor, access, name, descriptor, paramsInfo);
  }

  private void process() {
    putStubData("CMD", T_OBJECT, 0);
  }

  private void process1() {
    // reversed for test

    newArrayList(params_idx);
    mv.visitLdcInsn(10);
    mv.visitVarInsn(ISTORE, tmp_obj);
    addListElement(params_idx, T_INT, tmp_obj);

    mv.visitLdcInsn(20);
    mv.visitVarInsn(ISTORE, tmp_obj);
    addListElement(params_idx, T_INT, tmp_obj);

    findAndExecute(
        "SecAgent.utils.ReqInfo",
        "doTest1",
        new Class[] {int.class, int.class},
        reqinfo_idx,
        params_idx,
        res_idx);

    //    findAndExecute("SecAgent.utils.ReqLocal", "getReqInfo", new Class[]{}, null_idx, null_idx,
    // reqinfo_idx);
  }

  @Override
  protected void onMethodEnter() {
    super.onMethodEnter();
    //    debug_print_offline(String.format("[DEBUG] [CmdStub]: %s", this.paramsInfo.toString()));
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
