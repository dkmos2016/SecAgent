package SecAgent.SecAsm.Stub.Sql.Mybatis;

import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.utils.ParamsInfo;
import org.objectweb.asm.MethodVisitor;

/**
 * just log sql in xxMapper.xml, maybe with ?/#
 */
public class MybatisSqlAStub extends CommonStub {

  public MybatisSqlAStub(
    int api,
    MethodVisitor methodVisitor,
    int access,
    String name,
    String descriptor,
    ParamsInfo paramsInfo) {
    super(api, methodVisitor, access, name, descriptor, paramsInfo);
  }

  private void process() {
    debug_print_offline(String.format("[DEBUG] [MybatisSqlAStub]: %s", this.paramsInfo.toString()));

    mv.visitVarInsn(ALOAD, 0);
//    mv.visitVarInsn(ASTORE, bak_obj);
//    debug_print_online(T_OBJECT, bak_obj);
    mv.visitFieldInsn(GETFIELD, "org/apache/ibatis/scripting/xmltags/TextSqlNode", "text", "Ljava/lang/String;");
//    mv.visitMethodInsn(INVOKEVIRTUAL, "org/apache/ibatis/scripting/xmltags/DynamicContext", "getSql", "()Ljava/lang/String;", false);
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "trim", "()Ljava/lang/String;", false);
    mv.visitVarInsn(ASTORE, res_idx);

//    newArrayList(params_idx);
//    addListElement(params_idx, T_OBJECT, 0);
//    addListElement(params_idx, T_OBJECT, bak_obj);
//    findAndGetInstance("SecAgent.utils.Pair", new Class[]{Object.class, Object.class}, params_idx, res_idx);

//    putStubData("SQL", T_OBJECT, 2);
    newArrayList(params2_idx);
    addListElement(params2_idx, T_OBJECT, 1);
    addListElement(params2_idx, T_OBJECT, res_idx);

    debug_print_online(T_OBJECT, 1);
    debug_print_online(T_OBJECT, res_idx);

    addListElement(params2_idx, T_OBJECT, params2_idx);
    putStubData("MyBatis", T_OBJECT, params2_idx);
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
}
