package SecAgent.SecAsm;

import SecAgent.Utils.Conf.Config;
import SecAgent.Utils.Conf.StubConfig;
import SecAgent.Utils.utils.AgentClassLoader;
import SecAgent.Utils.utils.DefaultLoggerHelper.DefaultLogger;
import SecAgent.SecAsm.Common.CommonAdapter;
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
    if (!StubConfig.isIncludedClass(className)) {
      return classfileBuffer;
    }

    ClassReader cr = new ClassReader(classfileBuffer);

    ClassWriter cw =
      new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES) {
        @Override
        protected ClassLoader getClassLoader() {
          return new AgentClassLoader(loader);
          //        return loader;
        }
      };
    //    ClassVisitor cv = new testClassVisitor(cw, className);
    ClassVisitor cv = new CommonAdapter(cw, className);
    byte[] new_classfileBuffer;
    try {
      cr.accept(cv, ClassReader.EXPAND_FRAMES);
      new_classfileBuffer = cw.toByteArray();

    } catch (Exception e) {
      //      e.printStackTrace();
      if (logger != null) logger.error(e);
      new_classfileBuffer = classfileBuffer;
    }

    return new_classfileBuffer;
  }
}
