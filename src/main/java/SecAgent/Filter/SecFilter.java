package SecAgent.Filter;

import SecAgent.Conf.Config;
import SecAgent.utils.DefaultLoggerHelper.DefaultLogger;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecFilter implements Filter {
  private static final DefaultLogger logger =
      DefaultLogger.getLogger(SecFilter.class, Config.EXCEPTION_PATH);

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {
    if (logger != null) logger.debug("doFilter: ");
    try {
      CopyServletRequestWrapper new_request =
          new CopyServletRequestWrapper((HttpServletRequest) request);
      CopyServletResponseWrapper new_response =
          new CopyServletResponseWrapper((HttpServletResponse) response);
      chain.doFilter(new_request, new_response);
    } catch (Exception e) {
      if (logger != null) logger.error(e);
      throw new ServletException(e);
    }
  }
}
