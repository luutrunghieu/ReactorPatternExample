package reactor_with_queue.client;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by imdb on 02/02/2018.
 */
public class WriteEventHandler implements Runnable {

    String message;
    SocketChannel socketChannel;

    public WriteEventHandler(String message, SelectableChannel selectableChannel) {
        this.message = message;
        this.socketChannel = (SocketChannel) selectableChannel;
    }

    @Override
    public void run() {
        try {
            ByteBuffer buffer = ByteBuffer.wrap(message.getBytes());
            socketChannel.write(buffer);
            System.out.println("Sent: "+message);
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }

    }
}
