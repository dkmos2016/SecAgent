package SecAsm.SqlInject;


import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class SqlAdapter extends ClassVisitor implements Opcodes {
    static protected String CLASSNAME;

    public SqlAdapter(final ClassVisitor cv, final String name){
        super(Opcodes.ASM5, cv);
        CLASSNAME = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        // check classname and method name

        if (CLASSNAME.equals("")){
            return new SqlStub(this.api, mv, access, name, descriptor);
        }
        return mv;
    }

}
