package SecAgent.Filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            CopyServletRequestWrapper new_request = new CopyServletRequestWrapper((HttpServletRequest) request);
            CopyServletResponseWrapper new_response = new CopyServletResponseWrapper((HttpServletResponse) response);
            chain.doFilter(new_request, new_response);
        }catch (Exception e) {
            e.printStackTrace();
            chain.doFilter(request, response);
        }
    }
}
