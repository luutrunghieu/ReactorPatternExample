import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;

/**
 * Created by imdb on 29/01/2018.
 */
public class ReactorInitiator {
    private static final int NIO_SERVER_PORT = 9993;
    public void initiateServer(int port) throws Exception{
        ServerSocketChannel server = ServerSocketChannel.open();
        server.socket().bind(new InetSocketAddress(port));
        server.configureBlocking(false);

        Dispatcher dispatcher = new Dispatcher();
        dispatcher.registerChannel(SelectionKey.OP_WRITE,server);

        dispatcher.registerEventHandler(SelectionKey.OP_ACCEPT, new AcceptEventHandler(dispatcher.getDemultiplexer()));
        dispatcher.registerEventHandler(SelectionKey.OP_READ, new ReadEventHandler(dispatcher.getDemultiplexer()));
        dispatcher.registerEventHandler(SelectionKey.OP_WRITE, new WriteEventHandler());
        dispatcher.run();
    }

}
