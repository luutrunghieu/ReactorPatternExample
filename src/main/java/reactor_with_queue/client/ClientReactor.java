package reactor_with_queue.client;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by imdb on 01/02/2018.
 */
public class ClientReactor implements Runnable {
    BlockingQueue<String> queue;
    String hostIP;
    int port;
    ExecutorService executorService = Executors.newFixedThreadPool(2);

    public ClientReactor(String hostIP, int port) {
        queue = new ArrayBlockingQueue(1024);
        this.hostIP = hostIP;
        this.port = port;
    }

    public BlockingQueue<String> getQueue() {
        return queue;
    }

    public void setQueue(BlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            Selector selector = Selector.open();
            SocketChannel socketChannel = SocketChannel.open(new InetSocketAddress(9999));
            socketChannel.configureBlocking(false);
            socketChannel.register(selector, SelectionKey.OP_WRITE);
            while (true) {
                selector.select();
                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> iterator = selectionKeys.iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    if (selectionKey.isReadable()) {
                        // todo and finish it here (set value or set exception)
                        // should be CompletableFuture
                        Future<String> stringFuture = executorService.submit(new ReadEventHandler(selectionKey));
                        if(stringFuture.get().equalsIgnoreCase("Hello")){
                            selectionKey.interestOps(SelectionKey.OP_WRITE);
                        } else if(stringFuture.get().equalsIgnoreCase("Bye")){
                            socketChannel.close();
                        }
                    }
                    if (selectionKey.isWritable()) {
                        while(!queue.isEmpty()){
                            String message = queue.take();
                            ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
                            // todo create Future here
                            socketChannel.write(buffer);
                        }
                        selectionKey.interestOps(SelectionKey.OP_READ);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
