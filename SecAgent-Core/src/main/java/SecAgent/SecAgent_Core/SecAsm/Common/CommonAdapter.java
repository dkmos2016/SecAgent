package SecAgent.SecAgent_Core.SecAsm.Common;


import SecAgent.SecAgent_Core.Conf.Config;
import SecAgent.SecAgent_Core.Conf.StubConfig;
import SecAgent.SecAgent_Core.SecAsm.Stub.*;
import SecAgent.SecAgent_Core.SecAsm.Stub.Container.Tomcat.TomcatStub1;
import SecAgent.SecAgent_Core.SecAsm.Stub.Sql.MySqlStub;
import SecAgent.SecAgent_Core.SecAsm.Stub.Sql.Mybatis.MybatisSqlAStub;
import SecAgent.SecAgent_Core.SecAsm.Stub.Sql.Mybatis.MybatisSqlBStub;
import SecAgent.SecAgent_Core.SecAsm.Stub.Sql.Mybatis.MybatisValueStub;
import SecAgent.SecAgent_Core.SecAsm.Stub.Sql.OracleStub;
import SecAgent.SecAgent_Core.utils.DefaultLoggerHelper.DefaultLogger;
import SecAgent.SecAgent_Core.utils.ParamsInfo;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class CommonAdapter extends ClassVisitor implements Opcodes {
  private static final DefaultLogger logger =
      DefaultLogger.getLogger(CommonAdapter.class, Config.EXCEPTION_PATH);
  private final String CLASSNAME;

  public CommonAdapter(final ClassVisitor cv, final String name) {
    super(Opcodes.ASM9, cv);
    CLASSNAME = name;
  }

  @Override
  public MethodVisitor visitMethod(
      int access, String name, String descriptor, String signature, String[] exceptions) {
    MethodVisitor mv = null;
    try {
      mv = super.visitMethod(access, name, descriptor, signature, exceptions);
      ParamsInfo paramsInfo =
          new ParamsInfo(
              CLASSNAME, access, name, Type.getArgumentTypes(descriptor), descriptor, signature);

      switch (paramsInfo.toString()) {
        case StubConfig.MYSQL_STUB:
          return new MySqlStub(this.api, mv, access, name, descriptor, paramsInfo);

        case StubConfig.ORACLE_STUB:
          return new OracleStub(this.api, mv, access, name, descriptor, paramsInfo);

        case StubConfig.MYBATIS_VALUE_STUB:
          return new MybatisValueStub(this.api, mv, access, name, descriptor, paramsInfo);

        case StubConfig.MYBATIS_SQLA_STUB:
          return new MybatisSqlAStub(this.api, mv, access, name, descriptor, paramsInfo);

        case StubConfig.MYBATIS_SQLB_STUB:
          return new MybatisSqlBStub(this.api, mv, access, name, descriptor, paramsInfo);

        case StubConfig.TOMCAT_STUB:
          return new TomcatStub1(this.api, mv, access, name, descriptor, paramsInfo);

        case StubConfig.EXEC_STUB:
          return new CmdStub(this.api, mv, access, name, descriptor, paramsInfo);

        case StubConfig.DOWN_STUB:
          return new DownStub(this.api, mv, access, name, descriptor, paramsInfo);

        case StubConfig.UPLOAD_STUB:
          return new UploadStub(this.api, mv, access, name, descriptor, paramsInfo);

//        case StubConfig.SPRING_URL_STUB:
//          return new UrlStub(this.api, mv, access, name, descriptor, paramsInfo);

        case StubConfig.XXE_STUB:
          return new XxeStub(this.api, mv, access, name, descriptor, paramsInfo);

        default:
          if (StubConfig.isIncludedMethod(paramsInfo.toString())) {
            System.out.println("trying to stub into " + paramsInfo.toString());
            mv = new TrackStub(this.api, mv, access, name, descriptor, paramsInfo);
          }
      }
    } catch (Exception e) {
      if (logger != null) logger.error(e);
    }

    return mv;
  }
}
