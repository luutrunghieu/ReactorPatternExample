package reactor_with_queue.client;

import reactor_with_queue.model.EchoMessage;
import reactor_with_queue.model.Message;

import java.util.concurrent.CompletableFuture;

/**
 * Created by imdb on 02/02/2018.
 */
public class ConcreteClient extends AbstractClient {
    private ClientReactor reactor;
    @Override
    public CompletableFuture<EchoMessage> echo(String messagse) {
        /**
         * <pre>
         *     1. Create a Future of Message
         *     2. Send it to Reactor (the real work is to put it in to the queue and get the Future
         *     3. return the Future to client
         * </pre>
         */
        CompletableFuture<EchoMessage> echoResponseFuture = new CompletableFuture<>();
        try {
            reactor.getQueue().put(echoResponseFuture);
        } catch (Exception ex) {
            ex.printStackTrace();
            // complete echoFuture with Exception
        }
        return echoResponseFuture;
    }

    @Override
    public CompletableFuture<Long> complicatedPlus(long num1, long num2) {
        return null;
    }

}
