package SecAgent.SecAsm.Common;


import SecAgent.SecAsm.utils.AsmLogger;
import SecAgent.SecAsm.utils.AsmReqInfoOp;
import SecAgent.SecAsm.utils.AsmReqLocalOp;
import SecAgent.utils.ParamsInfo;
import SecAgent.utils.ReqInfo;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;


public class CommonStub extends AdviceAdapter implements Opcodes {
  protected final static int T_OBJECT = 1111111;
  protected final int sb_idx = newLocal(Type.getType(StringBuilder.class));
  protected final int tmp_sb = newLocal(Type.getType(StringBuilder.class));
  protected final int tmp_arr = newLocal(Type.getType("[Ljava/lang/Object;"));
  protected final int tmp_len = newLocal(Type.getType(int.class));
  protected final int tmp_idx = newLocal(Type.getType(int.class));
  protected final int tmp_obj = newLocal(Type.getType(Object.class));

  // new Throwable()
  protected final int stk_idx = newLocal(Type.getType(Throwable.class));
  protected final int res_idx = newLocal(Type.getType(Object.class));
  protected final int reqinfo_idx = newLocal(Type.getType(ReqInfo.class));
  protected final ParamsInfo paramsInfo;

  // for invoke
  /**
   * for invoke (nonestatic)
   */
  protected final int inst_idx = newLocal(Type.getType(Object.class));
  protected final int cls_idx = newLocal(Type.getType(Class.class));
  protected final int method_idx = newLocal(Type.getType(Method.class));
  protected final int params_idx = newLocal(Type.getType(Object[].class));
  protected final int null_idx = newLocal(Type.getType(Object[].class));

  /**
   * Constructs a new {@link AdviceAdapter}.
   *
   * @param api           the ASM API version implemented by this visitor. Must be one of {@link
   *                      Opcodes#ASM4}, {@link Opcodes#ASM5}, {@link Opcodes#ASM6} or {@link Opcodes#ASM7}.
   * @param methodVisitor the method visitor to which this adapter delegates calls.
   * @param access        the method's access flags (see {@link Opcodes}).
   * @param name          the method's name.
   * @param descriptor    the method's descriptor (see {@link Type Type}).
   */
  public CommonStub(int api, MethodVisitor methodVisitor, int access, String name, String descriptor, ParamsInfo paramsInfo) {
    super(api, methodVisitor, access, name, descriptor);
    this.paramsInfo = paramsInfo;
  }

