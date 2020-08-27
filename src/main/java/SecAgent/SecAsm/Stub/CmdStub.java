package SecAgent.SecAsm.Stub;

import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.utils.ParamsInfo;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;



/**
 * cannot use AsmReq*, use invoke instead
 * for CMD inject
 */
public class CmdStub extends CommonStub {
  int tid_idx = newLocal(Type.getType(long.class));

  public CmdStub(
    int api, MethodVisitor methodVisitor, int access, String name, String descriptor, ParamsInfo paramsInfo) {
    super(api, methodVisitor, access, name, descriptor, paramsInfo);

  }

  private void process() {
    debug_print_offline(
      String.format(
        "[DEBUG] [CmdStub]: %s", this.paramsInfo.toString()));

    mv.visitFrame(F_SAME1, 0, null, 0, null);

    putStubData("CMD", T_OBJECT, 0);

    debug_print_offline("CmdStub done");

    process1();
  }

  private void process1() {
    // reversed for test
    debug_print_online(T_OBJECT, method_idx);

    newInstance("java/util/ArrayList", params_idx);
    mv.visitLdcInsn(10);
    mv.visitVarInsn(ISTORE, tmp_obj);
    addListElement(params_idx, T_INT, tmp_obj);

    mv.visitLdcInsn(20);
    mv.visitVarInsn(ISTORE, tmp_obj);
    addListElement(params_idx, T_INT, tmp_obj);

    findAndExecute("SecAgent.utils.ReqInfo", "doTest", new Class[]{int.class, int.class}, reqinfo_idx, params_idx, res_idx);

//    findAndExecute("SecAgent.utils.ReqLocal", "getReqInfo", new Class[]{}, null_idx, null_idx, reqinfo_idx);
  }


  @Override
  protected void onMethodEnter() {
    super.onMethodEnter();
    System.out.println(String.format("stub into %s, params %d", paramsInfo, paramsInfo.getSize()));
  }

  @Override
  protected void onMethodExit(int opcode) {
    super.onMethodExit(opcode);
    process();
  }

  @Override
  public void visitEnd() {
    super.visitEnd();
  }


}
