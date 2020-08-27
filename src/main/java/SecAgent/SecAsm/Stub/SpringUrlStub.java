package SecAgent.SecAsm.Stub;

import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.SecAsm.utils.AsmReqInfoOp;
import SecAgent.SecAsm.utils.AsmReqLocalOp;
import SecAgent.utils.ParamsInfo;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

/**
 * log url
 */
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
//    debug_print_tid();

    debug_print_offline(String.format("[DEBUG] [SpringUrlStub]: %s", this.paramsInfo.toString()));

    getGlobalReqInfo(reqinfo_idx);

//    debug_print_offline("invoke done");
    debug_print_online(T_OBJECT, reqinfo_idx);


    classLoaderInfo();
//     TODO fix bug

    setHttpServletRequest(reqinfo_idx, 1);

//    findAndExecute("SecAgent.utils.ReqInfo", "doTest", new Class[]{int.class, int.class}, reqinfo_idx, params_idx, res_idx);

    debug_print_offline("process done..");
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


//    AsmReqInfoOp.doJob(mv, reqinfo_idx);
    // TODO remove ThreadLocal
//    AsmReqLocalOp.clearReqInfo(mv);
  }

  @Override
  public void visitEnd() {
    super.visitEnd();
  }
}
