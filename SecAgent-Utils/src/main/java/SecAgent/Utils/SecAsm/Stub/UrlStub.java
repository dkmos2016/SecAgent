package SecAgent.Utils.SecAsm.Stub;

import SecAgent.Utils.utils.ParamsInfo;
import SecAgent.Utils.SecAsm.Common.CommonStub;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


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

  private void genFullUrl(int dst_idx){
    newStringBuilder(tmp_sb);

    // scheme
    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(
      INVOKEINTERFACE, "javax/servlet/http/HttpServletRequest", "getScheme", "()Ljava/lang/String;", true);
    mv.visitVarInsn(ASTORE, tmp_obj);

    append(tmp_sb, tmp_obj);
    append(tmp_sb, "://");

    // host
    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(
      INVOKEINTERFACE, "javax/servlet/http/HttpServletRequest", "getServerName", "()Ljava/lang/String;", true);
    mv.visitVarInsn(ASTORE, tmp_obj);

    append(tmp_sb, tmp_obj);
    append(tmp_sb, ":");

    // port
    mv.visitVarInsn(ALOAD, tmp_sb);
    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(
      INVOKEINTERFACE, "javax/servlet/http/HttpServletRequest", "getServerPort", "()I", true);
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;", false);
    mv.visitInsn(POP);

    // Uri
    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(
      INVOKEINTERFACE, "javax/servlet/http/HttpServletRequest", "getRequestURI", "()Ljava/lang/String;", true);

    mv.visitVarInsn(ASTORE, tmp_obj);
    append(tmp_sb, tmp_obj);

    toStr(tmp_sb, dst_idx);
  }

  private void getQueries(int dst_idx){
    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(
      INVOKEINTERFACE, "javax/servlet/http/HttpServletRequest", "getParameterMap", "()Ljava/util/Map;", true);
    mv.visitVarInsn(ASTORE, dst_idx);
  }

  private void getMethod(int dst_idx){
    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(
      INVOKEINTERFACE, "javax/servlet/http/HttpServletRequest", "getMethod", "()Ljava/lang/String;", true);
    mv.visitVarInsn(ASTORE, dst_idx);
  }

  @Deprecated
  private void getInputStream(int dst_idx){
    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(
      INVOKEINTERFACE, "javax/servlet/http/HttpServletRequest", "getInputStream", "()Ljavax/servlet/ServletInputStream;", true);
    mv.visitVarInsn(ASTORE, dst_idx);
  }

  private void getQueryString(int dst_idx) {
    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(
      INVOKEINTERFACE, "javax/servlet/http/HttpServletRequest", "getQueryString", "()Ljava/lang/String;", true);
    mv.visitVarInsn(ASTORE, dst_idx);
  }

  @Deprecated
  private void process_bak() {
    // prepare parameters
    // setHttpServletRequest
    newArrayList(params_idx);
    //    genFullUrl(tmp_obj);
    addListElement(params_idx, T_OBJECT, 1);

    findAndExecute(
        "SecAgent.utils.ReqInfo",
        "setHttpServletRequest",
        new Class[] {Map.class},
        reqinfo_idx,
        params_idx,
        tmp_obj);

    newArrayList(params_idx);
    //    genFullUrl(tmp_obj);
    addListElement(params_idx, T_OBJECT, 2);

    findAndExecute(
        "SecAgent.utils.ReqInfo",
        "setHttpServletResponse",
        new Class[] {Map.class},
        reqinfo_idx,
        params_idx,
        tmp_obj);
  }

  private void process() {
    debug_print_offline("process2: ");
    newInstance(HashMap.class, res_idx);

    genFullUrl(tmp_obj);
    put(res_idx, "url", T_OBJECT, tmp_obj);

    getMethod(tmp_obj);
    put(res_idx, "method", T_OBJECT, tmp_obj);

    getQueryString(tmp_obj);
    put(res_idx, "queryString", T_OBJECT, tmp_obj);

    getQueries(tmp_obj);
    put(res_idx, "queries", T_OBJECT, tmp_obj);

    newArrayList(params_idx);
    addListElement(params_idx, T_OBJECT, res_idx);

    findAndExecute(
            "SecAgent.utils.ReqInfo",
            "setRequestInfo",
            new Class[] {Map.class},
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
    newArrayList(params_idx);
    findAndExecute(
        "SecAgent.utils.ReqInfo", "doJob", new Class[] {}, reqinfo_idx, params_idx, tmp_obj);

    findAndExecute(
        "SecAgent.utils.ReqLocal", "clear", new Class[] {}, reqinfo_idx, params_idx, tmp_obj);

    super.onMethodExit(opcode);
  }
}
