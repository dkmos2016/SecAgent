package SecAsm.test;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class testMethodVisitor extends MethodVisitor implements Opcodes {


    public testMethodVisitor(int api) {
        super(api);
    }

    public testMethodVisitor(int api, MethodVisitor methodVisitor) {
        super(api, methodVisitor);
    }

    @Override
    public void visitCode() {
        super.visitCode();
        this.mv.visitParameter("msg", Opcodes.ACC_PUBLIC);
    }
}
