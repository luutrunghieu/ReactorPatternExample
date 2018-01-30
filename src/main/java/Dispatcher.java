import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by imdb on 29/01/2018.
 */
public class Dispatcher {
    private Map<Integer, EventHandler> registeredHandlers = new ConcurrentHashMap<Integer, EventHandler>();
    private Selector demultiplexer;

    public Dispatcher() throws Exception {
        demultiplexer = Selector.open();
    }

    public Selector getDemultiplexer() {
        return demultiplexer;
    }

    public void registerEventHandler(int eventType, EventHandler handler) {
        registeredHandlers.put(eventType, handler);
    }

    public void registerChannel(int eventType, SelectableChannel channel) throws Exception {
        channel.register(demultiplexer, eventType);
    }

    public void run() {
        try {
            while (true) {
                demultiplexer.select();
                Set<SelectionKey> readyHanles = demultiplexer.selectedKeys();
                Iterator<SelectionKey> handleIterator = readyHanles.iterator();
                while (handleIterator.hasNext()) {
                    SelectionKey handle = handleIterator.next();
                    if (handle.isAcceptable()) {
                        EventHandler handler = registeredHandlers.get(SelectionKey.OP_ACCEPT);
                        handler.handleEvent(handle);
                    }
                    if (handle.isReadable()) {
                        EventHandler handler = registeredHandlers.get(SelectionKey.OP_READ);
                        handler.handleEvent(handle);
                        handleIterator.remove();
                    }
                    if (handle.isWritable()) {
                        EventHandler handler = registeredHandlers.get(SelectionKey.OP_WRITE);
                        handler.handleEvent(handle);
                        handleIterator.remove();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
