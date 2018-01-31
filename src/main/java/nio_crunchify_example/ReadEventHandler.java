package nio_crunchify_example;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Created by imdb on 31/01/2018.
 */
public class ReadEventHandler implements Runnable {
    private SelectionKey selectionKey;

    public ReadEventHandler(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    public void run() {
        try {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            ByteBuffer buffer = ByteBuffer.allocate(256);
            if (socketChannel != null) {
                socketChannel.read(buffer);
                String result = new String(buffer.array()).trim();
                System.out.println("Message received: " + result);
                socketChannel.register(selectionKey.selector(), SelectionKey.OP_WRITE, buffer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
