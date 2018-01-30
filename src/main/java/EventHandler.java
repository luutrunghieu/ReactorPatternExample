import java.nio.channels.SelectionKey;

/**
 * Created by imdb on 29/01/2018.
 */
public interface EventHandler {
    public void handleEvent(SelectionKey handle) throws Exception;
}
