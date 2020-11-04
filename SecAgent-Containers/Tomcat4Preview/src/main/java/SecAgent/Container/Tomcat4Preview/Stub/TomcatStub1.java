package SecAgent.Container.Tomcat4Preview.Stub;

import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.Utils.utils.ParamsInfo;
import org.objectweb.asm.MethodVisitor;
import javax.servlet.http.HttpServlet;

// for request.getInputSteam;
public class TomcatStub1 extends CommonStub {

  public TomcatStub1(
    int api,
    MethodVisitor methodVisitor,
    int access,
    String name,
    String descriptor,
    ParamsInfo paramsInfo) {
    super(api, methodVisitor, access, name, descriptor, paramsInfo);
  }

  private void process() {
    debug_print_offline(String.format("[DEBUG] [TomcatStub1]: %s", this.paramsInfo.toString()));

    findAndGetSecProxyInstance(1, inst_idx, "SecAgent.Container.Tomcat4Preview.Filter.SecInstanceProxyFactory");
    mv.visitVarInsn(ALOAD, inst_idx);
    mv.visitVarInsn(ASTORE, 1);
  }

  private void process2() {
    mv.visitVarInsn(ALOAD, 1);
    mv.visitVarInsn(ASTORE, bak_obj);

    newArrayList(params_idx);
    addListElement(params_idx, T_OBJECT, 1);

    findAndExecute("SecAgent.Utils.utils.Common", "getTomcatProxyFactory", new Class[]{Object.class}, null_idx, params_idx, inst_idx);
    replaceIfNotNull(T_OBJECT, 1, bak_obj, inst_idx);

  }

  @Override
  protected void onMethodEnter() {
    super.onMethodEnter();

    process2();
  }

  @Override
  protected void onMethodExit(int opcode) {
    super.onMethodExit(opcode);
  }
}
