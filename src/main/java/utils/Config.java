package utils;

public class Config {
    public static String[] exclude_classes;
    public static String[] exclude_methods;
    public static String[] include_classes;
    public static String[] include_methods;


    static {
        exclude_classes = new String[]{
                "MethodHandleImpl","ClassValue","ExceptionInInitializerError",
                "Shutdown","Throwable","NonRegisteringDriver","AbandonedConnectionCleanupThread",
                "IdentityHashMap","FloatingDecimal","NoSuchFieldException","IllegalAccessException",
                "Wrapper","java.lang.", "sun.invoke.util.BytecodeDescriptor",
                "jdk.internal.org.objectweb.asm",
                "java.util.",
                "sun.reflect.UnsafeStaticObjectFieldAccessorImpl",
                "java.net.SocketPermission",
                "java.sql.DriverManager.registerDriver",
                "com.mysql.cj.jdbc.Driver",
                "java.sql.DriverManager",
                "sun.net.www.protocol.",
                "sun.misc.CompoundEnumeration",
                "java.security.PrivilegedActionException",
                "sun.misc.Launcher",
                "sun.misc.URLClassPath",
                "java.net.URLClassLoader",
                "java.io.FileNotFoundException",
                "sun.invoke.util",
                "sun.launcher.LauncherHelper",
                "sun.usagetracker.UsageTrackerClient",

        };

        exclude_methods = new String[]{"main", "<clinit>"};
        include_classes = new String[] {

        };
        include_methods = new String[]{
                "com.mysql.cj.conf.HostInfo.<init>(Lcom.mysql.cj.conf.DatabaseUrlContainer;Ljava.lang.String;ILjava.lang.String;ZLjava.lang.String;ZLjava.util.Map;)V",
                "len.test"
        };
    }

    public static boolean isExcludedClass(String src){
        for (String clazz: exclude_classes) {
            if (src.contains(clazz.replace('.', '/'))) {
                return true;
            }
        }
        return false;
    }

    public static boolean isExcludedMethod(String src){
        for (String method: exclude_methods) {
            if (src.contains(method)){
                return true;
            }
        }
        return false;
    }

    public static boolean isIncludedClass(String src){
        for (String clazz: include_classes) {
            if (src.contains(clazz)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isIncludedMethod(String src){
        for (String method: include_methods) {
            if (src.contains(method)) {
                return true;
            }
        }
        return false;
    }
}
