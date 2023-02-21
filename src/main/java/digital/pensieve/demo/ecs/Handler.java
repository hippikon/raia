package digital.pensieve.demo.ecs;

import software.amazon.awssdk.services.ecs.EcsClient;


public class Handler {
    private final EcsClient ecsClient;

    public Handler() {
        ecsClient = DependencyFactory.ecsClient();
    }

    public void sendRequest() {
        // TODO: invoking the api calls using ecsClient.
    }
}
