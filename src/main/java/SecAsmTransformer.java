import SecAgent.Conf.Config;
import SecAgent.Conf.StubConfig;
import SecAgent.SecAsm.Common.CommonAdapter;
import SecAgent.utils.AgentClassLoader;
import SecAgent.utils.DefaultLoggerHelper.DefaultLogger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Opcodes;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
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

    // fix
    ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES) {
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

    if (className.replace("/", ".").equals("org.apache.ibatis.mapping.MappedStatement")) {
      try {
        FileOutputStream fout = new FileOutputStream("/Users/len/test.class");
        fout.write(new_classfileBuffer);
        fout.close();
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      } catch (IOException e) {
        e.printStackTrace();
      }

    }

    return new_classfileBuffer;
  }
}
