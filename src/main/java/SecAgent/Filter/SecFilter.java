package SecAgent.Filter;

import SecAgent.Conf.Config;
import SecAgent.utils.DefaultLoggerHelper.DefaultLogger;
import SecAgent.utils.ReqLocal;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;

public class SecFilter implements Filter {
  private static final DefaultLogger logger =
      DefaultLogger.getLogger(SecFilter.class, Config.DEBUG_PATH);

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    if (logger != null) logger.debug("doFilter: ");
    try {
      //      CopyServletRequestWrapper new_request =
      //          new CopyServletRequestWrapper((HttpServletRequest) request);
//      CopyServletResponseWrapper new_response =
//          new CopyServletResponseWrapper((HttpServletResponse) response);

      ReqLocal.getReqInfo().setHttpServletRequest((HttpServletRequest) request);

      ServletRequest new_request =
          (ServletRequest) new SecInstanceProxyFactory(request).getProxyInstance();
      ServletResponse new_response =
              (ServletResponse) new SecInstanceProxyFactory(response).getProxyInstance();
      chain.doFilter(new_request, new_response);

      ReqLocal.getReqInfo().doJob();
      ReqLocal.clear();
    } catch (Exception e) {
      if (logger != null) logger.error(e);
      throw new ServletException(e);
    }
  }
}
