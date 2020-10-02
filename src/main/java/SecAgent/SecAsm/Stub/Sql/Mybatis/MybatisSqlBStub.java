package SecAgent.SecAsm.Stub.Sql.Mybatis;

import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.utils.ParamsInfo;
import org.objectweb.asm.MethodVisitor;

/**
 * just log sql in xxMapper.xml, maybe with ?/#
 */
public class MybatisSqlStub extends CommonStub {

  public MybatisSqlStub(
      int api,
      MethodVisitor methodVisitor,
      int access,
      String name,
      String descriptor,
      ParamsInfo paramsInfo) {
    super(api, methodVisitor, access, name, descriptor, paramsInfo);
  }

  private void process() {
    debug_print_offline(String.format("[DEBUG] [MybatisSqlStub]: %s", this.paramsInfo.toString()));
//    newArrayList(params2_idx);
//    addListElement(params2_idx, T_OBJECT, 2);
//    addListElement(params2_idx, T_OBJECT, 3);
//    addListElement(params2_idx, T_OBJECT, 4);

//    debug_print_online(T_OBJECT, 0);

//    mv.visitVarInsn(ALOAD, 0);
//    mv.visitFieldInsn(GETFIELD, "BoundSql", "sql", "Ljava/lang/String;");
    mv.visitVarInsn(ASTORE, tmp_obj);

    mv.visitVarInsn(ALOAD, tmp_obj);
    mv.visitMethodInsn(INVOKEVIRTUAL, "org/apache/ibatis/mapping/BoundSql", "getSql", "()Ljava/lang/String;", false);
//    mv.visitInsn(POP);
    mv.visitVarInsn(ASTORE, res_idx);
    debug_print_online(T_OBJECT, res_idx);

    mv.visitVarInsn(ALOAD, tmp_obj);

//    putStubData("SQL", T_OBJECT, 2);
  }

  @Override
  protected void onMethodEnter() {
    super.onMethodEnter();
  }


  @Override
  protected void onMethodExit(int opcode) {
    super.onMethodExit(opcode);

    process();
  }
}
