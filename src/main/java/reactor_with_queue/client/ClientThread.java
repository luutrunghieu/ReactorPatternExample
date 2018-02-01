package reactor_with_queue.client;

import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Created by imdb on 01/02/2018.
 */
public class ClientThread implements Runnable{
    SocketChannel socketChannel;
    BlockingQueue queue;
    ClientThread(BlockingQueue<String> queue){
        this.queue = queue;
    }
    @Override
    public void run() {
        List<String> messages = new ArrayList<>();
        messages.add("Hello");
        messages.add("Bye");
        while(true){
            try {
                int rand = ThreadLocalRandom.current().nextInt(0, messages.size());
                // generate a message here
                String message = messages.get(rand);
                queue.put(messages.get(rand));
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
