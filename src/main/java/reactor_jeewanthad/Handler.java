package reactor_jeewanthad;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * Created by imdb on 31/01/2018.
 */
public class Handler implements Runnable{
    final SocketChannel socketChannel;
    final SelectionKey selectionKey;
    ByteBuffer input = ByteBuffer.allocate(1024);
    static final int READING = 0, WRITING = 1;
    int state = READING;

    Handler(Selector selector, SocketChannel socketChannel)throws Exception{
        this.socketChannel = socketChannel;
        socketChannel.configureBlocking(false);
        selectionKey = socketChannel.register(selector,SelectionKey.OP_READ);
        selectionKey.attach(this);
        selector.wakeup();
    }

    public void run() {
        try{
            if(state == READING){
                read();
            } else {
                write();
            }
        } catch(Exception ex){

        }
    }
}
