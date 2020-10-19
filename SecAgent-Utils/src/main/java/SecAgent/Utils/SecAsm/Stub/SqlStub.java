package SecAgent.Utils.SecAsm.Stub;

import SecAgent.Utils.utils.ParamsInfo;
import SecAgent.Utils.SecAsm.Common.CommonStub;
import org.objectweb.asm.MethodVisitor;

public class SqlStub extends CommonStub {

  public SqlStub(
      int api,
      MethodVisitor methodVisitor,
      int access,
      String name,
      String descriptor,
      ParamsInfo paramsInfo) {
    super(api, methodVisitor, access, name, descriptor, paramsInfo);
  }

  private void process() {
    debug_print_offline(String.format("[DEBUG] [SqlStub]: %s", this.paramsInfo.toString()));

    putStubData("SQL", T_OBJECT, 0);

    debug_print_offline("SqlStub done");
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
