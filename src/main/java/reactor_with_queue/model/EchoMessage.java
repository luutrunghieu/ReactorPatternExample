package reactor_with_queue.model;

import java.nio.ByteBuffer;

/**
 * Created by imdb on 02/02/2018.
 */
public class EchoMessage extends Message<String> {
    private String message;

    @Override
    public void write(ByteBuffer byteBuffer) {

    }

    @Override
    public String read(ByteBuffer byteBuffer) {
        return null;
    }
}
