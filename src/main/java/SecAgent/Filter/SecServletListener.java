package SecAgent.Filter;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;

public class SecServletListener implements ServletRequestListener {

  @Override
  public void requestDestroyed(ServletRequestEvent sre) {}

  @Override
  public void requestInitialized(ServletRequestEvent sre) {}
}
