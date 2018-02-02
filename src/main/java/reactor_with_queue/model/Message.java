package reactor_with_queue.model;

import java.nio.ByteBuffer;

/**
 * Created by imdb on 02/02/2018.
 */
public abstract class Message<T> {
    public abstract void write(ByteBuffer byteBuffer);
    public abstract T read(ByteBuffer byteBuffer);
}
