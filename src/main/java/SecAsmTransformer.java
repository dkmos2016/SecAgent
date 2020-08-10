
import com.sun.javafx.binding.StringFormatter;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class SecAsmTransformer implements ClassFileTransformer {
  @Override
  public byte[] transform(
      ClassLoader loader,
      String className,
      Class<?> classBeingRedefined,
      ProtectionDomain protectionDomain,
      byte[] classfileBuffer)
      throws IllegalClassFormatException {
      if (className.equals("test")) {

      }
    return new byte[0];
  }
}
