

import java.io.*;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

import SecAsm.Track.TrackAdapter;
import SecAsm.test.testClassVisitor;

import org.apache.log4j.Logger;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;


public class SecAsmTransformer implements ClassFileTransformer {
  @Override
  public byte[] transform(
      ClassLoader loader,
      String className,
      Class<?> classBeingRedefined,
      ProtectionDomain protectionDomain,
      byte[] classfileBuffer)
      throws IllegalClassFormatException {
      if (className.equals("len/test")) {
          ClassReader cr = new ClassReader(classfileBuffer);
          ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
          ClassVisitor cv = new TrackAdapter(cw, className);
          cr.accept(cv, ClassReader.EXPAND_FRAMES);

          byte[] new_classfileBuffer = cw.toByteArray();

          // for test
          try {
              System.out.println("write to test1.class");
              File file = new File("test1.class");
              FileOutputStream fileOutputStream = new FileOutputStream(file);
              fileOutputStream.write(new_classfileBuffer);
              fileOutputStream.close();
              System.out.println("write to test1.class");
          } catch (IOException e) {
              Logger.getLogger(this.getClass()).info("IOException");
              e.printStackTrace();
          }
          return new_classfileBuffer;
      } else {

//          try {
//              Class clazz = loader.loadClass(className);
//              if (clazz.isMemberClass() ) {
//                  return classfileBuffer;
//              }
//          } catch (ClassNotFoundException e) {
//              e.printStackTrace();
//          }

          ClassReader cr = new ClassReader(classfileBuffer);
          ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_FRAMES);
          ClassVisitor cv = new TrackAdapter(cw, className);
          cr.accept(cv, ClassReader.EXPAND_FRAMES);

          byte[] new_classfileBuffer = cw.toByteArray();
          return new_classfileBuffer;
      }
//    return new byte[0];
  }
}
