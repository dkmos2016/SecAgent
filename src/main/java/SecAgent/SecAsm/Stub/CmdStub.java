package SecAgent.SecAsm.Stub;

import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.utils.ParamsInfo;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

/**
 * process.start run in another thread, cannot use ThreadLocal
 */
public class CmdStub extends CommonStub {

  public CmdStub(
    int api, MethodVisitor methodVisitor, int access, String name, String descriptor, ParamsInfo paramsInfo) {
    super(api, methodVisitor, access, name, descriptor, paramsInfo);

  }

  private void process() {
    debug_print_offline(
      String.format(
        "[DEBUG] [CmdStub]: %s", this.paramsInfo.toString()));

    // TODO fix bug (getGlobalReqInfo)

//    getGlobalReqInfo(reqinfo_idx);
//    AsmReqLocalOp.getReqInfo(mv, reqinfo_idx);

//    AsmReqInfoOp.setStubDatas(mv, reqinfo_idx, 0);

//    AsmReqInfoOp.toStr(mv, reqinfo_idx, res_idx);
//
//    debug_print_online(T_OBJECT, res_idx);
  }

  private void process1() {
    debug_print_offline(
      String.format(
        "[DEBUG] [CmdStub]: %s", this.paramsInfo.toString()));
    // clear opcode stack
    Label if_empty = new Label();
    mv.visitVarInsn(ALOAD, 0);
    mv.visitJumpInsn(IFNULL, if_empty);

    // StringBuilder sb = new StringBuilder();
    newStringBuilder(tmp_sb);

    // tmp_arr = args[]
    // tmp_len = tmp_arr.length
    // tmp_idx = 0;
    mv.visitVarInsn(ALOAD, 0);
    mv.visitVarInsn(ASTORE, tmp_arr);
    mv.visitVarInsn(ALOAD, tmp_arr);
    mv.visitInsn(ARRAYLENGTH);
    mv.visitVarInsn(ISTORE, tmp_len);
    mv.visitInsn(ICONST_0);
    mv.visitVarInsn(ISTORE, tmp_idx);

    Label loop_start = new Label();
    Label loop_end = new Label();
    mv.visitLabel(loop_start);

    // if tmp_idx < tmp_len
    mv.visitVarInsn(ILOAD, tmp_idx);
    mv.visitVarInsn(ILOAD, tmp_len);
    mv.visitJumpInsn(IF_ICMPGE, loop_end);

    // tmp_obj = tmp_arr[tmp_idx]
    mv.visitVarInsn(ALOAD, tmp_arr);
    mv.visitVarInsn(ILOAD, tmp_idx);
    mv.visitInsn(AALOAD);
    mv.visitVarInsn(ASTORE, tmp_obj);

    append(tmp_sb, tmp_obj);
    append(tmp_sb, " ");

    // loop
    mv.visitIincInsn(tmp_idx, 1);
    mv.visitJumpInsn(GOTO, loop_start);
    mv.visitLabel(loop_end);

    // loop done
    // v = tmp_sb.toString()

    // do something else  (ex log, upload, print,..)

    toStr(tmp_sb, res_idx);
    debug_print_online(T_OBJECT, res_idx);

    mv.visitLabel(if_empty);
  }


  @Override
  protected void onMethodEnter() {
    super.onMethodEnter();
    System.out.println(String.format("stub into %s, params %d", paramsInfo, paramsInfo.getSize()));
  }

  @Override
  protected void onMethodExit(int opcode) {
    super.onMethodExit(opcode);
    process();

//        ReqTest();

  }

  @Override
  public void visitEnd() {
    super.visitEnd();
  }


}
