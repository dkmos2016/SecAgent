package SecAgent.SecAgent_Core.SecAsm.Stub.Sql.Mybatis;


import SecAgent.SecAgent_Core.SecAsm.Common.CommonStub;
import SecAgent.SecAgent_Core.utils.ParamsInfo;
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
    newArrayList(params2_idx);
    addListElement(params2_idx, T_OBJECT, 0);

    mv.visitLdcInsn("AFTER");
    mv.visitVarInsn(ASTORE, tmp_obj);
    addListElement(params2_idx, T_OBJECT, tmp_obj);

    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/String", "trim", "()Ljava/lang/String;", false);
    mv.visitVarInsn(ASTORE, tmp_obj);

    addListElement(params2_idx, T_OBJECT, tmp_obj);

    putStubData("MyBatis", T_OBJECT, params2_idx);
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
