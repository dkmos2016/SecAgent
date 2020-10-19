package SecAgent.Utils.SecAsm.Stub.Sql.Mybatis;

import SecAgent.Utils.utils.ParamsInfo;
import SecAgent.Utils.SecAsm.Common.CommonStub;
import org.objectweb.asm.MethodVisitor;

/** just log sql in xxMapper.xml, maybe with ?/# */
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
    mv.visitVarInsn(ALOAD, 0);
    mv.visitFieldInsn(
        GETFIELD, "org/apache/ibatis/scripting/xmltags/TextSqlNode", "text", "Ljava/lang/String;");
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "trim", "()Ljava/lang/String;", false);
    mv.visitVarInsn(ASTORE, res_idx);

    newArrayList(params_idx);
    addListElement(params_idx, T_OBJECT, 1);

    mv.visitLdcInsn("BEFORE");
    mv.visitVarInsn(ASTORE, tmp_obj);
    addListElement(params_idx, T_OBJECT, tmp_obj);

    addListElement(params_idx, T_OBJECT, res_idx);

    putStubData("MyBatis", T_OBJECT, params_idx);
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
