import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

/**
 * Created by imdb on 29/01/2018.
 */
public class WriteEventHandler implements EventHandler {

    public void handleEvent(SelectionKey handle) throws Exception{
        SocketChannel socketChannel = (SocketChannel) handle.channel();
        ByteBuffer buffer = (ByteBuffer) handle.attachment();
        socketChannel.write(buffer);
        socketChannel.close();
    }
}
