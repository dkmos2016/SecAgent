package SecAsm.Common;


import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import utils.ParamsInfo;

import java.util.HashMap;


public class CommonStub extends AdviceAdapter implements Opcodes {
    protected final int sb_idx = newLocal(Type.getType(StringBuilder.class));
    protected final int tmp_sb = newLocal(Type.getType(StringBuilder.class));
    protected final int tmp_arr = newLocal(Type.getType("[Ljava/lang/Object;"));
    protected final int tmp_len = newLocal(Type.getType(int.class));
    protected final int tmp_idx = newLocal(Type.getType(int.class));
    protected final int tmp_obj = newLocal(Type.getType(Object.class));

    protected final int obj_idx = newLocal(Type.getType(Object.class));

    protected final int hmap_idx = newLocal(Type.getType(HashMap.class));

    protected final ParamsInfo paramsInfo;

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
    protected CommonStub(int api, MethodVisitor methodVisitor, int access, String name, String descriptor, ParamsInfo paramsInfo) {
        super(api, methodVisitor, access, name, descriptor);
        this.paramsInfo = paramsInfo;
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

    /**
     * generate new instance of cls, and saved to target
     * @param cls: classname, ex: java/lang/StringBuilder
     * @param target: index of instance, ex: tmp_sb
     */
    public void newInstance(String cls, int target) {
        mv.visitTypeInsn(NEW, cls);
        mv.visitInsn(DUP);
        mv.visitMethodInsn(INVOKESPECIAL, cls, "<init>", "()V", false);
        mv.visitVarInsn(ASTORE, target);
    }

    public void newHashMap(int obj_idx) {
//        mv.visitTypeInsn(NEW, "java/lang/HashMap");
//        mv.visitInsn(DUP);
//        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/HashMap", "<init>", "()V", false);
//        mv.visitVarInsn(ASTORE, obj_idx);
        newInstance("java/lang/HashMap", obj_idx);
    }

    /**
     * new StringBuilder()
     * @param sb_idx
     */
    public void newStringBuilder(int sb_idx) {
//        mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
//        mv.visitInsn(DUP);
//        mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
//        mv.visitVarInsn(ASTORE, sb_idx);
        this.newInstance("java/lang/StringBuilder", sb_idx);
    }

    /**
     * sb.append(obj);
     * @param sb_idx: index of StringBuilder's instance
     * @param obj_idx: index of target
     */
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
     * sb.append(obj);
     * @param sb_idx: index of StringBuilder's instance
     * @param sep: index of target
     */
    public void append(int sb_idx, String sep) {
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
}
