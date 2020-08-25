package SecAgent.SecAsm.Stub;

import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.utils.ParamsInfo;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.util.ArrayList;
import java.util.List;


/**
 * cannot use AsmReq*, use invoke instead
 * for CMD inject
 */
public class CmdStub extends CommonStub {
  int tid_idx = newLocal(Type.getType(long.class));

  public CmdStub(
    int api, MethodVisitor methodVisitor, int access, String name, String descriptor, ParamsInfo paramsInfo) {
    super(api, methodVisitor, access, name, descriptor, paramsInfo);

  }

  private void process() {
    debug_print_offline(
      String.format(
        "[DEBUG] [CmdStub]: %s", this.paramsInfo.toString()));

    // TODO fix bug (getGlobalReqInfo)

    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false);
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Thread", "getId", "()J", false);
    mv.visitVarInsn(LSTORE, tid_idx);
    debug_print_online(T_LONG, tid_idx);

    putStubData("CMD", 0);

    process1();
  }

  private void process1() {
   // reversed for test
    loadClass("SecAgent.utils.ReqInfo", cls_idx);
    getDeclaredMethod(cls_idx, "doTest", new Class[]{Object[].class}, method_idx);

    newInstance("java/util/ArrayList", params_idx);

    mv.visitLdcInsn(10);
    mv.visitVarInsn(ISTORE, tmp_idx);
    addListElement(params_idx, T_INT, tmp_idx);
//    mv.visitVarInsn(ALOAD, params_idx);
//    mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "toArray", "()[Ljava/lang/Object;", false);
//    mv.visitVarInsn(ASTORE, res_idx);
//    debug_print_online(T_OBJECT, res_idx);

    invokestatic(method_idx, params_idx, res_idx);
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
