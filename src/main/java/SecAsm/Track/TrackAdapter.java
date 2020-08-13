package SecAsm.Track;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import utils.Config;
import utils.ParamsInfo;

public class TrackAdapter extends ClassVisitor implements Opcodes {
  protected static String CLASSNAME;

  public TrackAdapter(final ClassVisitor cv, final String name) {
    super(Opcodes.ASM5, cv);
    CLASSNAME = name;
  }

  @Override
  public MethodVisitor visitMethod(
      int access, String name, String descriptor, String signature, String[] exceptions) {
    Type[] types = Type.getArgumentTypes(descriptor);
    MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);

    ParamsInfo paramsInfo = new ParamsInfo(CLASSNAME, access, name, types, descriptor, signature);

    if (!Config.isExcludedClass(CLASSNAME) && !Config.isExcludedMethod(name)) {
      mv = new TrackStub(this.api, mv, access, name, descriptor, paramsInfo);
    }

    //    mv = new TrackStub(this.api, mv, access, name, descriptor, paramsInfo);
    return mv;
  }
}
