package nio_crunchify_example;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by imdb on 30/01/2018.
 */
public class ServerExample {
    public static void main(String[] args) throws Exception {
        Selector selector = Selector.open();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind(new InetSocketAddress("localhost", 9999));
        serverSocketChannel.configureBlocking(false);

        int ops = serverSocketChannel.validOps();
        SelectionKey selectionKey = serverSocketChannel.register(selector, ops);

        ExecutorService executorService = Executors.newCachedThreadPool();
        while (true) {
            System.out.println("i'm a server and i'm waiting for new connection and buffer select...");
            selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                if (key.isAcceptable()) {
//                    SocketChannel socketChannel = serverSocketChannel.accept();
//                    socketChannel.configureBlocking(false);
//                    socketChannel.register(selector, SelectionKey.OP_READ);
//                    System.out.println("Connection Accepted: " + socketChannel.getLocalAddress() + "\n");
                    executorService.submit(new AcceptEventHandler(key));
                }
                if (key.isReadable()) {
//                    SocketChannel socketChannel = (SocketChannel) key.channel();
//                    ByteBuffer buffer = ByteBuffer.allocate(256);
//                    socketChannel.read(buffer);
//                    String result = new String(buffer.array()).trim();
//                    System.out.println("Message received: " + result);
//                    socketChannel.register(selector, SelectionKey.OP_WRITE, buffer);
                    executorService.submit(new ReadEventHandler(key));
                }
                if (key.isWritable()) {
//                    SocketChannel socketChannel = (SocketChannel) key.channel();
//                    ByteBuffer buffer = (ByteBuffer) key.attachment();
//                    buffer.flip();
//                    socketChannel.write(buffer);
//                    String response = new String(buffer.array()).trim();
//                    System.out.println("Write to client: "+response);
//                    if(response.equals("Crunchy")){
//                        System.out.println("Closed");
//                        socketChannel.close();
//                    } else{
//                        socketChannel.register(selector,SelectionKey.OP_READ);
//                    }
                    executorService.submit(new WriteEventHandler(key));
                }
            }
            iterator.remove();
        }
    }
}

