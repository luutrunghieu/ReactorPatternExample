package nio_crunchify_example;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Created by imdb on 31/01/2018.
 */
public class WriteEventHandler implements Runnable {
    private SelectionKey selectionKey;

    public WriteEventHandler(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    public void run() {
        try {
            SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
            ByteBuffer buffer = (ByteBuffer) selectionKey.attachment();
            buffer.flip();
            if (socketChannel != null) {
                socketChannel.write(buffer);
                String response = new String(buffer.array()).trim();
                System.out.println("Write to client: " + response);
                if (response.equals("Crunchy")) {
                    System.out.println("Closed");
                    socketChannel.close();
                } else {
                    socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
