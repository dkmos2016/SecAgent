import SecAgent.utils.ReqInfo;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;

public class AgentTargetSample<E> {
  private final int id;
  private final String name;
  private HttpServletRequest httpServletRequest;

  public AgentTargetSample(int id, String name) {
    StringBuilder sb = new StringBuilder();
    sb.append(id);
    sb.append("|");
    sb.append(name);
    sb.toString();
    this.id = id;
    this.name = name;
  }

  public static void test(Class[] params) {
    ReqInfo res;
    try {
      res =
          (ReqInfo)
              Thread.currentThread()
                  .getContextClassLoader()
                  .loadClass("SecAgent.utils.ReqInfo")
                  .getDeclaredMethod("doTest", params)
                  .invoke(null, 1, 2);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) {
    DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
    try {
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document d = builder.parse("src/test/resources/test.xml");
      NodeList sList = d.getElementsByTagName("student");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void test(HttpServletRequest request) {
    try {
      System.out.println(request.getInputStream());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
