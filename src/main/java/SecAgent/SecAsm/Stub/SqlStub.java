package SecAgent.SecAsm.Stub;

import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.SecAsm.utils.AsmReqInfoOp;
import SecAgent.SecAsm.utils.AsmReqLocalOp;
import SecAgent.utils.ParamsInfo;
import org.objectweb.asm.Label;
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

    AsmReqLocalOp.getReqInfo(mv, reqinfo_idx);

    newInstance("java/lang/Throwable", stk_idx);
    AsmReqInfoOp.putStubData(mv, reqinfo_idx, "SQL", stk_idx, 0);

//    AsmReqInfoOp.setType(mv, reqinfo_idx, "EXEC");
//    AsmReqInfoOp.setStubData(mv, reqinfo_idx, 0);

//    AsmReqInfoOp.toStr(mv, reqinfo_idx, res_idx);

//    info(res_idx);
//    AsmReqInfoOp.doJob(mv, reqinfo_idx);




//    stackTrack();

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