package SecAgent.SecAsm.Common;

import SecAgent.SecAsm.Stub.*;
import SecAgent.SecAsm.Stub.Sql.MySqlStub;
import SecAgent.SecAsm.Stub.Sql.Mybatis.MybatisSqlAStub;
import SecAgent.SecAsm.Stub.Sql.Mybatis.MybatisSqlBStub;
import SecAgent.SecAsm.Stub.Sql.Mybatis.MybatisValueStub;
import SecAgent.SecAsm.Stub.Sql.OracleStub;
import SecAgent.Utils.Conf.Config;
import SecAgent.Utils.Conf.StubConfig;
import SecAgent.Utils.utils.DefaultLoggerHelper.DefaultLogger;
import SecAgent.Utils.utils.ParamsInfo;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

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
    MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);

    try {
      ParamsInfo paramsInfo =
        new ParamsInfo(
          CLASSNAME, access, name, Type.getArgumentTypes(descriptor), descriptor, signature);
      String methodname = paramsInfo.toString();
      Constructor constructor = constructors.getOrDefault(methodname, null);
      if (constructor == null) {
        /**
         * todo load container's jar
         */
        Class cls = null;
        switch (methodname) {
          case StubConfig.TOMCAT_STUB1:
            Config.jarLoader.addURL(Config.CONTAINER_JAR_PATHs.getOrDefault("TOMCAT", null));
            cls = Config.jarLoader.loadClass("SecAgent.Container.Tomcat.Stub.TomcatStub1");
            break;

          case StubConfig.TOMCAT_URL_STUB:
            Config.jarLoader.addURL(Config.CONTAINER_JAR_PATHs.getOrDefault("TOMCAT", null));
            cls = Config.jarLoader.loadClass("SecAgent.Container.Tomcat.Stub.TomcatUrlStub");
            break;

          case StubConfig.DUBBO_STUB:
            Config.jarLoader.addURL(Config.CONTAINER_JAR_PATHs.getOrDefault("DUBBO", null));
            cls = Config.jarLoader.loadClass("SecAgent.Container.Tomcat.Stub.DubboStub");
            break;

          default:
            break;
        }

        if (cls != null) {
          constructors.put(methodname, getStubConstructor(cls));
        }
      }

      constructor = constructors.getOrDefault(methodname, null);
      if (constructor == null && StubConfig.isIncludedMethod(paramsInfo.toString())) {
        constructor = constructors.get("DEFAULT");
      }


      if (constructor != null) {
        System.out.println("found:" + methodname);
        mv = (MethodVisitor) constructor.newInstance(this.api, mv, access, name, descriptor, paramsInfo);
      } else {
//        System.out.println("not found: "+methodname);
      }

      /* Deprecated */
      /**
       *
       * if (StubConfig.isIncludedMethod(paramsInfo.toString())) {
       *             System.out.println("trying to stub into " + paramsInfo.toString());
       *             mv = new TrackStub(this.api, mv, access, name, descriptor, paramsInfo);
       *           }
       */
    } catch (Exception e) {
      if (logger != null) logger.error(e);
      e.printStackTrace();
    }
    return mv;
  }
}
