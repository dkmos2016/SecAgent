package SecAgent.Filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class SecFileter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        CopyServletRequestWrapper new_request = new CopyServletRequestWrapper((HttpServletRequest) request);

        chain.doFilter(new_request, response);
    }
}
