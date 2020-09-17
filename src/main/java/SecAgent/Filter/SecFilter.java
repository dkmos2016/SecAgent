package SecAgent.Filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class SecFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            CopyServletRequestWrapper new_request = new CopyServletRequestWrapper((HttpServletRequest) request);

            chain.doFilter(new_request, response);
        }catch (Exception e) {
            e.printStackTrace();
            chain.doFilter(request, response);
        }

    }
}
