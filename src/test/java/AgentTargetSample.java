import java.io.File;

public class AgentTargetSample {
    private int id;
    private String name;
    public AgentTargetSample(int id, String name) {
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        sb.append("|");
        sb.append(name);
        sb.toString();
        this.id = id;
        this.name = name;
    }

    public String getName(String s) {
        return s+name;
    }

    public static class test {
        private int id;
        private String desc;

        public test(int id, String desc) {
            this.id = id;
            this.desc = desc;
        }

        @Override
        public String toString() {
            return String.format("%d %s", this.id, this.desc);
        }
    }

    public static void output(String[] strings, String name, test t, double i,int j) {

        StringBuilder sb = new StringBuilder();
        sb.append(strings[0]);
        sb.append("|");
        sb.append(strings[1]);
        sb.append("|");
        sb.append(strings[2]);
        sb.append("|");
        sb.append(t.toString());
        System.out.println(sb.toString());

        System.out.println(String.format("%s say hello!",name));
    }

    public static void main(String[] args) {
        String[] strings = new String[] {"hello", "world", "!"};
        test t = new test(1, "HHHHHH");
        output(strings, "hhhh",  t, 1, 1);
        System.out.println(args.length);
    }
}