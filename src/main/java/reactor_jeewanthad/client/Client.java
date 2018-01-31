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

public class Client {
    String hostIp;
    int hostPort;

    public Client(String hostIp, int hostPort) {
        this.hostIp = hostIp;
        this.hostPort = hostPort;
    }

    public void runClient() throws IOException {
        Selector selector = Selector.open();

        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(hostIp, hostPort));
        socketChannel.configureBlocking(false);
        System.out.println("Connecting to server on port "+hostPort+ "....");
        SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_WRITE);
        selectionKey.attach(new ClientHandler(selectionKey));
        int i = 0;
//        boolean flag = true;
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
    }

    void dispatch(SelectionKey key) {
        Runnable r = (Runnable) key.attachment();
        if (r != null) {
            r.run();
        }
    }

    public static void main(String[] args) throws IOException {

        Client client = new Client("127.0.0.1", 9999);
        client.runClient();
    }
}
