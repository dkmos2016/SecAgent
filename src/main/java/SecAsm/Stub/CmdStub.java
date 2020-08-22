package SecAsm.Stub;

import SecAsm.Common.CommonStub;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import SecAsm.utils.ParamsInfo;

/**
 * process.start run in another thread, cannot use ThreadLocal
 */
public class CmdStub extends CommonStub {

    public CmdStub(
            int api, MethodVisitor methodVisitor, int access, String name, String descriptor, ParamsInfo paramsInfo) {
        super(api, methodVisitor, access, name, descriptor, paramsInfo);

    }

    private void process() {
        debug_print_offline(
                String.format(
                        "[DEBUG] [CmdStub]: %s", this.paramsInfo.toString()));
        // clear opcode stack
        Label if_empty = new Label();
        mv.visitVarInsn(ALOAD, 0);
        mv.visitJumpInsn(IFNULL, if_empty);

        // StringBuilder sb = new StringBuilder();
        newStringBuilder(tmp_sb);

        // tmp_arr = args[]
        // tmp_len = tmp_arr.length
        // tmp_idx = 0;
        mv.visitVarInsn(ALOAD, 0);
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

        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "java/lang/StringBuilder",
                "append",
                "(Ljava/lang/String;)Ljava/lang/StringBuilder;",
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


    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        System.out.println(String.format("stub into %s, params %d", paramsInfo, paramsInfo.getSize()));
    }

    @Override
    protected void onMethodExit(int opcode) {
        super.onMethodExit(opcode);
        process();

//        ReqTest();

    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }


}
