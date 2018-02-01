package reactor_jeewanthad.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client implements Runnable {
    String hostIp;
    int hostPort;
    MonitorThread monitorThread;

    public Client(String hostIp, int hostPort, MonitorThread monitorThread) {
        this.hostIp = hostIp;
        this.hostPort = hostPort;
        this.monitorThread = monitorThread;
    }

    public void run() {
        try {
            Selector selector = Selector.open();
            SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(hostIp, hostPort));
            socketChannel.configureBlocking(false);
            System.out.println("Connecting to server on port " + hostPort + "....");
            SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_WRITE);
            selectionKey.attach(new ClientHandler(selectionKey,monitorThread));
            int i = 0;
            while (true) {
                selector.select();
                Set<SelectionKey> keys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = keys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    dispatch(key);
                }
                keys.clear();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void dispatch(SelectionKey key) {
        Runnable r = (Runnable) key.attachment();
        if (r != null) {
            r.run();
        }
    }

    public static void main(String[] args) throws IOException {
        MonitorThread monitorThread = new MonitorThread();
        ExecutorService executorService = Executors.newFixedThreadPool(11);
        executorService.execute(monitorThread);
        for (int i = 0; i < 10; i++) {
            executorService.execute(new Client("127.0.0.1", 9999, monitorThread));
        }
    }
}
