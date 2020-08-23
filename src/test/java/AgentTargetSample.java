import org.apache.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

public class AgentTargetSample {
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

  public static void output(String name, test t, double i, int j) {
    System.out.println(String.format("%s|%d", name, i));
  }

  public static void show(String name, test[] t, byte[] bs) {
    StringBuilder sb = new StringBuilder();
    for (test n : t) {
      sb.append(n.toString());
    }


    if (bs != null) {
      sb.append(bs);
    }

    System.out.println(sb.toString());
  }

  public static void main(String[] args) throws IOException {
//        String[] strings = new String[] {"hello", "world", "!"};
//        test t = new test(1, "HHHHHH");
//        t.toString();
//        output("hhhh",  t, 1, 300000);
//        System.out.println("hello world!");

    show("hell", new test[]{
        new test(1111), new test(4444), new test(7777)},
      new byte[]{1, 2, 3}
    );

    Logger.getLogger("test").info("hello world");


    try {
      Runtime.getRuntime().exec("cmd /c 'whoami'");
    } catch (Exception e) {

    }

    HashMap<String, String[]> result = new HashMap<>();

    result.put("hello", null);

    Object f = new File("test.log");

    System.out.println(f.getClass());
  }

  public String getName(String s) {
    return s + name;
  }

  public static class test {
    private final int id;
    private final String desc;


    public test(int id) {
      this(id, "test");

      StringBuilder sb = new StringBuilder();
      sb.append(this.getClass().getName());
      sb.append('|');
      sb.append(id);
      sb.append('|');
      sb.append(desc);
      System.out.println(sb.toString());


      long x = 100000000000l;
    }

    public test(int id, String desc) {
//            try{
//                System.out.println(this.getClass().getName());
//                System.out.println(id);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
      try {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getName());
        sb.append('|');
        sb.append(id);
        sb.append('|');
        sb.append(desc);
        System.out.println(sb.toString());
      } catch (Exception e) {
        e.printStackTrace();
      }

      this.id = id;
      this.desc = desc;
    }

    @Override
    public String toString() {
      return String.format("%d %s", this.id, this.desc);
    }
  }
}