  protected void debug_print_offline(String msg) {
    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
    mv.visitLdcInsn(msg);
    mv.visitMethodInsn(
      INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
  }

  protected void debug_print_online(int type, int obj_idx) {
    String desc;
    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
    switch (type) {
      case T_BYTE:
      case T_SHORT:
      case T_INT:
        desc = "(I)V";
        mv.visitVarInsn(ILOAD, obj_idx);
        break;
      case T_BOOLEAN:
        desc = "(Z)V";
        mv.visitVarInsn(ILOAD, obj_idx);
        break;

      case T_CHAR:
        desc = "(C)V";
        mv.visitVarInsn(ILOAD, obj_idx);
        break;

      case T_LONG:
        desc = "(J)V";
        mv.visitVarInsn(LLOAD, obj_idx);
        break;

      case T_DOUBLE:
        desc = "(J)V";
        mv.visitVarInsn(DLOAD, obj_idx);
        break;

      case T_FLOAT:
        desc = "(J)V";
        mv.visitVarInsn(FLOAD, obj_idx);
        break;

      case T_OBJECT:
      default:
        desc = "(Ljava/lang/Object;)V";
        mv.visitVarInsn(ALOAD, obj_idx);
        break;
    }
    mv.visitMethodInsn(
      INVOKEVIRTUAL, "java/io/PrintStream", "println", desc, false);
  }

  /**
   * generate new instance of cls, and saved to target
   *
   * @param cls:    classname, ex: java/lang/StringBuilder
   * @param target: index of instance, ex: tmp_sb
   */
  protected void newInstance(String cls, int target) {
    mv.visitTypeInsn(NEW, cls);
    mv.visitInsn(DUP);
    mv.visitMethodInsn(INVOKESPECIAL, cls, "<init>", "()V", false);
    mv.visitVarInsn(ASTORE, target);
  }


  /**
   * new StringBuilder()
   *
   * @param sb_idx
   */
  protected void newStringBuilder(int sb_idx) {
//        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
//        mv.visitInsn(DUP);
//        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
//        mv.visitVarInsn(ASTORE, sb_idx);
    this.newInstance("java/lang/StringBuilder", sb_idx);
  }

  /**
   * sb.append(obj.toString());
   *
   * @param sb_idx:  index of StringBuilder's instance
   * @param obj_idx: index of target
   */
  protected void append(int sb_idx, int obj_idx) {
    mv.visitVarInsn(ALOAD, sb_idx);
    mv.visitVarInsn(ALOAD, obj_idx);
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;", false);
    mv.visitMethodInsn(
      INVOKEVIRTUAL,
      "java/lang/StringBuilder",
      "append",
      "(Ljava/lang/Object;)Ljava/lang/StringBuilder;",
      false);
    mv.visitInsn(POP);
  }

  /**
   * sb.append(str);
   *
   * @param sb_idx: index of StringBuilder's instance
   * @param sep:    index of target
   */
  protected void append(int sb_idx, String sep) {
    mv.visitVarInsn(ALOAD, sb_idx);
    mv.visitLdcInsn(sep);
    mv.visitMethodInsn(
      INVOKEVIRTUAL,
      "java/lang/StringBuilder",
      "append",
      "(Ljava/lang/Object;)Ljava/lang/StringBuilder;",
      false);
    mv.visitInsn(POP);
  }

  /**
   * sb.appent(o)
   *
   * @param type
   * @param obj_idx
   */
  @Deprecated
  protected void append(int sb_idx, int type, int obj_idx) {
    mv.visitVarInsn(ALOAD, sb_idx);

    String desc = "(Ljava/lang/Object;)Ljava/lang/StringBuilder;";
    switch (type) {
      case T_BYTE:
      case T_SHORT:
      case T_INT:
        desc = "(I)Ljava/lang/StringBuilder;";
        mv.visitVarInsn(ILOAD, obj_idx);
        break;

      case T_BOOLEAN:
        desc = "(Z)Ljava/lang/StringBuilder;";
        mv.visitVarInsn(ILOAD, obj_idx);
        break;

      case T_CHAR:
        desc = "(C)Ljava/lang/StringBuilder;";
        mv.visitVarInsn(ILOAD, obj_idx);
        break;

      case T_LONG:
        desc = "(J)Ljava/lang/StringBuilder;";
        mv.visitVarInsn(LLOAD, obj_idx);
        break;

      case T_DOUBLE:
        desc = "(J)Ljava/lang/StringBuilder;";
        mv.visitVarInsn(DLOAD, obj_idx);
        break;

      case T_FLOAT:
        desc = "(J)Ljava/lang/StringBuilder;";
        mv.visitVarInsn(FLOAD, obj_idx);
        break;

      case T_OBJECT:
      default:
        desc = "(Ljava/lang/Object;)Ljava/lang/StringBuilder;";
        mv.visitVarInsn(ALOAD, obj_idx);
        break;
    }

    mv.visitMethodInsn(
      INVOKEVIRTUAL, "java/lang/StringBuilder", "append", desc, false);
    mv.visitInsn(POP);
  }

  /**
   * sb.appent(o)
   *
   * @param type
   * @param obj_idx
   */
  @Deprecated
  protected void setValue(int tar_idx, int type, Object obj_idx) {
    mv.visitLdcInsn(obj_idx);
    switch (type) {
      case T_BYTE:
      case T_SHORT:
      case T_INT:
      case T_BOOLEAN:
      case T_CHAR:
        mv.visitVarInsn(ISTORE, tar_idx);
        break;

      case T_LONG:
        mv.visitVarInsn(LSTORE, tar_idx);
        break;

      case T_DOUBLE:
        mv.visitVarInsn(DSTORE, tar_idx);
        break;

      case T_FLOAT:
        mv.visitVarInsn(FSTORE, tar_idx);
        break;

      case T_OBJECT:
      default:
        mv.visitVarInsn(ASTORE, tar_idx);
        break;
    }

  }

  /**
   * dst = sb.toString();
   *
   * @param sb_idx
   * @param dst_idx
   */
  @Deprecated
  protected void toStr(int sb_idx, int dst_idx) {
    mv.visitVarInsn(ALOAD, sb_idx);
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
    mv.visitVarInsn(ASTORE, dst_idx);
  }


  /**
   * print trace stack
   */
  @Deprecated
  protected void stackTrack() {
    debug_print_offline(
      String.format(
        "[DEBUG] [SpringUrlStub]: %s", this.paramsInfo.toString()));

    // StackTraceElement[] tmp_arr = new Throwable().getStackTrace();
    mv.visitTypeInsn(NEW, "java/lang/Throwable");
    mv.visitInsn(DUP);
    mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Throwable", "<init>", "()V", false);
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Throwable", "getStackTrace", "()[Ljava/lang/StackTraceElement;", false);
    mv.visitVarInsn(ASTORE, tmp_arr);

    Label if_empty = new Label();
    mv.visitVarInsn(ALOAD, tmp_arr);
    mv.visitJumpInsn(IFNULL, if_empty);

    // StringBuilder sb = new StringBuilder();
    newStringBuilder(tmp_sb);

    // tmp_len = tmp_arr.length
    // tmp_idx = 0;
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

    // tmp_sb.append(tmp_obj.toString());
    append(tmp_sb, tmp_obj);
    append(tmp_sb, "\n");

    // loop
    mv.visitIincInsn(tmp_idx, 1);
    mv.visitJumpInsn(GOTO, loop_start);

    // loop done
    mv.visitLabel(loop_end);

    // do something else  (ex log, upload, print,..)
    debug_print_online(ALOAD, tmp_sb);

    mv.visitLabel(if_empty);
  }


  /**
   * use thread to dump current classloader
   */
  @Deprecated
  protected void classLoaderInfo() {
    // classloader info
    Label try_start = new Label();
    Label try_target = new Label();
    Label try_excep = new Label();

    mv.visitTryCatchBlock(try_start,  try_target, try_excep, "java/lang/ClassNotFoundException");
    mv.visitLabel(try_start);

    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false);
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Thread", "getContextClassLoader", "()Ljava/lang/ClassLoader;", false);
    mv.visitVarInsn(ASTORE, tmp_obj);
    debug_print_online(T_OBJECT, tmp_obj);

    mv.visitJumpInsn(GOTO, try_target);
    mv.visitLabel(try_excep);
    mv.visitFrame(F_SAME1, 0, null, 1, new Object[]{"java/lang/ClassNotFoundException"});
    mv.visitVarInsn(ASTORE, tmp_obj);
    debug_print_offline("exception:");
    mv.visitVarInsn(ALOAD, tmp_obj);
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/ClassNotFoundException", "printStackTrace", "()V", false);
    mv.visitLabel(try_target);

    mv.visitFrame(F_SAME, 0, null, 0, null);
  }

  /**
   * get ReqInfo from ThreadLocal
   *
   * @param reqinfo_idx
   */
  protected void getGlobalReqInfo(int reqinfo_idx) {
//    AsmReqLocalOp.getReqInfo(mv, reqinfo_idx);
    debug_print_offline("hello");
    findAndExecute("SecAgent.utils.ReqLocal", "getReqInfo", null, null_idx, null_idx, reqinfo_idx);
  }


  /**
   * save log data to ReqInfo
   * @param type
   * @param src_idx
   */
  protected void putStubData(String type, int src_type, int src_idx) {
    getGlobalReqInfo(reqinfo_idx);

    newInstance("java/util/ArrayList", params_idx);
    mv.visitLdcInsn(type);
    mv.visitVarInsn(ASTORE, tmp_obj);
    addListElement(params_idx, T_OBJECT, tmp_obj);
    newInstance("java/lang/Throwable", stk_idx);
    addListElement(params_idx, T_OBJECT, stk_idx);
    addListElement(params_idx, src_type, src_idx);

    findAndExecute("SecAgent.utils.ReqInfo", "putStubData", new Class[]{String.class, Throwable.class, Object.class}, reqinfo_idx, params_idx, res_idx);
  }

  /**
   * find method with paramTypes, and execute
   * @param classname: class'name, ex: java.lang.Math
   * @param methodname: method name
   * @param paramTypes: method parameters' type, int.class, Object.class...
   * @param inst_idx: instance of object
   * @param params_idx: params index of real stack, ArrayList
   * @param dst_idx: save result to dst_idx
   */
  protected void findAndExecute(String classname, String methodname, Class[] paramTypes, int inst_idx, int params_idx, int dst_idx) {
    debug_print_offline("findAndExecute 1");
    Label try_start = new Label();
    Label try_end = new Label();
    Label try_excep = new Label();

    mv.visitTryCatchBlock(try_start, try_end, try_excep, "java/lang/Exception");
    mv.visitLabel(try_start);
    loadClass(classname, cls_idx);
//    getDeclaredMethod(cls_idx, methodname, paramTypes, method_idx);
//    invoke(method_idx, inst_idx, params_idx, dst_idx);
    debug_print_offline("findAndExecute 1");

    mv.visitJumpInsn(GOTO, try_end);
    mv.visitLabel(try_excep);
    mv.visitFrame(F_SAME1, 0, null, 1, new Object[]{"java/lang/Exception"});
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Exception", "printStackTrace", "()V", false);

    mv.visitLabel(try_end);

  }

  /**
   * load class
   * @param classname
   * @param dst_idx
   */
  private void loadClass(String classname, int dst_idx) {
    mv.visitMethodInsn(INVOKESTATIC, "java/lang/Thread", "currentThread", "()Ljava/lang/Thread;", false);
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Thread", "getContextClassLoader", "()Ljava/lang/ClassLoader;", false);
    mv.visitLdcInsn(classname);
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/ClassLoader", "loadClass", "(Ljava/lang/String;)Ljava/lang/Class;", false);
    mv.visitVarInsn(ASTORE, dst_idx);
  }

  /**
   * getDeclaredMethod
   * @param cls_idx
   * @param methodname
   * @param paramTypes
   * @param dst_idx
   */
  private void getDeclaredMethod(int cls_idx, String methodname, Class[] paramTypes, int dst_idx) {
    mv.visitVarInsn(ALOAD, cls_idx);
    mv.visitLdcInsn(methodname);

    if (paramTypes == null || paramTypes.length == 0) {
      mv.visitInsn(ACONST_NULL);
    } else {
      mv.visitIntInsn(BIPUSH, paramTypes.length);
      mv.visitTypeInsn(ANEWARRAY, "java/lang/Class");

      for (int i = 0; i < paramTypes.length; i++) {
        mv.visitInsn(DUP);
        mv.visitIntInsn(BIPUSH, i);
        switch (paramTypes[i].getName()) {
          case "byte":
            mv.visitFieldInsn(GETSTATIC, "java/lang/Byte", "TYPE", "Ljava/lang/Class;");
            break;

          case "short":
            mv.visitFieldInsn(GETSTATIC, "java/lang/Short", "TYPE", "Ljava/lang/Class;");
            break;

          case "int":
            mv.visitFieldInsn(GETSTATIC, "java/lang/Integer", "TYPE", "Ljava/lang/Class;");
            break;

          case "boolean":
            mv.visitFieldInsn(GETSTATIC, "java/lang/Boolean", "TYPE", "Ljava/lang/Class;");
            break;

          case "char":
            mv.visitFieldInsn(GETSTATIC, "java/lang/Char", "TYPE", "Ljava/lang/Class;");
            break;

          case "long":
            mv.visitFieldInsn(GETSTATIC, "java/lang/Long", "TYPE", "Ljava/lang/Class;");
            break;

          case "double":
            mv.visitFieldInsn(GETSTATIC, "java/lang/Double", "TYPE", "Ljava/lang/Class;");
            break;

          case "float":
            mv.visitFieldInsn(GETSTATIC, "java/lang/Float", "TYPE", "Ljava/lang/Class;");
            break;

          default:
            mv.visitLdcInsn(Type.getType(paramTypes[i]));
            break;
        }

        //      mv.visitFieldInsn(GETSTATIC, paramTypes[i].getName().replace('.', '/'), "TYPE",
        // "Ljava/lang/Class;");
        mv.visitInsn(AASTORE);
      }
    }

    mv.visitVarInsn(ASTORE, tmp_obj);
    debug_print_online(T_OBJECT, tmp_obj);

    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Class", "getDeclaredMethod", "(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;", false);
    mv.visitVarInsn(ASTORE, dst_idx);
  }

  /**
   * invoke method
   * @param method_idx
   * @param inst_idx
   * @param params_idx
   * @param dst_idx
   */
  private void invoke(int method_idx, int inst_idx, int params_idx, int dst_idx) {

    mv.visitVarInsn(ALOAD, method_idx);
//    mv.visitInsn(ACONST_NULL);
    mv.visitVarInsn(ALOAD, inst_idx);

    Label if_null = new Label();
    Label if_body = new Label();
    Label if_end = new Label();

    // if params == null
    mv.visitVarInsn(ALOAD, params_idx);
    mv.visitJumpInsn(IFNULL, if_null);
    mv.visitJumpInsn(GOTO, if_body);

    // if params.length == 0
    mv.visitVarInsn(ALOAD, params_idx);
    mv.visitInsn(ARRAYLENGTH);
    mv.visitJumpInsn(IFEQ, if_null);

    mv.visitLabel(if_body);
    mv.visitVarInsn(ALOAD, params_idx);
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "toArray", "()[Ljava/lang/Object;", false);
    mv.visitJumpInsn(GOTO, if_end);

    mv.visitLabel(if_null);
    mv.visitInsn(ACONST_NULL);
    mv.visitLabel(if_end);

    mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/reflect/Method", "invoke", "(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;", false);
//    debug_print_online(T_OBJECT, method_idx);

//    mv.visitInsn(POP);
    mv.visitVarInsn(ASTORE, dst_idx);
  }


  protected void addListElement(int list_idx, int type,  int obj_idx) {
    mv.visitVarInsn(ALOAD, list_idx);

    switch (type) {
      case T_BYTE:
        mv.visitVarInsn(ILOAD, obj_idx);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Byte", "valueOf", "(B)Ljava/lang/Byte;", false);
        break;

      case T_SHORT:
        mv.visitVarInsn(ILOAD, obj_idx);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Short", "valueOf", "(S)Ljava/lang/Short;", false);
        break;

      case T_INT:
        mv.visitVarInsn(ILOAD, obj_idx);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Integer", "valueOf", "(I)Ljava/lang/Integer;", false);
        break;

      case T_BOOLEAN:
        mv.visitVarInsn(ILOAD, obj_idx);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Boolean", "valueOf", "(Z)Ljava/lang/Boolean;", false);
        break;

      case T_CHAR:
        mv.visitVarInsn(ILOAD, obj_idx);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Char", "valueOf", "(C)Ljava/lang/Char;", false);
        break;

      case T_LONG:
        mv.visitVarInsn(LLOAD, obj_idx);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Long", "valueOf", "(J)Ljava/lang/Long;", false);
        break;

      case T_DOUBLE:
        mv.visitVarInsn(DLOAD, obj_idx);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Double", "valueOf", "(D)Ljava/lang/Double;", false);
        break;

      case T_FLOAT:
        mv.visitVarInsn(FLOAD, obj_idx);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Float", "valueOf", "(F)Ljava/lang/Float;", false);
        break;

      case T_OBJECT:
      default:
        mv.visitVarInsn(ALOAD, obj_idx);
        break;
    }

    mv.visitMethodInsn(INVOKEVIRTUAL, "java/util/ArrayList", "add", "(Ljava/lang/Object;)Z", false);
    mv.visitInsn(POP);
}

  /**
   * @param reqinfo_idx
   * @param src_idx
   */
  protected void setHttpServletRequest(int reqinfo_idx, int src_idx) {
    AsmReqInfoOp.setHttpServletRequest(mv, reqinfo_idx, src_idx);
  }

  /**
   * Logger.info
   */
  protected void info(int obj_idx) {
    AsmLogger.info(mv, obj_idx);
  }

  /**
   * Logger.debug
   */
  protected void debug(int obj_idx) {
    AsmLogger.debug(mv, obj_idx);
  }

  /**
   * Logger.warn
   */
  protected void warn(int obj_idx) {
    AsmLogger.warn(mv, obj_idx);
  }

  /**
   * Logger.error
   */
  protected void error(int obj_idx) {
    AsmLogger.error(mv, obj_idx);
  }

  @Override
  protected void onMethodEnter() {
    super.onMethodEnter();

    if (Modifier.isStatic(this.paramsInfo.getAccess())) {
      debug_print_offline(
              "static method");
    }
  }
}
