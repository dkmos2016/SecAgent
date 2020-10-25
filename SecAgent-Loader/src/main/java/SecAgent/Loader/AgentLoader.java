package SecAgent.Loader;

import java.net.URL;
import java.net.URLClassLoader;


public class AgentLoader extends URLClassLoader {

    private final static String[] JAR_FILEs;

    static {
        JAR_FILEs = new String[] {
          "SecAgent-Tomcat.jar",
          "SecAgent-Dubbo.jar",
          "SecAgent-Jboss.jar",
        };
    }

    public AgentLoader(URL[] urls) {
        super(urls, AgentLoader.getSystemClassLoader().getParent());
    }

    /**
     * unzip containers' jar from SecAgent.jar
     */
    private void initilize() {
        JarUtils.doReleaseJar("", "");
        for (String file_name: JAR_FILEs) {
            JarUtils.doReleaseJar(file_name, null);
        }
    }

    @Override
    protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
//        return super.loadClass(name, resolve);

        if (name.startsWith("java") || name.startsWith("net") || name.endsWith("ReqInfo") || name.equals("SecAgent.SecAgent_Core.Logger.ExceptionLogger")) {
            return super.loadClass(name, resolve);
        } else {

            Class cls = findClass(name);

            if (resolve) {
                resolveClass(cls);
            }

            return cls;
        }

    }
}
