package reactor_with_queue.client;

/**
 * Created by imdb on 01/02/2018.
 */
public class RunClient {
    public static void main(String[] args) {
        ClientReactor reactor = new ClientReactor("127.0.0.1",9999);
        new Thread(reactor).start();

        ClientThread clientThread = new ClientThread(reactor.getQueue());
        new Thread(clientThread).start();
    }
}
