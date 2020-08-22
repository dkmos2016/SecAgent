package SecAgent.SecAsm.test;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class testClassVisitor extends ClassVisitor implements Opcodes {
  static protected String CLASSNAME;

  public testClassVisitor(final ClassVisitor cv, final String name) {
    super(Opcodes.ASM5, cv);
    CLASSNAME = name;
  }

  @Override
  public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
    MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
    System.out.println(String.format("%s - %s", CLASSNAME, name));
    return mv;
  }
}
