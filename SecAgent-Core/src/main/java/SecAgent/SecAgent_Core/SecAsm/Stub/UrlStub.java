package SecAgent.SecAgent_Core.SecAsm.Stub;


import SecAgent.SecAgent_Core.SecAsm.Common.CommonStub;
import SecAgent.SecAgent_Core.utils.ParamsInfo;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;


/** log url */
@SuppressWarnings("unused")
public class UrlStub extends CommonStub {
  private final int instream_idx = newLocal(Type.getType(InputStream.class));

  public UrlStub(
      int api,
      MethodVisitor methodVisitor,
      int access,
      String name,
      String descriptor,
      ParamsInfo paramsInfo) {
    super(api, methodVisitor, access, name, descriptor, paramsInfo);
  }

  private void process() {
    // prepare parameters
    // setHttpServletRequest
    newArrayList(params_idx);
    //    genFullUrl(tmp_obj);
    addListElement(params_idx, T_OBJECT, 1);

    findAndExecute(
        "SecAgent.SecAgent_Core.utils.ReqInfo",
        "setHttpServletRequest",
        new Class[] {HttpServletRequest.class},
        reqinfo_idx,
        params_idx,
        tmp_obj);

    newArrayList(params_idx);
    //    genFullUrl(tmp_obj);
    addListElement(params_idx, T_OBJECT, 2);

    findAndExecute(
        "SecAgent.SecAgent_Core.utils.ReqInfo",
        "setHttpServletResponse",
        new Class[] {HttpServletResponse.class},
        reqinfo_idx,
        params_idx,
        tmp_obj);
  }

  @Override
  protected void onMethodEnter() {
    super.onMethodEnter();

    process();
  }

  @Override
  protected void onMethodExit(int opcode) {
    super.onMethodExit(opcode);

    newArrayList(params_idx);
    findAndExecute(
        "SecAgent.SecAgent_Core.utils.ReqInfo", "doJob", new Class[] {}, reqinfo_idx, params_idx, tmp_obj);

    findAndExecute(
        "SecAgent.SecAgent_Core.utils.ReqLocal", "clear", new Class[] {}, reqinfo_idx, params_idx, tmp_obj);
  }

  @Override
  public void visitEnd() {
    super.visitEnd();
  }
}
