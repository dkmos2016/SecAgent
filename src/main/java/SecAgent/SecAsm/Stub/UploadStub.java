package SecAgent.SecAsm.Stub;


import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.utils.ParamsInfo;
import org.objectweb.asm.MethodVisitor;

/**
 * cannot use AsmReq*, use invoke instead
 * stub for upload file test
 */
public class UploadStub extends CommonStub {
  public UploadStub(
    int api, MethodVisitor methodVisitor, int access, String name, String descriptor, ParamsInfo paramsInfo) {
    super(api, methodVisitor, access, name, descriptor, paramsInfo);

  }


  private void process() {
    debug_print_offline(
      String.format(
        "[DEBUG] [UploadStub]: %s", this.paramsInfo.toString()));
    
    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/File", "getAbsolutePath", "()Ljava/lang/String;", false);
    mv.visitVarInsn(ASTORE, res_idx);

    putStubData("UPLOAD", res_idx);
  }

  @Override
  protected void onMethodEnter() {
    super.onMethodEnter();
    System.out.println(String.format("stub into %s, params %d", paramsInfo, paramsInfo.getSize()));
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
