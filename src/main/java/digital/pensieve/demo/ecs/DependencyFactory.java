
package digital.pensieve.demo.ecs;

import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.services.ecs.EcsClient;

/**
 * The module containing all dependencies required by the {@link Handler}.
 */
public class DependencyFactory {

    private DependencyFactory() {}

    /**
     * @return an instance of EcsClient
     */
    public static EcsClient ecsClient() {
        return EcsClient.builder()
                       .httpClientBuilder(ApacheHttpClient.builder())
                       .build();
    }
}
