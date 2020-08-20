package SecAsm.Common;


import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.commons.LocalVariablesSorter.*;
import utils.ParamsInfo;


public class CommonStub extends AdviceAdapter implements Opcodes {
    final int obj_idx = newLocal(Type.getType("[Ljava/lang/StackTraceElement;"));
    final int tmp_sb = newLocal(Type.getType(StringBuilder.class));
    final int tmp_arr = newLocal(Type.getType("[Ljava/lang/Object;"));
    final int tmp_len = newLocal(Type.getType(int.class));
    final int tmp_idx = newLocal(Type.getType(int.class));
    final int tmp_obj = newLocal(Type.getType(Object.class));

    public ParamsInfo paramsInfo;

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
    protected CommonStub(int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
        super(api, methodVisitor, access, name, descriptor);
    }


    public void debug_print_offline(String msg) {
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitLdcInsn(msg);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
    }

    public void debug_print_online(int opcode, int idx) {
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
        mv.visitVarInsn(opcode, idx);
        mv.visitMethodInsn(
                INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V", false);
    }

    public void newStringBuilder(int sb_idx) {
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
        mv.visitVarInsn(ASTORE, sb_idx);
    }

    public void append(int sb_idx, int obj_idx) {
        mv.visitVarInsn(ALOAD, sb_idx);
        mv.visitVarInsn(ALOAD, obj_idx);
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "java/lang/StringBuilder",
                "append",
                "(Ljava/lang/Object;)Ljava/lang/StringBuilder;",
                false);
        mv.visitInsn(POP);
    }

    /**
     * print trace stack
     */
    public void stackTrack() {
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
        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
        mv.visitVarInsn(ASTORE, tmp_sb);

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
        mv.visitVarInsn(ALOAD, tmp_sb);
        mv.visitVarInsn(ALOAD, tmp_obj);

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

        // do something else  (ex log, upload, print,..)
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");

        mv.visitVarInsn(ALOAD, tmp_sb);
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "java/lang/StringBuilder",
                "toString",
                "()Ljava/lang/String;",
                false);

        mv.visitMethodInsn(
                INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/Object;)V", false);

        mv.visitLabel(if_empty);
    }
}
