package SecAgent.Proxy;

import SecAgent.Conf.Config;
import SecAgent.utils.DefaultLoggerHelper.DefaultLogger;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

/** for class with implements */
abstract public class SecInstanceProxyFactoryAbstract {
    protected static final DefaultLogger logger;

    static {
        logger = DefaultLogger.getLogger(SecInstanceProxyFactoryAbstract.class, Config.EXCEPTION_PATH);
        logger.setLevel(DefaultLogger.MyLevel.DEBUG);
    }

    protected final Object target;
    protected ByteArrayInputStream byteArrayInputStream = null;

    public SecInstanceProxyFactoryAbstract(Object object) {
        this.target = object;
        //    System.out.println(object);
        logger.debug("SecInstanceProxyFactory.<init>");
    }

    abstract protected ByteArrayOutputStream proxyGetInputStream(Object obj) throws IOException;

    abstract protected void proxyGetParameterMap(Object obj);
//    {
//        logger.debug("proxyGetParameterMap");
//        ReqLocal.getReqInfo().setQueries((Map) obj);
//    }

    public Object getProxyInstance(InvocationHandler handle) {
        return Proxy.newProxyInstance(
                target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                handle);
    }

    abstract Object getProxyInstance();
}
