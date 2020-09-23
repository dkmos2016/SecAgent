import SecAgent.Conf.Config;
import SecAgent.SecAsm.Common.CommonAdapter;
import SecAgent.utils.DefaultLoggerHelper.DefaultLogger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.lang.instrument.ClassFileTransformer;
import java.security.ProtectionDomain;

public class SecAsmTransformer implements ClassFileTransformer, Opcodes {
  private static final DefaultLogger logger =
      DefaultLogger.getLogger(SecAsmTransformer.class, Config.EXCEPTION_PATH);

  @Override
  public byte[] transform(
      ClassLoader loader,
      String className,
      Class<?> classBeingRedefined,
      ProtectionDomain protectionDomain,
      byte[] classfileBuffer) {
    if (!Config.isIncludedClass(className)) {
      return classfileBuffer;
    }

    if (className.contains("ClassLoader")) {
      return classfileBuffer;
    }

    ClassReader cr = new ClassReader(classfileBuffer);
    ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
    //    ClassVisitor cv = new testClassVisitor(cw, className);
    ClassVisitor cv = new CommonAdapter(cw, className);
    try {
      cr.accept(cv, ClassReader.EXPAND_FRAMES);
    } catch (Exception e) {
      //      e.printStackTrace();
      if (logger != null) logger.error(e);
    }

    byte[] new_classfileBuffer = cw.toByteArray();

    return new_classfileBuffer;
  }
}
