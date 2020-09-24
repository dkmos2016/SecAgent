package SecAgent.Filter;

import SecAgent.Conf.Config;
import SecAgent.utils.DefaultLoggerHelper.DefaultLogger;
import SecAgent.utils.ReqLocal;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class SecServletRequestFactory {
  private static final DefaultLogger logger;

  private final Object target;
  private ByteArrayInputStream byteArrayInputStream = null;


    static {
        logger = DefaultLogger.getLogger(SecServletRequestFactory.class, Config.EXCEPTION_PATH);
        logger.setLevel(DefaultLogger.MyLevel.DEBUG);
    }

  public SecServletRequestFactory(Object object) {
    this.target = object;
  }

  public Object getProxyInstance() {
    return Proxy.newProxyInstance(
        target.getClass().getClassLoader(),
        target.getClass().getInterfaces(),
        new InvocationHandler() {
          @Override
          public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            logger.debug("before invoke " + method.getName());
            Object obj = method.invoke(target, args);
            if (obj instanceof InputStream) {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

                if (byteArrayInputStream == null) {
                    InputStream in = (InputStream) obj;
                    int v = -1;
                    while ((v = in.read()) > -1) {
                        byteArrayOutputStream.write(v);
                    }

                    byteArrayInputStream =
                            new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

                    ReqLocal.getReqInfo().setInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
                }


                obj = new ServletInputStream() {
                    @Override
                    public boolean isFinished() {
                        return false;
                    }

                    @Override
                    public boolean isReady() {
                        return false;
                    }

                    @Override
                    public void setReadListener(ReadListener readListener) {

                    }

                    @Override
                    public int read() throws IOException {
                        return byteArrayInputStream.read();
                    }
                };
            }
            logger.debug("after invoke " + method.getName());

            return obj;
          }
        });
  }
}
