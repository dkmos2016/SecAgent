package SecAgent.SecAsm.Stub;

import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.utils.ParamsInfo;
import org.objectweb.asm.MethodVisitor;

public class SqlStub extends CommonStub {

  public SqlStub(
    int api, MethodVisitor methodVisitor, int access, String name, String descriptor, ParamsInfo paramsInfo) {
    super(api, methodVisitor, access, name, descriptor, paramsInfo);

  }

  private void process() {
    debug_print_offline(
      String.format(
        "[DEBUG] [SqlStub]: %s", this.paramsInfo.toString()));
    //    mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
    //    mv.visitInsn(DUP);
    //    mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
    //    mv.visitVarInsn(ASTORE, sb_idx);
    //
    //    mv.visitVarInsn(ALOAD, sb_idx);
    //    mv.visitVarInsn();
//    debug_print_online(ALOAD, 0);
    info(0);

    ReqTest4Sql();


  }

  @Override
  public void visitCode() {
    super.visitCode();
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