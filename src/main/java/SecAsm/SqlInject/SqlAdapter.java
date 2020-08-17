package SecAsm.SqlInject;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import utils.ParamsInfo;


public class SqlAdapter extends ClassVisitor implements Opcodes  {
    protected static String CLASSNAME;

    public SqlAdapter(final ClassVisitor cv, final String name) {
        super(Opcodes.ASM5, cv);
        CLASSNAME = name;
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        ParamsInfo paramsInfo = new ParamsInfo(CLASSNAME, access, name, Type.getArgumentTypes(descriptor), descriptor, signature);

        return new SqlStub(this.api, mv, access, name, descriptor, paramsInfo);
    }

}
