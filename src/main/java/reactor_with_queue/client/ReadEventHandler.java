package reactor_with_queue.client;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Callable;

/**
 * Created by imdb on 01/02/2018.
 */
public class ReadEventHandler implements Callable {
    SelectionKey selectionKey;
    SocketChannel socketChannel;

    public ReadEventHandler(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
        socketChannel = (SocketChannel) selectionKey.channel();
    }

    @Override
    public String call() throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(256);
        socketChannel.read(buffer);
        String result = new String(buffer.array()).trim();
        System.out.println("Message received from server: "+result);
        return result;
    }
}
