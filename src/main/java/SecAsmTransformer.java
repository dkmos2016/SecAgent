import SecAgent.SecAsm.Common.CommonAdapter;
import org.objectweb.asm.*;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class SecAsmTransformer implements ClassFileTransformer, Opcodes {

  @Override
  public byte[] transform(
      ClassLoader loader,
      String className,
      Class<?> classBeingRedefined,
      ProtectionDomain protectionDomain,
      byte[] classfileBuffer) {

    if (className.contains("ClassLoader")) {
      System.out.println(String.format("skipped %s", className));
      return classfileBuffer;
    }

    if (className.contains("HttpServletRequest")) {
      System.out.println("loaded HttpServletRequest: ");
      System.out.println(loader);
      System.out.println(className);
    }


    ClassReader cr = new ClassReader(classfileBuffer);
    ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
    //    ClassVisitor cv = new testClassVisitor(cw, className);
    ClassVisitor cv = new CommonAdapter(cw, className);
    cr.accept(cv, ClassReader.EXPAND_FRAMES);

    byte[] new_classfileBuffer = cw.toByteArray();

    return new_classfileBuffer;
  }
}
