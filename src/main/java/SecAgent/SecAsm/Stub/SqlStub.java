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

//    newArrayList(params_idx);
//    addListElement(params_idx, T_OBJECT, 0);
//
//    findAndGetInstance("SecAgent.Filter.SecInstanceProxyFactory", new Class[]{Object.class}, params_idx, inst_idx);
//
//    newArrayList(params_idx);
//    findAndExecute("SecAgent.Filter.SecInstanceProxyFactory", "getProxyInstance", new Class[]{}, inst_idx, params_idx, tmp_obj);
//
//    mv.visitVarInsn(ALOAD, 0);
//    mv.visitVarInsn(ASTORE, bak_obj);
//
//    mv.visitVarInsn(ALOAD, tmp_obj);
//    mv.visitTypeInsn(CHECKCAST, "org/apache/ibatis/mapping/BoundSql");
//    mv.visitVarInsn(ASTORE, 0);

    findAndGetSecProxyInstance(0, inst_idx);

    mv.visitVarInsn(ALOAD, inst_idx);
    mv.visitTypeInsn(CHECKCAST, "org/apache/ibatis/mapping/BoundSql");
    mv.visitVarInsn(ASTORE, 0);

//    debug_print_online(T_OBJECT, inst_idx);

    putStubData("SQL", T_OBJECT, 2);

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
