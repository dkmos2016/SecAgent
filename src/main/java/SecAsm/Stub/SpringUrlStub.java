package SecAsm.Stub;

import SecAsm.Common.CommonStub;
import org.objectweb.asm.MethodVisitor;
import SecAsm.utils.ParamsInfo;

public class SpringUrlStub extends CommonStub {
  public SpringUrlStub(
      int api,
      MethodVisitor methodVisitor,
      int access,
      String name,
      String descriptor,
      ParamsInfo paramsInfo) {
    super(api, methodVisitor, access, name, descriptor, paramsInfo);

  }


  private void process() {
    debug_print_offline(String.format("[DEBUG] [SpringUrlStub]: %s", this.paramsInfo.toString()));
    newStringBuilder(tmp_sb);

    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(
        INVOKEINTERFACE, "javax/servlet/http/HttpServletRequest", "getRequestURI", "()Ljava/lang/String;", true);

    mv.visitVarInsn(ASTORE, res_idx);

    append(tmp_sb, res_idx);

    debug_print_online(ALOAD, tmp_sb);

    stackTrack();


    ReqTest();
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
