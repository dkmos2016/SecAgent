package SecAsm.Stub;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.AdviceAdapter;
import utils.ParamsInfo;

public class SqlStub extends AdviceAdapter implements Opcodes {
    final int sb_idx = newLocal(Type.getType(StringBuilder.class));
    final int tmp_sb = newLocal(Type.getType(StringBuilder.class));
    final int tmp_arr = newLocal(Type.getType("[Ljava/lang/Object;"));
    final int tmp_len = newLocal(Type.getType(int.class));
    final int tmp_idx = newLocal(Type.getType(int.class));
    final int tmp_obj = newLocal(Type.getType(Object.class));
    ParamsInfo paramsInfo;

    public SqlStub(
            int api, MethodVisitor methodVisitor, int access, String name, String descriptor, ParamsInfo paramsInfo) {
        super(api, methodVisitor, access, name, descriptor);

        this.paramsInfo = paramsInfo;

        System.out.println(String.format("stub into %s, params %d", paramsInfo, paramsInfo.getSize()));

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

    private void process() {
        //    mv.visitTypeInsn(NEW, "java/lang/StringBuilder");
        //    mv.visitInsn(DUP);
        //    mv.visitMethodInsn(INVOKESPECIAL, "java/lang/StringBuilder", "<init>", "()V", false);
        //    mv.visitVarInsn(ASTORE, sb_idx);
        //
        //    mv.visitVarInsn(ALOAD, sb_idx);
        //    mv.visitVarInsn();
        debug_print_online(ALOAD, 0);
    }

    @Override
    public void visitCode() {
        super.visitCode();
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        process();
    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}