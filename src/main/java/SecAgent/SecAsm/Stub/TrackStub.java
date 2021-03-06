package SecAgent.SecAsm.Stub;

import SecAgent.utils.ParamsInfo;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;

import java.lang.reflect.Modifier;

/** origin asm bytecode */
public class TrackStub extends AdviceAdapter implements Opcodes {
  private static final int T_OBJECT = 1111111;
  private final int sb_idx = newLocal(Type.getType(StringBuilder.class));
  private final int tmp_sb = newLocal(Type.getType(StringBuilder.class));
  private final int tmp_arr = newLocal(Type.getType("[Ljava/lang/Object;"));
  private final int tmp_len = newLocal(Type.getType(int.class));
  private final int tmp_idx = newLocal(Type.getType(int.class));
  private final int tmp_obj = newLocal(Type.getType(Object.class));
  private ParamsInfo paramsInfo;

  public TrackStub(
      int api,
      MethodVisitor methodVisitor,
      int access,
      String name,
      String descriptor,
      ParamsInfo paramsInfo) {
    super(api, methodVisitor, access, name, descriptor);
    this.paramsInfo = paramsInfo;
  }

  private void append(int sb_idx, int obj_idx) {
    mv.visitVarInsn(ALOAD, sb_idx);
    mv.visitVarInsn(ALOAD, obj_idx);
    mv.visitMethodInsn(
        INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;", false);
    mv.visitMethodInsn(
        INVOKEVIRTUAL,
        "java/lang/StringBuilder",
        "append",
        "(Ljava/lang/Object;)Ljava/lang/StringBuilder;",
        false);
    mv.visitInsn(POP);
  }

  private void append(int sb_idx, String sep) {
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
   * @param cls
   * @param target
   */
  private void newInstance(String cls, int target) {
    mv.visitTypeInsn(NEW, cls);
    mv.visitInsn(DUP);
    mv.visitMethodInsn(INVOKESPECIAL, cls, "<init>", "()V", false);
    mv.visitVarInsn(ASTORE, target);
  }

  /** print trace stack */
  @Deprecated
  private void stackTrack() {
    // StackTraceElement[] tmp_arr = new Throwable().getStackTrace();
    mv.visitTypeInsn(NEW, "java/lang/Throwable");
    mv.visitInsn(DUP);
    mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Throwable", "<init>", "()V", false);
    mv.visitMethodInsn(
        INVOKEVIRTUAL,
        "java/lang/Throwable",
        "getStackTrace",
        "()[Ljava/lang/StackTraceElement;",
        false);
    mv.visitVarInsn(ASTORE, tmp_arr);

    Label if_empty = new Label();
    mv.visitVarInsn(ALOAD, tmp_arr);
    mv.visitJumpInsn(IFNULL, if_empty);

    // StringBuilder sb = new StringBuilder();
    newInstance("java/lang/StringBuilder", sb_idx);

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

    mv.visitLabel(if_empty);
  }

  private void debug_print_offline(String msg) {
    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
    mv.visitLdcInsn(msg);
    mv.visitMethodInsn(
        INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
  }

  private void debug_print_online(int type, int obj_idx) {
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
    mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "println", desc, false);
  }

  private void process() {
    int param_idx = 1;
    int size = paramsInfo.getSize() + 1;

    mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
    mv.visitInsn(DUP);
    mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
    mv.visitVarInsn(ASTORE, sb_idx);

    // fix init param_idx 0
    if (Modifier.isStatic(this.paramsInfo.getAccess())) {
      param_idx = 0;
      size -= 1;
    }

    mv.visitFrame(Opcodes.NEW, 10, new Object[] {}, 0, new Object[] {});

    //    System.out.println(
    //        String.format(
    //            "sb_idx: %d, tmp_sb: %d, tmp_arr: %d, tmp_len: %d, tmp_idx: %d, tmp_obj: %d",
    //            sb_idx, tmp_sb, tmp_arr, tmp_len, tmp_idx, tmp_obj));

    debug_print_offline("parameter in: ");
    for (Type type : this.paramsInfo.getIn_types()) {
      mv.visitVarInsn(ALOAD, sb_idx);
      String desc = String.format("(%s)Ljava/lang/StringBuilder;", type.toString());
      debug_print_offline(
          String.format(
              "[DEBUG] [TrackStub]: %s %s %d",
              this.paramsInfo.toString(), type.toString(), param_idx));

      switch (type.toString()) {
        case "B":
        case "S":
        case "I":
          desc = String.format("(I)Ljava/lang/StringBuilder;");
        case "C":
        case "Z":
          mv.visitVarInsn(ILOAD, param_idx);
          break;

        case "D":
          mv.visitVarInsn(DLOAD, param_idx);
          break;

        case "F":
          mv.visitVarInsn(FLOAD, param_idx);
          break;

        case "J":
          mv.visitVarInsn(LLOAD, param_idx);
          break;

        case "Ljava/lang/String;":
          mv.visitVarInsn(ALOAD, param_idx);
          break;

        default:
          //          System.out.println("[]default");
          desc = "(Ljava/lang/Object;)Ljava/lang/StringBuilder;";

          if (type.toString().startsWith("[")
              && !type.toString().startsWith("[[")
              && type.toString().endsWith(";")) {

            debug_print_offline(
                param_idx + " reformat " + type.toString() + "   " + this.paramsInfo.toString());

            // clear opcode stack
            Label if_empty = new Label();
            Label if_end = new Label();
            mv.visitVarInsn(ALOAD, param_idx);
            mv.visitJumpInsn(IFNULL, if_empty);
            mv.visitInsn(POP);

            // StringBuilder sb = new StringBuilder();
            mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
            mv.visitInsn(DUP);
            mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
            mv.visitVarInsn(ASTORE, tmp_sb);

            // tmp_arr = args[]
            // tmp_len = tmp_arr.length
            // tmp_idx = 0;
            mv.visitVarInsn(ALOAD, param_idx);
            mv.visitVarInsn(ASTORE, tmp_arr);
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
            mv.visitVarInsn(ALOAD, tmp_sb);
            mv.visitVarInsn(ALOAD, tmp_obj);

            // mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/Object", "toString",
            // "()Ljava/lang/String;", false);
            // mv.visitMethodInsn(INVOKEVIRTUAL,
            // type.getElementType().getClassName().replace(".", "/"), "toString",
            // "()Ljava/lang/String;", false);
            // mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
            // "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "java/lang/StringBuilder",
                "append",
                "(Ljava/lang/Object;)Ljava/lang/StringBuilder;",
                false);
            mv.visitLdcInsn(" ");
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "java/lang/StringBuilder",
                "append",
                "(Ljava/lang/Object;)Ljava/lang/StringBuilder;",
                false);
            mv.visitInsn(POP);

            // loop
            mv.visitIincInsn(tmp_idx, 1);
            mv.visitJumpInsn(GOTO, loop_start);
            mv.visitLabel(loop_end);

            // loop done
            // v = tmp_sb.toString()
            mv.visitVarInsn(ALOAD, sb_idx);
            mv.visitVarInsn(ALOAD, tmp_sb);
            mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "java/lang/StringBuilder",
                "toString",
                "()Ljava/lang/String;",
                false);
            //            mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append",
            // "(Ljava/lang/String;)Ljava/lang/StringBuilder;", false);

            // System.out.println(String.format("[DEBUG]: %s", type.toString()));
            mv.visitJumpInsn(GOTO, if_end);
            mv.visitLabel(if_empty);
            mv.visitVarInsn(ALOAD, param_idx);
            mv.visitLabel(if_end);
          } else {
            mv.visitVarInsn(ALOAD, param_idx);
          }

          break;
      }

      param_idx += type.getSize();

      // sb.append(v)
      mv.visitMethodInsn(INVOKEVIRTUAL, "java/lang/StringBuilder", "append", desc, false);
      mv.visitInsn(POP);

      if (param_idx < size) {
        // sb.append('|');
        mv.visitVarInsn(ALOAD, sb_idx);
        mv.visitIntInsn(BIPUSH, 124);
        mv.visitMethodInsn(
            INVOKEVIRTUAL,
            "java/lang/StringBuilder",
            "append",
            "(C)Ljava/lang/StringBuilder;",
            false);
        mv.visitInsn(POP);
      }
    }

