package reactor_with_queue.client;

import nio_crunchify_example.*;
import nio_crunchify_example.ReadEventHandler;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.*;

/**
 * Created by imdb on 01/02/2018.
 */
public class ClientReactor implements Runnable {
    BlockingQueue<CompletableFuture> queue; // future created, wait to be sent
    Map<Long, CompletableFuture> waitingFuture; // future sent, wait for response
    String hostIP;
    int port;
    ExecutorService executorService = Executors.newFixedThreadPool(2);
    List<CompletableFuture<Void>> writeListFuture;
    public ClientReactor(String hostIP, int port) {
        queue = new ArrayBlockingQueue(1024);
        this.hostIP = hostIP;
        this.port = port;
    }

    public BlockingQueue<CompletableFuture> getQueue() {
        return queue;
    }

    public void setQueue(BlockingQueue<CompletableFuture> queue) {
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
//                        for(CompletableFuture<Void> writeFuture : writeListFuture){
//                            writeFuture.thenRun(new ReadEventHandler(selectionKey)).handle((res,ex)->{
//                                return null;
//                            });
//                        }
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
                            CompletableFuture<Void> writeFuture = CompletableFuture.runAsync(new WriteEventHandler(message,socketChannel),executorService);
                            writeListFuture.add(writeFuture);
                            socketChannel.write(buffer);
                            System.out.println("Sent: "+message);
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
