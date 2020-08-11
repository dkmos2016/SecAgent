

public class AgentTargetSample {
    public static void output(String name) {
        String msg = name;
        System.out.println(String.format("%s say hello!", name));
    }

    public static void output(String name, float code) {
        String msg = name;
        float c = code;
        System.out.println(String.format("%s say hello!", name));
    }

    public static void main(String[] args) {
        output("len");
    }
}