    // System.out.println(sb.toString());
    mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
    mv.visitVarInsn(ALOAD, sb_idx);
    mv.visitMethodInsn(
        INVOKEVIRTUAL, "java/lang/StringBuilder", "toString", "()Ljava/lang/String;", false);
    mv.visitMethodInsn(
        INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
  }

  @Override
  protected void onMethodEnter() {
    super.onMethodEnter();
//    System.out.println(String.format("stub into %s, params %d", paramsInfo, paramsInfo.getSize()));
    debug_print_offline(String.format("[DEBUG] [TrackStub]: %s", this.paramsInfo.toString()));
  }

  @Override
  protected void onMethodExit(int opcode) {
    process();

    int type = 0;

    switch (opcode) {
      case ARETURN:
        type = T_OBJECT;
        mv.visitVarInsn(ASTORE, tmp_obj);
        mv.visitVarInsn(ALOAD, tmp_obj);
        break;
      case FRETURN:
        type = T_FLOAT;
        mv.visitVarInsn(FSTORE, tmp_obj);
        mv.visitVarInsn(FLOAD, tmp_obj);
        break;

      case DRETURN:
        type = T_DOUBLE;
        mv.visitVarInsn(DSTORE, tmp_obj);
        mv.visitVarInsn(DLOAD, tmp_obj);
        break;
      case LRETURN:
        type = T_LONG;
        mv.visitVarInsn(LSTORE, tmp_obj);
        mv.visitVarInsn(LLOAD, tmp_obj);
        break;
      case IRETURN:
        type = T_INT;
        mv.visitVarInsn(ISTORE, tmp_obj);
        mv.visitVarInsn(ILOAD, tmp_obj);
        break;

      case RETURN:
      default:
        break;
    }

    debug_print_offline("parameter out: ");
    debug_print_online(type, tmp_obj);

    super.onMethodExit(opcode);
  }

  @Override
  public void endMethod() {
    super.endMethod();
  }
}
