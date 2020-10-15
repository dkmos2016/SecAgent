package SecAgent.SecAgent_Core.SecAsm.Stub;


import SecAgent.SecAgent_Core.SecAsm.Common.CommonStub;
import SecAgent.SecAgent_Core.utils.ParamsInfo;
import org.objectweb.asm.MethodVisitor;

/** cannot use AsmReq*, use invoke instead stub for upload file test */
public class UploadStub extends CommonStub {
  public UploadStub(
      int api,
      MethodVisitor methodVisitor,
      int access,
      String name,
      String descriptor,
      ParamsInfo paramsInfo) {
    super(api, methodVisitor, access, name, descriptor, paramsInfo);
  }

  private void process() {

    //    mv.visitVarInsn(ALOAD, 1);
    //    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/File", "getAbsolutePath",
    // "()Ljava/lang/String;", false);
    //    mv.visitVarInsn(ASTORE, res_idx);

    newArrayList(params2_idx);
    addListElement(params2_idx, T_OBJECT, 1);

    putStubData("UPLOAD", T_OBJECT, params2_idx);
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

  @Override
  public void visitEnd() {
    super.visitEnd();
  }
}
