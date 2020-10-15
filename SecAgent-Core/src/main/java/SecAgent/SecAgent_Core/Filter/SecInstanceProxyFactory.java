package SecAgent.SecAgent_Core.Filter;


import SecAgent.SecAgent_Core.utils.DefaultLoggerHelper.DefaultLogger;
import SecAgent.SecAgent_Core.Conf.Config;
import SecAgent.SecAgent_Core.utils.ReqLocal;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;

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
    //    System.out.println(object);
    logger.debug("SecInstanceProxyFactory.<init>");
  }

  private Object proxyGetInputStream(Object obj) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

    if (byteArrayInputStream == null) {
      InputStream in = (InputStream) obj;
      int v = -1;
      while ((v = in.read()) > -1) {
        byteArrayOutputStream.write(v);
      }
      in.close();

      byteArrayOutputStream.flush();
      byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

      ReqLocal.getReqInfo()
          .setInputStream(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
    }

    return new ServletInputStream() {
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

  private void proxyGetParameterMap(Object obj) {
    logger.debug("proxyGetParameterMap");
    ReqLocal.getReqInfo().setQueries((Map) obj);
  }

  @SuppressWarnings("unused")
  private ServletOutputStream proxyGetOutputStream(Object obj) throws IOException {
    return new ServletOutputStream() {
      @Override
      public boolean isReady() {
        return false;
      }

      @Override
      public void setWriteListener(WriteListener writeListener) {

      }

      @Override
      public void write(int b) throws IOException {
        ((ServletOutputStream)obj).write(b);
        ReqLocal.getReqInfo().getOutputStream().write(b);
      }

    };
  }

  public Object getProxyInstance() {
    return Proxy.newProxyInstance(
        target.getClass().getClassLoader(),
        target.getClass().getInterfaces(),
        new InvocationHandler() {
          @Override
          public Object invoke(Object proxy, Method method, Object[] args) {
            logger.debug("before invoke " + method.getName());

            Object ret = null;
            try {
              Object obj = method.invoke(target, args);
              String method_name = method.getName();
              if (obj instanceof InputStream && method_name.equals("getInputStream")) {
                ret = proxyGetInputStream(obj);
              } else if (obj instanceof Map && method.getName().equals("getParameterMap")) {
                proxyGetParameterMap(obj);
              } else if (obj instanceof Map && method.getName().equals("getParameterMap")) {
                ret = proxyGetOutputStream(obj);
              } else {
                ret = obj;
              }

              logger.debug("after invoke " + method.getName());
            } catch (Exception e) {
              logger.error(e);
            }

            return ret;
          }
        });
  }
}
