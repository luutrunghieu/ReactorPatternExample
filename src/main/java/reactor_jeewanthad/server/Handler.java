package reactor_jeewanthad.server;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.Executors;

/**
 * Created by imdb on 31/01/2018.
 */
public class Handler implements Runnable {
    final SocketChannel socketChannel;
    final SelectionKey selectionKey;
    ByteBuffer input = ByteBuffer.allocate(1024);
    static final int READING = 0, WRITING = 1;
    int state = READING;
    String clientName = "";

    Handler(Selector selector, SocketChannel socketChannel) throws Exception {
        this.socketChannel = socketChannel;
        socketChannel.configureBlocking(false);
        selectionKey = socketChannel.register(selector, SelectionKey.OP_READ);
        selectionKey.attach(this);
        selector.wakeup();
    }

    public void run() {
        try {
            if (state == READING) {
                read();
            } else if (state == WRITING) {
                write();
            }
        } catch (Exception ex) {

        }
    }

    void read() throws Exception {

        int readCount = socketChannel.read(input);
        if (readCount > 0) {
            readProcess(readCount);
        }
        try {
            Thread.sleep(1000);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        state = WRITING;
        selectionKey.interestOps(SelectionKey.OP_WRITE);
    }

    synchronized void readProcess(int readCount) {
        StringBuilder sb = new StringBuilder();
        input.flip();
        byte[] subStringBytes = new byte[readCount];
        byte[] array = input.array();
        System.arraycopy(array, 0, subStringBytes, 0, readCount);
        sb.append(new String(subStringBytes));
        input.clear();
        clientName = sb.toString().trim();
        System.out.println("Message received from client: "+clientName);
    }

    void write() throws Exception {
        if (clientName.equalsIgnoreCase("Bye")) {
            socketChannel.close();
        } else {
//            System.out.println("Saying hello to " + clientName);
            ByteBuffer output = ByteBuffer.wrap((clientName + "\n").getBytes());
            socketChannel.write(output);
            selectionKey.interestOps(SelectionKey.OP_READ);
            state = READING;
            System.out.println("Sent: "+clientName);
        }

    }
}
