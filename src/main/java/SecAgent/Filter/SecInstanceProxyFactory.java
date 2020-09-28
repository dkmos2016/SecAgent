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

/** for class with implements */
public class SecInstanceProxyFactory {
  private static final DefaultLogger logger;

  static {
    logger = DefaultLogger.getLogger(SecInstanceProxyFactory.class, Config.EXCEPTION_PATH);
    logger.setLevel(DefaultLogger.MyLevel.DEBUG);
  }

  private final Object target;
  private ByteArrayInputStream byteArrayInputStream = null;

  public SecInstanceProxyFactory(Object object) {
    this.target = object;
    System.out.println(object);
    logger.debug("SecInstanceProxyFactory.<init>");
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
                byteArrayOutputStream.flush();

                byteArrayInputStream =
                    new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

                //                    System.out.println(new
                // String(byteArrayOutputStream.toByteArray()));
                ReqLocal.getReqInfo()
                    .setInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
              }

              obj =
                  new ServletInputStream() {
                    @Override
                    public boolean isFinished() {
                      return false;
                    }

                    @Override
                    public boolean isReady() {
                      return false;
                    }

                    @Override
                    public void setReadListener(ReadListener readListener) {}

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
