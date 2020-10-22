package SecAgent.SecAsm.Common;

import SecAgent.SecAsm.Stub.*;
import SecAgent.SecAsm.Stub.Container.Tomcat.TomcatStub1;
import SecAgent.SecAsm.Stub.Sql.MySqlStub;
import SecAgent.SecAsm.Stub.Sql.Mybatis.MybatisSqlAStub;
import SecAgent.SecAsm.Stub.Sql.Mybatis.MybatisSqlBStub;
import SecAgent.SecAsm.Stub.Sql.Mybatis.MybatisValueStub;
import SecAgent.SecAsm.Stub.Sql.OracleStub;
import SecAgent.Utils.Conf.Config;
import SecAgent.Utils.Conf.StubConfig;
import SecAgent.Utils.utils.DefaultLoggerHelper.DefaultLogger;
import SecAgent.Utils.utils.JarClassLoader;
import SecAgent.Utils.utils.ParamsInfo;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.File;
import java.lang.reflect.Constructor;
import java.util.HashMap;

public class CommonAdapter extends ClassVisitor implements Opcodes {
  private static final DefaultLogger logger =
      DefaultLogger.getLogger(CommonAdapter.class, Config.EXCEPTION_PATH);
  private final String CLASSNAME;

  private final static HashMap<String, Constructor> constructors = new HashMap();

  static {
    constructors.put(StubConfig.EXEC_STUB, getStubConstructor(CmdStub.class));
    constructors.put(StubConfig.DOWN_STUB, getStubConstructor(DownStub.class));
    constructors.put(StubConfig.UPLOAD_STUB, getStubConstructor(UploadStub.class));
    constructors.put(StubConfig.MYSQL_STUB, getStubConstructor(MySqlStub.class));

    constructors.put(StubConfig.ORACLE_STUB, getStubConstructor(OracleStub.class));

    constructors.put(StubConfig.MYBATIS_SQLA_STUB, getStubConstructor(MybatisSqlAStub.class));
    constructors.put(StubConfig.MYBATIS_SQLB_STUB, getStubConstructor(MybatisSqlBStub.class));
    constructors.put(StubConfig.MYBATIS_VALUE_STUB, getStubConstructor(MybatisValueStub.class));

    constructors.put(StubConfig.XXE_STUB, getStubConstructor(XxeStub.class));

    constructors.put("DEFAULT", getStubConstructor(TrackStub.class));
  }

  public static Constructor getStubConstructor(Class cls){
    Constructor constructor = null;
    try{

      constructor = cls.getConstructor(int.class, MethodVisitor.class, int.class, String.class, String.class, ParamsInfo.class);
    } catch (Exception e){
      logger.error(e);
    }
    return constructor;
  }

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
      String methodname = paramsInfo.toString();
      Constructor constructor = constructors.getOrDefault(methodname, null);
      if (constructor == null) {
        /**
         * todo load container's jar
         */
//        try {
//          new JarClassLoader(new File("urls/"+))
//        }
      }

      /* Deprecated */

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

        case StubConfig.SPRING_URL_STUB:
          return new UrlStub(this.api, mv, access, name, descriptor, paramsInfo);

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
