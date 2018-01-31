package reactor_jeewanthad.client;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class ClientHandler implements Runnable {
    static final int READING = 0, WRITING = 1;
    int state = WRITING;
    SelectionKey key;
    SocketChannel socketChannel;

    ClientHandler(SelectionKey key) {
        this.key = key;
        socketChannel = (SocketChannel) key.channel();
        key.attach(this);
    }

    public void run() {
        try {
            if (state == WRITING) {
                write();
            } else if (state == READING) {
                read();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    void read() throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(256);
        socketChannel.read(buffer);
        String result = new String(buffer.array()).trim();
        System.out.println("Message received from server: " + result);
        key.interestOps(SelectionKey.OP_WRITE);
        state = WRITING;
    }

    void write() throws Exception {
        List<String> listName = new ArrayList();
        listName.add("Hieu");
        listName.add("An");
        listName.add("Bye");
        int rand = ThreadLocalRandom.current().nextInt(0, 2 + 1);
        ByteBuffer buffer = ByteBuffer.wrap(listName.get(rand).getBytes());
        socketChannel.write(buffer);
        System.out.println("Sent: "+listName.get(rand));
        if(listName.get(rand).equalsIgnoreCase("Bye")){
            socketChannel.close();
        } else{
            key.interestOps(SelectionKey.OP_READ);
            state = READING;
        }
    }
}
