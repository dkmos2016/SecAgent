package SecAgent.Container.Tomcat.Filter;

import javax.servlet.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import SecAgent.Utils.utils.ReqLocal;
import SecAgent.Utils.utils.DefaultLoggerHelper.DefaultLogger;
import SecAgent.Utils.Conf.Config;

public class SecFilter implements Filter {
  private static final DefaultLogger logger =
      DefaultLogger.getLogger(SecFilter.class, Config.DEBUG_PATH);

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    if (logger != null) logger.debug("doFilter: ");
    try {
      System.out.println("doFilter:");
      //      CopyServletRequestWrapper new_request =
      //          new CopyServletRequestWrapper((HttpServletRequest) request);
//      CopyServletResponseWrapper new_response =
//          new CopyServletResponseWrapper((HttpServletResponse) response);

      ReqLocal.getReqInfo().setHttpServletRequest((HttpServletRequest) request);

      ServletRequest new_request =
          (ServletRequest) new SecInstanceProxyFactory(request).getProxyInstance();
//      ServletResponse new_response =
//              (ServletResponse) new SecInstanceProxyFactory(response).getProxyInstance();
      chain.doFilter(new_request, response);

      ReqLocal.getReqInfo().doJob("HTTP");
      ReqLocal.clear();
    } catch (Exception e) {
      if (logger != null) logger.error(e);
      throw new ServletException(e);
    }
  }

}
