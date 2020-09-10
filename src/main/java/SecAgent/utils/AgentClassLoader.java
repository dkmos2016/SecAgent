package SecAgent.utils;

public class AgentClassLoader extends ClassLoader {
  protected ClassLoader parent;

  public AgentClassLoader() {
    super(null);
//    this.parent = null;
  }

  public AgentClassLoader(ClassLoader parent) {
    super(parent);
//    this.parent = null;
  }

  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    System.out.println("AgentClassLoader loading class ");
    System.out.println(name);
    return super.loadClass(name, resolve);
  }
}
