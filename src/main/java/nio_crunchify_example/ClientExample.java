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
        companies.add("IBM");
        companies.add("Facebook");
        companies.add("Crunchy");
//
//        System.out.println("Sending message");
//        for(String company: companies){
//            byte[] message = company.getBytes();
//            ByteBuffer buffer = ByteBuffer.wrap(message);
//            socketChannel.write(buffer);
//            buffer.clear();
//            System.out.println("Sent: "+company);
//            Thread.sleep(2000);
//        }
        SelectionKey selectionKey = socketChannel.register(selector, SelectionKey.OP_WRITE);
        while (true) {
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isWritable()) {
                    for (String company : companies) {
                        byte[] message = company.getBytes();
                        ByteBuffer buffer = ByteBuffer.wrap(message);
                        socketChannel.write(buffer);
                        buffer.clear();
                        System.out.println("Sent: " + company);
                        Thread.sleep(2000);
                    }
                    socketChannel.register(selector,SelectionKey.OP_READ);
                }
                if(key.isReadable()){
                    ByteBuffer buffer = ByteBuffer.allocate(256);
                    SocketChannel readSocketChannel = (SocketChannel) key.channel();
                    readSocketChannel.read(buffer);
                    String result = new String(buffer.array()).trim();
                    System.out.println("Message received from server: "+result);
                }
            }
        }
//        socketChannel.close();
    }
}
