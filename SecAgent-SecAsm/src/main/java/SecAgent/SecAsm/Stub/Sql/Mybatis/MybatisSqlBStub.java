package SecAgent.SecAsm.Stub.Sql.Mybatis;

import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.Utils.utils.ParamsInfo;
import org.objectweb.asm.MethodVisitor;

/** just log sql in xxMapper.xml, maybe with ?/# */
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
//    debug_print_offline("test");
    newArrayList(params_idx);
    addListElement(params_idx, T_OBJECT, 15);

    mv.visitLdcInsn("AFTER");
    mv.visitVarInsn(ASTORE, tmp_obj);
    addListElement(params_idx, T_OBJECT, tmp_obj);

    mv.visitVarInsn(ASTORE, bak_obj);
    mv.visitVarInsn(ALOAD, bak_obj);
    mv.visitMethodInsn(INVOKEVIRTUAL, "org/apache/ibatis/mapping/BoundSql", "getSql", "()Ljava/lang/String;", false);
    mv.visitVarInsn(ASTORE, tmp_obj);

    addListElement(params_idx, T_OBJECT, tmp_obj);

    putStubData("MyBatis", T_OBJECT, params_idx);

//    debug_print_online(T_OBJECT, params_idx);

    mv.visitVarInsn(ALOAD, bak_obj);
  }

  @Override
  protected void onMethodEnter() {
    super.onMethodEnter();
  }

  @Override
  protected void onMethodExit(int opcode) {
    super.onMethodExit(opcode);
    process();
    if(opcode == ARETURN) {
    }
  }
}
