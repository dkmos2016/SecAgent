package SecAgent.Container.Tomcat.Stub;

import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.Utils.utils.ParamsInfo;
import SecAgent.Utils.utils.ReqInfo.Protocol;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


/**
 *  instead with proxy
 *  log url
 */
@Deprecated
public class TomcatUrlStub extends CommonStub {
  private final int instream_idx = newLocal(Type.getType(InputStream.class));

  public TomcatUrlStub(
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
    mv.visitVarInsn(Opcodes.ALOAD, 1);
    mv.visitMethodInsn(
      Opcodes.INVOKEINTERFACE, "javax/servlet/http/HttpServletRequest", "getScheme", "()Ljava/lang/String;", true);
    mv.visitVarInsn(Opcodes.ASTORE, tmp_obj);

    append(tmp_sb, tmp_obj);
    append(tmp_sb, "://");

    // host
    mv.visitVarInsn(Opcodes.ALOAD, 1);
    mv.visitMethodInsn(
      Opcodes.INVOKEINTERFACE, "javax/servlet/http/HttpServletRequest", "getServerName", "()Ljava/lang/String;", true);
    mv.visitVarInsn(Opcodes.ASTORE, tmp_obj);

    append(tmp_sb, tmp_obj);
    append(tmp_sb, ":");

    // port
    mv.visitVarInsn(Opcodes.ALOAD, tmp_sb);
    mv.visitVarInsn(Opcodes.ALOAD, 1);
    mv.visitMethodInsn(
      Opcodes.INVOKEINTERFACE, "javax/servlet/http/HttpServletRequest", "getServerPort", "()I", true);
    mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;", false);
    mv.visitInsn(Opcodes.POP);

    // Uri
    mv.visitVarInsn(Opcodes.ALOAD, 1);
    mv.visitMethodInsn(
      Opcodes.INVOKEINTERFACE, "javax/servlet/http/HttpServletRequest", "getRequestURI", "()Ljava/lang/String;", true);

    mv.visitVarInsn(Opcodes.ASTORE, tmp_obj);
    append(tmp_sb, tmp_obj);

    toStr(tmp_sb, dst_idx);
  }

  private void getQueries(int dst_idx){
    mv.visitVarInsn(Opcodes.ALOAD, 1);
    mv.visitMethodInsn(
      Opcodes.INVOKEINTERFACE, "javax/servlet/http/HttpServletRequest", "getParameterMap", "()Ljava/util/Map;", true);
    mv.visitVarInsn(Opcodes.ASTORE, dst_idx);
  }

  private void getMethod(int dst_idx){
    mv.visitVarInsn(Opcodes.ALOAD, 1);
    mv.visitMethodInsn(
      Opcodes.INVOKEINTERFACE, "javax/servlet/http/HttpServletRequest", "getMethod", "()Ljava/lang/String;", true);
    mv.visitVarInsn(Opcodes.ASTORE, dst_idx);
  }

  @Deprecated
  private void getInputStream(int dst_idx){
    mv.visitVarInsn(Opcodes.ALOAD, 1);
    mv.visitMethodInsn(
      Opcodes.INVOKEINTERFACE, "javax/servlet/http/HttpServletRequest", "getInputStream", "()Ljavax/servlet/ServletInputStream;", true);
    mv.visitVarInsn(Opcodes.ASTORE, dst_idx);
  }

  private void getQueryString(int dst_idx) {
    mv.visitVarInsn(Opcodes.ALOAD, 1);
    mv.visitMethodInsn(
      Opcodes.INVOKEINTERFACE, "javax/servlet/http/HttpServletRequest", "getQueryString", "()Ljava/lang/String;", true);
    mv.visitVarInsn(Opcodes.ASTORE, dst_idx);
  }

  @Deprecated
  private void process_bak() {
    // prepare parameters
    // setHttpServletRequest
    newArrayList(params_idx);
    //    genFullUrl(tmp_obj);
    addListElement(params_idx, CommonStub.T_OBJECT, 1);

    findAndExecute(
      "SecAgent.utils.ReqInfo",
      "setHttpServletRequest",
      new Class[] {Map.class},
      reqinfo_idx,
      params_idx,
      tmp_obj);

    newArrayList(params_idx);
    //    genFullUrl(tmp_obj);
    addListElement(params_idx, CommonStub.T_OBJECT, 2);

    findAndExecute(
      "SecAgent.utils.ReqInfo",
      "setHttpServletResponse",
      new Class[] {Map.class},
      reqinfo_idx,
      params_idx,
      tmp_obj);
  }

  private void process() {
    newInstance(HashMap.class, res_idx);

    genFullUrl(tmp_obj);
    put(res_idx, "url", CommonStub.T_OBJECT, tmp_obj);

    getMethod(tmp_obj);
    put(res_idx, "method", CommonStub.T_OBJECT, tmp_obj);

    getQueryString(tmp_obj);
    put(res_idx, "queryString", CommonStub.T_OBJECT, tmp_obj);

    newArrayList(params_idx);
    addListElement(params_idx, CommonStub.T_OBJECT, res_idx);

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
    getQueries(tmp_obj);
    addListElement(params_idx, CommonStub.T_OBJECT, tmp_obj);
    findAndExecute(
      "SecAgent.utils.ReqInfo", "setQueries", new Class[] {Map.class}, reqinfo_idx, params_idx, tmp_obj);

    newArrayList(params_idx);
    mv.visitLdcInsn(Protocol.HTTP.getName());
    mv.visitVarInsn(Opcodes.ASTORE, tmp_obj);
    addListElement(params_idx, CommonStub.T_OBJECT, tmp_obj);

    findAndExecute(
      "SecAgent.utils.ReqInfo", "doJob", new Class[] {String.class}, reqinfo_idx, params_idx, tmp_obj);

    newArrayList(params_idx);
    findAndExecute(
      "SecAgent.utils.ReqLocal", "clear", new Class[] {}, reqinfo_idx, params_idx, tmp_obj);

    super.onMethodExit(opcode);
  }
}