package SecAgent.SecAsm.Stub;

import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.utils.ParamsInfo;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.io.InputStream;
import java.util.Map;

/** log url */
public class UrlStub extends CommonStub {
  private int instream_idx = newLocal(Type.getType(InputStream.class));

  public UrlStub(
      int api,
      MethodVisitor methodVisitor,
      int access,
      String name,
      String descriptor,
      ParamsInfo paramsInfo) {
    super(api, methodVisitor, access, name, descriptor, paramsInfo);
  }

  private void genFullUrl(int dst_idx) {
    newStringBuilder(tmp_sb);

    // scheme
    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(
        INVOKEINTERFACE,
        "javax/servlet/http/HttpServletRequest",
        "getScheme",
        "()Ljava/lang/String;",
        true);
    mv.visitVarInsn(ASTORE, tmp_obj);

    append(tmp_sb, tmp_obj);
    append(tmp_sb, "://");

    // host
    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(
        INVOKEINTERFACE,
        "javax/servlet/http/HttpServletRequest",
        "getServerName",
        "()Ljava/lang/String;",
        true);
    mv.visitVarInsn(ASTORE, tmp_obj);

    append(tmp_sb, tmp_obj);
    append(tmp_sb, ":");

    // port
    mv.visitVarInsn(ALOAD, tmp_sb);
    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(
        INVOKEINTERFACE, "javax/servlet/http/HttpServletRequest", "getServerPort", "()I", true);
    mv.visitMethodInsn(
        INVOKEVIRTUAL, "java/lang/StringBuilder", "append", "(I)Ljava/lang/StringBuilder;", false);
    mv.visitInsn(POP);

    // Uri
    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(
        INVOKEINTERFACE,
        "javax/servlet/http/HttpServletRequest",
        "getRequestURI",
        "()Ljava/lang/String;",
        true);

    mv.visitVarInsn(ASTORE, tmp_obj);
    append(tmp_sb, tmp_obj);

    toStr(tmp_sb, dst_idx);
  }

  private void getQueries(int dst_idx) {
    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(
        INVOKEINTERFACE,
        "javax/servlet/http/HttpServletRequest",
        "getParameterMap",
        "()Ljava/util/Map;",
        true);
    mv.visitVarInsn(ASTORE, dst_idx);
  }

  private void getMethod(int dst_idx) {
    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(
        INVOKEINTERFACE,
        "javax/servlet/http/HttpServletRequest",
        "getMethod",
        "()Ljava/lang/String;",
        true);
    mv.visitVarInsn(ASTORE, dst_idx);
  }

  private void getInputStream(int dst_idx) {
    debug_print_offline("invoke getInputStream *******");

//    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(INVOKEINTERFACE, "javax/servlet/http/HttpServletRequest", "getInputStream", "()Ljavax/servlet/ServletInputStream;", true);
    mv.visitVarInsn(ASTORE, dst_idx);

//    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V", false);

    debug_print_online(T_OBJECT, dst_idx);
  }

  private void getQueryString(int dst_idx) {
    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(
        INVOKEINTERFACE,
        "javax/servlet/http/HttpServletRequest",
        "getQueryString",
        "()Ljava/lang/String;",
        true);
    mv.visitVarInsn(ASTORE, dst_idx);
  }

  private void process() {
    //    debug_print_tid();
    debug_print_offline(String.format("[DEBUG] [SpringUrlStub]: %s", this.paramsInfo.toString()));

    getGlobalReqInfo(reqinfo_idx);
    debug_print_online(T_OBJECT, reqinfo_idx);

    // prepare parameters
    // setUrl
    newArrayList(params_idx);
    genFullUrl(tmp_obj);
    addListElement(params_idx, T_OBJECT, tmp_obj);

    findAndExecute(
        "SecAgent.utils.ReqInfo",
        "setUrl",
        new Class[] {String.class},
        reqinfo_idx,
        params_idx,
        tmp_obj);

    // setQueries
    setNull(params_idx);
    newArrayList(params_idx);
    getQueries(tmp_obj);
    addListElement(params_idx, T_OBJECT, tmp_obj);

    findAndExecute(
        "SecAgent.utils.ReqInfo",
        "setQueries",
        new Class[] {Map.class},
        reqinfo_idx,
        params_idx,
        tmp_obj);

    // setMethod
    setNull(params_idx);
    newArrayList(params_idx);
    getMethod(tmp_obj);
    addListElement(params_idx, T_OBJECT, tmp_obj);

    findAndExecute(
        "SecAgent.utils.ReqInfo",
        "setMethod",
        new Class[] {String.class},
        reqinfo_idx,
        params_idx,
        tmp_obj);

    // setQueryString
    setNull(params_idx);
    newArrayList(params_idx);
    getQueryString(tmp_obj);
    addListElement(params_idx, T_OBJECT, tmp_obj);

    findAndExecute(
        "SecAgent.utils.ReqInfo",
        "setQueryString",
        new Class[] {String.class},
        reqinfo_idx,
        params_idx,
        tmp_obj);

    // setInputStream
//    setNull(params_idx);
//    newArrayList(params_idx);
//    getInputStream(instream_idx);
//    addListElement(params_idx, T_OBJECT, instream_idx);
//
//    findAndExecute(
//        "SecAgent.utils.ReqInfo",
//        "setInputStream",
//        new Class[] {InputStream.class},
//        reqinfo_idx,
//        params_idx,
//        tmp_obj);

    setNull(params_idx);

//    newArrayList(params_idx);
//    getQueryString(tmp_obj);
//    addListElement(params_idx, T_OBJECT, tmp_obj);
//
//    findAndExecute(
//        "SecAgent.utils.ReqInfo",
//        "setQueryString",
//        new Class[] {String.class},
//        reqinfo_idx,
//        params_idx,
//        tmp_obj);
//
//    setNull(params_idx);
    //    debug_print_online(T_OBJECT,  tmp_obj);

    //    findAndExecute("SecAgent.utils.ReqInfo", "doTest", new Class[]{int.class, int.class},
    // reqinfo_idx, params_idx, res_idx);

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

    newArrayList(params_idx);
    findAndExecute(
        "SecAgent.utils.ReqInfo", "doJob", new Class[] {}, reqinfo_idx, params_idx, tmp_obj);
    // TODO remove ThreadLocal
    //    AsmReqLocalOp.clearReqInfo(mv);

    findAndExecute(
        "SecAgent.utils.ReqLocal", "clear", new Class[] {}, reqinfo_idx, params_idx, tmp_obj);
  }

  @Override
  public void visitEnd() {
    super.visitEnd();
  }
}
