package SecAgent.SecAsm.Stub.Sql;

import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.utils.ParamsInfo;
import org.objectweb.asm.MethodVisitor;

public class MybatisStub extends CommonStub {

  public MybatisStub(
      int api,
      MethodVisitor methodVisitor,
      int access,
      String name,
      String descriptor,
      ParamsInfo paramsInfo) {
    super(api, methodVisitor, access, name, descriptor, paramsInfo);
  }

  private void process() {
//    debug_print_offline(String.format("[DEBUG] [MybatisStub]: %s", this.paramsInfo.toString()));
    newArrayList(params2_idx);
    addListElement(params2_idx, T_OBJECT, 2);
    addListElement(params2_idx, T_OBJECT, 3);
    addListElement(params2_idx, T_OBJECT, 4);

//    debug_print_online(T_OBJECT, params2_idx);

    putStubData("SQL", T_OBJECT, params2_idx);
  }

  @Override
  protected void onMethodEnter() {
    super.onMethodEnter();
    process();
  }

  @Override
  protected void onMethodExit(int opcode) {

    //    mv.visitVarInsn(ALOAD, bak_obj);
    //    mv.visitVarInsn(ASTORE, 0);
    super.onMethodExit(opcode);
  }

  @Override
  public void visitEnd() {
    super.visitEnd();
  }
}
