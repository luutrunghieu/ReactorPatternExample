package reactor_with_queue.client;

/**
 * Created by imdb on 02/02/2018.
 */
public class RunConcreteClient {
    public static final long NUM_REQUEST = 1000;

    public static void main(String[] args) {
        // initial a monitor here
        AbstractClient client = new ConcreteClient();
        for (int i = 0; i < NUM_REQUEST; i++) {
            String messsage = ""; // initial it
            client.echo(messsage)
                    .whenComplete((message, throwable) -> {
                        if (throwable != null) {
                            throwable.printStackTrace();
                            // montior increase error count by 1
                        } else {
                            // montior increase success count by 1
                        }
                    });
        }
    }
}
