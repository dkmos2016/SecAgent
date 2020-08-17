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

        public  test(int id) {
            this(id, "test");

            StringBuilder sb = new StringBuilder();
            sb.append(this.getClass().getName());
            sb.append('|');
            sb.append(id);
            sb.append('|');
            sb.append(desc);
            System.out.println(sb.toString());
        }

        public test(int id, String desc) {
//            try{
//                System.out.println(this.getClass().getName());
//                System.out.println(id);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            try{
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

    public static void output(String name, test t, double i,int j) {
        System.out.println(String.format("%s|%d", name,i));
    }

    public static void show(String name, test []t, byte[] bs) {
        StringBuilder sb = new StringBuilder();
        for (test n: t) {
            sb.append(n.toString());
        }


        if (bs != null) {
            sb.append(bs);
        }

        System.out.println(sb.toString());
    }

    public static void main(String[] args) {
//        String[] strings = new String[] {"hello", "world", "!"};
//        test t = new test(1, "HHHHHH");
//        t.toString();
//        output("hhhh",  t, 1, 300000);
//        System.out.println("hello world!");

        show("hell", new test[]{
                new test(1111),new test(4444),new test(7777)},
                new byte[]{1,2,3}
        );
    }
}