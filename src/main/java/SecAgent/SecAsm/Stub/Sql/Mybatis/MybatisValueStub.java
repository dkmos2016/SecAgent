package SecAgent.SecAsm.Stub.Sql.Mybatis;

import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.utils.ParamsInfo;
import org.objectweb.asm.MethodVisitor;

/** just log sql in xxMapper.xml, maybe with ?/# */
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
    mv.visitVarInsn(ASTORE, bak_obj);

    newArrayList(params_idx);
    addListElement(params_idx, T_OBJECT, 1);
    addListElement(params_idx, T_OBJECT, bak_obj);
    findAndGetInstance(
        "SecAgent.utils.Pair", new Class[] {Object.class, Object.class}, params_idx, res_idx);

    newArrayList(params_idx);
    mv.visitVarInsn(ALOAD, 0);
    mv.visitFieldInsn(
        GETFIELD,
        "org/apache/ibatis/scripting/xmltags/TextSqlNode$BindingTokenParser",
        "context",
        "Lorg/apache/ibatis/scripting/xmltags/DynamicContext;");
    mv.visitVarInsn(ASTORE, tmp_obj);

    addListElement(params_idx, T_OBJECT, tmp_obj);

    mv.visitLdcInsn("PARAMETER");
    mv.visitVarInsn(ASTORE, tmp_obj);
    addListElement(params_idx, T_OBJECT, tmp_obj);

    addListElement(params_idx, T_OBJECT, res_idx);

    putStubData("MYBATIS", T_OBJECT, params_idx);

    mv.visitVarInsn(ALOAD, bak_obj);
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
