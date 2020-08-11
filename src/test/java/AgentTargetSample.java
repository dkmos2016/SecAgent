import java.io.File;

public class AgentTargetSample {
    public static void output(String name) {
        String msg = name;
        System.out.println(String.format("%s say hello!", name));
    }

    public static void output(String name, long code, File f) {
        StringBuilder sb = new StringBuilder();

        sb.append(name);
        sb.append('|');
        sb.append(code);
        sb.append('|');
        sb.append(f);
        System.out.println(sb.toString());
        System.out.println(String.format("%s say hello!", name));
    }

    public static void main(String[] args) {
        output("len");
    }
}