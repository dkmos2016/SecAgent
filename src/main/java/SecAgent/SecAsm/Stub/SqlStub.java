package SecAgent.SecAsm.Stub;

import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.utils.ParamsInfo;
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
    putStubData("SQL", T_OBJECT, 2);
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
