package reactor_with_queue.client;

import reactor_with_queue.model.EchoMessage;
import reactor_with_queue.model.Message;

import java.util.concurrent.CompletableFuture;

/**
 * Created by imdb on 02/02/2018.
 */
public abstract class AbstractClient {
    public abstract CompletableFuture<EchoMessage> echo(String messagse);
    public abstract CompletableFuture<Long> complicatedPlus(long num1, long num2);
}
