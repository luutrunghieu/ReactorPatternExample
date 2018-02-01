package reactor_jeewanthad.test;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by imdb on 31/01/2018.
 */
public class HandlerWithThreadPool extends Handler {
    static ExecutorService pool = Executors.newFixedThreadPool(2);
    static final int PROCESSING = 2;

    public HandlerWithThreadPool(Selector sel, SocketChannel c) throws Exception {
        super(sel, c);
    }

    void read() throws Exception {
        int readCount = socketChannel.read(input);
        if (readCount > 0) {
            state = PROCESSING;
            pool.execute(new Processer(readCount));
        }
        selectionKey.interestOps(SelectionKey.OP_WRITE);
    }

    synchronized void processAndHandOff(int readCount) {
        readProcess(readCount);
        state = WRITING;
    }

    class Processer implements Runnable {
        int readCount;

        Processer(int readCount) {
            this.readCount = readCount;
        }

        public void run() {
            processAndHandOff(readCount);
        }
    }
}
