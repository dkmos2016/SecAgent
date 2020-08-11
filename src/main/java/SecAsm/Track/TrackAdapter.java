package SecAsm.Track;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class TrackAdapter extends ClassVisitor implements Opcodes {
    static protected String CLASSNAME;

    public TrackAdapter(final ClassVisitor cv, final String name) {
        super(Opcodes.ASM5, cv);
        CLASSNAME = name;
    }


    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        String output;
        StringBuilder sb = new StringBuilder();
        sb.append("-------------------------------------------\n");
        if (!name.contains("init") && CLASSNAME.contains("mysql"))
        {sb.append(String.format( "%s %s\n", CLASSNAME, name));}
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);

        Type[] types = Type.getArgumentTypes(descriptor);
        for (Type type: types){
            sb.append(type.toString()+"\n");
        }

        sb.append("-------------------------------------------\n\n");
        if (types.length == 0) {
            output = "";
        } else {
            output = sb.toString();
        }
        System.out.print(output);
        return mv;
    }
}
