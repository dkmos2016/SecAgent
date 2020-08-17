package SecAsm.Track;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import utils.ParamsInfo;

import java.lang.reflect.Modifier;

public class TrackStub extends AdviceAdapter implements Opcodes {
  ParamsInfo paramsInfo;

  protected TrackStub(
      int api,
      MethodVisitor methodVisitor,
      int access,
      String name,
      String descriptor,
      ParamsInfo paramsInfo) {
    super(api, methodVisitor, access, name, descriptor);
    this.paramsInfo = paramsInfo;
  }

  public static void output(int idx) {
    System.out.println(String.format("debug wiit idx-%d", idx));
  }

  @Override
  public void visitCode() {
    super.visitCode();
  }

  public void process() {

    int param_idx = 1;
    int size = paramsInfo.getSize()+1;

    int sb_idx = newLocal(Type.getType(StringBuilder.class));

    mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
    mv.visitInsn(DUP);
    mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
    mv.visitVarInsn(ASTORE, sb_idx);

    // fix init param_idx 0
    if (Modifier.isStatic(this.paramsInfo.getAccess())) {
      param_idx = 0;
      size -= 1;
    }

    for (Type type : this.paramsInfo.getIn_types()) {
      mv.visitVarInsn(ALOAD, sb_idx);
      String desc = String.format("(%s)Ljava/lang/StringBuilder;", type.toString());

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
          System.out.println("default");
          desc = "(Ljava/lang/String;)Ljava/lang/StringBuilder;";
          mv.visitVarInsn(ALOAD, param_idx);
          mv.visitMethodInsn(
              INVOKEVIRTUAL, "java/lang/Object", "toString", "()Ljava/lang/String;", false);
          break;
      }

      param_idx += type.getSize();

      // sb.append(v.toString())
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
    System.out.println(String.format("stub into %s, params %d", paramsInfo, paramsInfo.getSize()));
  }

  @Override
  protected void onMethodExit(int opcode) {
    process();

    super.onMethodExit(opcode);
  }

  @Override
  public void endMethod() {
    super.endMethod();
  }
}
