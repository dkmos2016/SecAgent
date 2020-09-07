package SecAgent.SecAsm.Common;

import SecAgent.Conf.Config;
import SecAgent.SecAsm.Stub.*;
import SecAgent.utils.ParamsInfo;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class CommonAdapter extends ClassVisitor implements Opcodes {
  private final String CLASSNAME;

  public CommonAdapter(final ClassVisitor cv, final String name) {
    super(Opcodes.ASM9, cv);
    CLASSNAME = name;
  }

  @Override
  public MethodVisitor visitMethod(
      int access, String name, String descriptor, String signature, String[] exceptions) {
    MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
    ParamsInfo paramsInfo =
        new ParamsInfo(
            CLASSNAME, access, name, Type.getArgumentTypes(descriptor), descriptor, signature);

    switch (paramsInfo.toString()) {
//      case Config.SQL_STUB:
//        return new SqlStub(this.api, mv, access, name, descriptor, paramsInfo);

      case Config.EXEC_STUB:
        return new CmdStub(this.api, mv, access, name, descriptor, paramsInfo);

      case Config.DOWN_STUB:
        return new DownStub(this.api, mv, access, name, descriptor, paramsInfo);

      case Config.UPLOAD_STUB:
        return new UploadStub(this.api, mv, access, name, descriptor, paramsInfo);

      case Config.SPRING_URL_STUB:
        return new UrlStub(this.api, mv, access, name, descriptor, paramsInfo);

      default:
        if (Config.isIncludedMethod(paramsInfo.toString())) {
          mv = new TrackStub(this.api, mv, access, name, descriptor, paramsInfo);
        }
    }
    return mv;
  }
}
