import SecAsm.Common.CommonAdapter;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class SecAsmTransformer implements ClassFileTransformer {

  @Override
  public byte[] transform(
      ClassLoader loader,
      String className,
      Class<?> classBeingRedefined,
      ProtectionDomain protectionDomain,
      byte[] classfileBuffer) {

    if (className.contains("ClassLoader")) {
      System.out.println(
              String.format("skipped %s", className)
      );
      return classfileBuffer;
    }

    ClassReader cr = new ClassReader(classfileBuffer);


    ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
//    ClassVisitor cv = new testClassVisitor(cw, className);
    ClassVisitor cv = new CommonAdapter(cw, className);
    cr.accept(cv, ClassReader.EXPAND_FRAMES);


    byte[] new_classfileBuffer = cw.toByteArray();

    // for len.test
//    if (className.equals("len/test") || className.equals("java/lang/ProcessImpl")) {
//      try {
//        System.out.println("write to test1.class");
//        File file = new File("test1.class");
//        FileOutputStream fileOutputStream = new FileOutputStream(file);
//        fileOutputStream.write(new_classfileBuffer);
//        fileOutputStream.close();
//        System.out.println("write to test1.class");
//      } catch (IOException e) {
//        Logger.getLogger(this.getClass()).info("IOException");
//        e.printStackTrace();
//      }
//    }
    return new_classfileBuffer;
  }
}
