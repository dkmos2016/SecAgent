package SecAgent.utils;

public class AgentClassLoader extends ClassLoader {
  protected ClassLoader parent;

  protected AgentClassLoader(ClassLoader parent) {
    super(parent);
    //    this.parent = parent;
  }

  @Override
  protected Class<?> loadClass(String name, boolean resolve) throws ClassNotFoundException {
    System.out.println("AgentClassLoader loading class ");
    System.out.println(this.getParent());
    return super.loadClass(name, resolve);
  }
}
