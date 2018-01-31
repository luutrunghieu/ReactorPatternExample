package nio_crunchify_example;

import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * Created by imdb on 31/01/2018.
 */
public class AcceptEventHandler implements Runnable {
    private SelectionKey selectionKey;

    public AcceptEventHandler(SelectionKey selectionKey) {
        this.selectionKey = selectionKey;
    }

    public void run() {
        try {
            ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
            SocketChannel socketChannel = serverSocketChannel.accept();
            if (socketChannel != null) {
                socketChannel.configureBlocking(false);
                socketChannel.register(selectionKey.selector(), SelectionKey.OP_READ);
                System.out.println("Connection Accepted: " + socketChannel.getLocalAddress() + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
