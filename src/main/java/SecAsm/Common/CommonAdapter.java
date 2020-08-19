package SecAsm.Common;

import SecAsm.Stub.SqlStub;
import SecAsm.Stub.CmdStub;
import SecAsm.Stub.TrackStub;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import utils.Config;
import utils.ParamsInfo;

public class CommonAdapter extends ClassVisitor implements Opcodes {
    private String CLASSNAME;

    public CommonAdapter(final ClassVisitor cv, final String name){
        super(Opcodes.ASM9, cv);
        CLASSNAME = name;

    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        ParamsInfo paramsInfo = new ParamsInfo(CLASSNAME, access, name, Type.getArgumentTypes(descriptor), descriptor, signature);

//        try {
//            File file = new File("./logxxx.txt");
//            FileOutputStream fout = new FileOutputStream(file, true);
//
//            fout.write(String.format("%s %s\n", CLASSNAME, name).getBytes());
//            fout.close();
//        } catch (FileNotFoundException e) {
//
//        } catch (IOException e) {
//
//        }

//        if (paramsInfo.toString().startsWith("java.lang.S")) {
//            System.out.println(paramsInfo.toString());
//        }
        switch (paramsInfo.toString()) {
            case Config.SQL_STUB:
                return new SqlStub(this.api,mv,access,name,descriptor,paramsInfo);

            case Config.EXEC_STUB:
                return new CmdStub(this.api,mv,access,name,descriptor,paramsInfo);

            default:
                if (Config.isIncludedMethod(paramsInfo.toString())) {
                    mv = new TrackStub(this.api, mv, access, name, descriptor, paramsInfo);
                }
        }
        return mv;

    }

}
