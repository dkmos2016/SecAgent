package SecAgent.SecAsm.Stub;

import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.utils.ParamsInfo;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.HashMap;


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

  private void process() {
    // prepare parameters
    // setHttpServletRequest
    newArrayList(params_idx);
    //    genFullUrl(tmp_obj);
    addListElement(params_idx, T_OBJECT, 1);

    findAndExecute(
        "SecAgent.utils.ReqInfo",
        "setHttpServletRequest",
        new Class[] {HttpServletRequest.class},
        reqinfo_idx,
        params_idx,
        tmp_obj);

    newArrayList(params_idx);
    //    genFullUrl(tmp_obj);
    addListElement(params_idx, T_OBJECT, 2);

    findAndExecute(
        "SecAgent.utils.ReqInfo",
        "setHttpServletResponse",
        new Class[] {HttpServletResponse.class},
        reqinfo_idx,
        params_idx,
        tmp_obj);
  }

  private void process2() {
    debug_print_offline("process2: ");
    newInstance(HashMap.class, res_idx);

    genFullUrl(tmp_obj);

    put(res_idx, "url", T_OBJECT, tmp_obj);

    debug_print_online(T_OBJECT, tmp_obj);
  }

  @Override
  protected void onMethodEnter() {
    super.onMethodEnter();

//    process();
    process2();
  }

  @Override
  protected void onMethodExit(int opcode) {
    super.onMethodExit(opcode);

    newArrayList(params_idx);
    findAndExecute(
        "SecAgent.utils.ReqInfo", "doJob", new Class[] {}, reqinfo_idx, params_idx, tmp_obj);

    findAndExecute(
        "SecAgent.utils.ReqLocal", "clear", new Class[] {}, reqinfo_idx, params_idx, tmp_obj);
  }

  @Override
  public void visitEnd() {
    super.visitEnd();
  }
}
