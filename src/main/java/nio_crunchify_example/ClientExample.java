package nio_crunchify_example;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Created by imdb on 30/01/2018.
 */
public class ClientExample {
    public static void main(String[] args) throws Exception {
        Selector selector = Selector.open();

        SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress("localhost", 9999));
        socketChannel.configureBlocking(false);
        System.out.println("Connecting to server on port 9999....");
        List<String> companies = new ArrayList<String>();
        companies.add("Google");
        companies.add("Crunchy");
        SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_WRITE);
        int i = 0;
        boolean flag = true;
        while (flag) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isWritable()) {
                    String company = companies.get(i);
                    byte[] message = company.getBytes();
                    ByteBuffer buffer = ByteBuffer.wrap(message);
                    socketChannel.write(buffer);
                    buffer.clear();
                    System.out.println("Sent: " + company);
                    i++;
                    socketChannel.register(selector, SelectionKey.OP_READ);
                }
                else if (key.isReadable()) {
                    ByteBuffer buffer = ByteBuffer.allocate(256);
                    socketChannel.read(buffer);
                    String result = new String(buffer.array()).trim();
                    System.out.println("Message received from server: " + result);
                    if (result.equals("Crunchy")) {
                        System.out.println("Closed");
                        flag = false;
                        socketChannel.close();
                    } else{
                        socketChannel.register(selector,SelectionKey.OP_WRITE);
                    }
                }
            }
            iterator.remove();
        }
    }
}
