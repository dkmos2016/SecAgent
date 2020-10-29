package SecAgent.Container.Tomcat.Filter;

import javax.servlet.*;
import java.util.EnumSet;
import java.util.Set;

public class SecServletContainerInitializer implements ServletContainerInitializer {

  @Override
  public void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException {
    System.out.println("SecServletContainerInitializer: ");
    FilterRegistration.Dynamic filter = ctx.addFilter("SecFileter", new SecFilter());
    //    ctx.addListener(new SecServletListener());
    //
    filter.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), true, "/*");
  }
}
