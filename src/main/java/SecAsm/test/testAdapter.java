package SecAsm.test;


import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.AdviceAdapter;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.Method;


public class testAdapter extends AdviceAdapter implements Opcodes{
    static boolean inject = false;


    protected testAdapter(int api, MethodVisitor methodVisitor, int access, String name, String descriptor) {
        super(api, methodVisitor, access, name, descriptor);
    }


    @Override
    protected void onMethodEnter() {
        super.onMethodEnter();
        Type[] array = {Type.getType("S")};
        invokeStatic(Type.getType("Ljava/io/PrintStream;"), new Method("println",Type.getType("V"), array){

        });

    }
}

