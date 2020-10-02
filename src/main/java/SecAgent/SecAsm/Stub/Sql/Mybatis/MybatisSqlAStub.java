package SecAgent.SecAsm.Stub.Sql.Mybatis;

import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.utils.ParamsInfo;
import org.objectweb.asm.MethodVisitor;

/**
 * just log sql in xxMapper.xml, maybe with ?/#
 */
public class MybatisValueStub extends CommonStub {

  public MybatisValueStub(
      int api,
      MethodVisitor methodVisitor,
      int access,
      String name,
      String descriptor,
      ParamsInfo paramsInfo) {
    super(api, methodVisitor, access, name, descriptor, paramsInfo);
  }

  private void process() {
    debug_print_offline(String.format("[DEBUG] [MybatisValueStub]: %s", this.paramsInfo.toString()));

    mv.visitVarInsn(ASTORE, bak_obj);
    debug_print_online(T_OBJECT, 0);
    debug_print_online(T_OBJECT, bak_obj);

    newArrayList(params_idx);
    addListElement(params_idx, T_OBJECT, 0);
    addListElement(params_idx, T_OBJECT, bak_obj);
    findAndGetInstance("SecAgent.utils.Pair", new Class[]{Object.class, Object.class}, params_idx, res_idx);

    mv.visitVarInsn(ALOAD, bak_obj);

//    putStubData("SQL", T_OBJECT, 2);
  }

  @Override
  protected void onMethodEnter() {
    super.onMethodEnter();
  }

  @Override
  protected void onMethodExit(int opcode) {

    //    mv.visitVarInsn(ALOAD, bak_obj);
    //    mv.visitVarInsn(ASTORE, 0);

    super.onMethodExit(opcode);
    process();
  }

  @Override
  public void visitEnd() {
    super.visitEnd();
  }
}
