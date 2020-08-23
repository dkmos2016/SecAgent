package SecAgent.SecAsm.Stub;

import SecAgent.SecAsm.Common.CommonStub;
import SecAgent.SecAsm.utils.AsmReqLocalOp;
import SecAgent.utils.ParamsInfo;
import org.objectweb.asm.MethodVisitor;

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

//  private void genReqInfo() {
//
//  }

  @Deprecated
  private void genFullUrl(int dst_idx) {
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

  @Deprecated
  private void getMethod(int dst_idx) {
    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(
      INVOKEINTERFACE, "javax/servlet/http/HttpServletRequest", "getMethod", "()Ljava/lang/String;", true);
    mv.visitVarInsn(ASTORE, dst_idx);
  }

  @Deprecated
  private void getQuery(int dst_idx) {
    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(
      INVOKEINTERFACE, "javax/servlet/http/HttpServletRequest", "getQueryString", "()Ljava/lang/String;", true);
    mv.visitVarInsn(ASTORE, dst_idx);
  }

  @Deprecated
  private void getQueries(int dst_idx) {
    mv.visitVarInsn(ALOAD, 1);
    mv.visitMethodInsn(
      INVOKEINTERFACE, "javax/servlet/http/HttpServletRequest", "getParameterMap", "()Ljava/util/Map;", true);
    mv.visitVarInsn(ASTORE, dst_idx);
  }


  private void process() {
    debug_print_offline(String.format("[DEBUG] [SpringUrlStub]: %s", this.paramsInfo.toString()));

    getGlobalReqInfo(reqinfo_idx);
    setHttpServletRequest(reqinfo_idx, 1);
//    req2str(reqinfo_idx, res_idx);
//    info(res_idx);
//
//    stackTrack();
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

    // TODO remove ThreadLocal
    AsmReqLocalOp.clearReqInfo(mv);
  }

  @Override
  public void visitEnd() {
    super.visitEnd();
  }
}
