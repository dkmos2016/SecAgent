import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;


public class ClassLogger implements ClassFileTransformer {
  @Override
  public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
    if (className.equals("test")) {
      System.out.println(String.format("Process by ClassFileTransformer,target class = %s", className));
    }
//        System.out.println(String.format("Process by ClassFileTransformer,target class = %s", className));

//        return new byte[0];

    return classfileBuffer;
  }
}
