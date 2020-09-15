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

    if (className.contains("ServletRequestWrapper")) {
      System.out.println("-----------------------------------------------------------------------------------------------");
      Override(cw);
    }

    return new_classfileBuffer;
  }

  private void Override(ClassWriter cw) {
    // public InputStream in;
    cw.visitField(ACC_PUBLIC, "in", "Ljava/io/InputSteam;", null, null);

    MethodVisitor mv = null;

    {

      mv = cw.visitMethod(ACC_PUBLIC, "setInputSteam", "(Ljava/io/InputStream;)V", null, new String[]{"java/io/IOException"});
      mv.visitCode();
      mv.visitVarInsn(ALOAD, 0);
      mv.visitVarInsn(ALOAD, 1);
      mv.visitFieldInsn(PUTFIELD, "javax/servlet/ServletRequestWrapper", "in", "Ljava/io/InputStream;");
      mv.visitInsn(RETURN);

      mv.visitEnd();
    }

    {
      mv = cw.visitMethod(ACC_PUBLIC, "getInputStream", "()Ljava/io/InputStream;", null, new String[]{"java/io/IOException"});
      mv.visitCode();
      mv.visitVarInsn(ALOAD, 0);
      mv.visitFieldInsn(GETFIELD, "javax/servlet/ServletRequestWrapper", "in", "Ljava/io/InputStream;");

      Label if_not_null = new Label();
      mv.visitJumpInsn(IFNONNULL, if_not_null);

      mv.visitVarInsn(ALOAD, 0);
      mv.visitFieldInsn(GETFIELD, "javax/servlet/ServletRequestWrapper", "httpServletRequest", "Ljavax/servlet/http/HttpServletRequest;");
      mv.visitMethodInsn(INVOKEINTERFACE, "javax/servlet/http/HttpServletRequest", "getInputStream", "()Ljavax/servlet/ServletInputStream;", true);
      mv.visitInsn(ARETURN);

      mv.visitLabel(if_not_null);
      mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
      mv.visitVarInsn(ALOAD, 0);
      mv.visitFieldInsn(GETFIELD, "javax/servlet/ServletRequestWrapper", "in", "Ljava/io/InputStream;");
      mv.visitInsn(ARETURN);
      mv.visitEnd();
    }

    try {
      FileOutputStream tmp = new FileOutputStream("G:/test.class");
      tmp.write(cw.toByteArray());
      tmp.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

  }
}
