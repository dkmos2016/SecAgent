package SecAgent.SecAsm.Stub.Sql.Mybatis;

import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.utils.ParamsInfo;
import org.objectweb.asm.MethodVisitor;

/**
 * just log sql in xxMapper.xml, maybe with ?/#
 */
public class MybatisSqlBStub extends CommonStub {

  public MybatisSqlBStub(
    int api,
    MethodVisitor methodVisitor,
    int access,
    String name,
    String descriptor,
    ParamsInfo paramsInfo) {
    super(api, methodVisitor, access, name, descriptor, paramsInfo);
  }

  private void process() {
    debug_print_offline(String.format("[DEBUG] [MybatisSqlBStub]: %s", this.paramsInfo.toString()));

    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "trim", "()Ljava/lang/String;", false);
    mv.visitVarInsn(ASTORE, tmp_obj);

    debug_print_online(T_OBJECT, 0);
    debug_print_online(T_OBJECT, tmp_obj);

    newArrayList(params2_idx);
    addListElement(params2_idx, T_OBJECT, 0);
    addListElement(params2_idx, T_OBJECT, tmp_obj);

    addListElement(params2_idx, T_OBJECT, params2_idx);
    putStubData("MyBatis", T_OBJECT, params2_idx);


//    putStubData("SQL", T_OBJECT, 2);
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
}
