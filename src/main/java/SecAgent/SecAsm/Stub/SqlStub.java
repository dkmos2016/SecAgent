package SecAgent.SecAsm.Stub;

import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.SecAsm.utils.AsmReqInfoOp;
import SecAgent.SecAsm.utils.AsmReqLocalOp;
import SecAgent.utils.ParamsInfo;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

public class SqlStub extends CommonStub {

  public SqlStub(
    int api, MethodVisitor methodVisitor, int access, String name, String descriptor, ParamsInfo paramsInfo) {
    super(api, methodVisitor, access, name, descriptor, paramsInfo);

  }

  private void process() {
    debug_print_offline(
      String.format(
        "[DEBUG] [SqlStub]: %s", this.paramsInfo.toString()));


    putStubData("SQL", T_OBJECT, 0);
  }

  @Override
  public void visitCode() {
    super.visitCode();
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