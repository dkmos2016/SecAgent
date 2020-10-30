package SecAgent.utils;

import SecAgent.Conf.Config;
import SecAgent.utils.DefaultLoggerHelper.DefaultLogger;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HeartBeat {
    private final static ExecutorService ES = Executors.newSingleThreadExecutor();
    private final static String HOST;
    private final static int PORT;
    private final static int TIMEOUT;
    private final static int TIMESEC;

    private final static DefaultLogger logger = DefaultLogger.getLogger(HeartBeat.class, Config.EXCEPTION_PATH);


    static {
        HOST = Resources.getProperty("HEARTBEAT_HOST");
        PORT = Integer.parseInt(Resources.getProperty("HEARTBEAT_PORT"));
        TIMEOUT = convert(Resources.getProperty("HEARTBEAT_CONNECT_TIMEOUT"));
        TIMESEC = convert(Resources.getProperty("HEARTBEAT_TIMESEC"));
        if (logger != null) logger.setLevel(DefaultLogger.MyLevel.DEBUG);
    }

    private static int convert(String time_des) {
        int time = 3;
        boolean flag = false;
        time_des = time_des.replace("\n", "");
        String tag = null;
        int sz = 1000;

        if (!flag && time_des.endsWith("ms")) {
            tag = "ms";
            flag = true;
            sz = 1;
        } else if (!flag && time_des.endsWith("m")) {
            tag = "m";
            flag = true;
            sz = 60 * 1000;
        } else if (!flag && time_des.endsWith("s")) {
            tag = "s";
            flag = true;
            sz = 1000;
        } else if (!flag && time_des.endsWith("h")) {
            tag = "h";
            flag = true;
            sz = 60 * 60 * 1000;
        } else {
;
        }

        if (flag) {
            int idx = time_des.indexOf(tag);
            String value = time_des.substring(0, idx);
            char c = time_des.charAt(idx-1);
            if (Character.isDigit(c)) {
                time = Integer.parseInt(value) * sz;
            }
        }

        return time;
    }

    private static boolean beat() throws IOException {
        boolean flag = false;
        Socket socket = new Socket();
        SocketAddress socketAddress = new InetSocketAddress(HOST, PORT);
        byte[] bytes = new byte[40];
        try {
            socket.connect(socketAddress, TIMEOUT);
            logger.debug("connect is ok!");
            OutputStream out = socket.getOutputStream();
            out.write("hello".getBytes());
            out.close();

            flag = true;
        } catch (IOException e) {
            logger.error(e);
        } finally {
            socket.close();
        }

        return flag;
    }

    private static void startBeat() throws Exception {
        while (true) {
            try {
                beat();
                Thread.sleep(TIMESEC);
            } catch (Exception e) {
                logger.error(e);
            }
        }
    }

    public static void start() {
        ES.submit(new Callable(){
            @Override
            public Object call() throws Exception {
                startBeat();
                return null;
            }
        });
    }
}
