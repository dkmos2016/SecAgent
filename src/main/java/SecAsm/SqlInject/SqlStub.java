package SecAsm.SqlInject;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

public class SqlStub extends AdviceAdapter implements Opcodes {
    protected SqlStub(int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
        super(api, methodVisitor, access, name, descriptor);
    }

    @Override
    public void visitCode() {
        super.visitCode();
    }

    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
    }

    @Override
    public void visitEnd() {
        super.visitEnd();
    }
}
