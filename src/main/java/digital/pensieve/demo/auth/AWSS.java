package digital.pensieve.demo.auth;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.EnvironmentVariableCredentialsProvider;
import software.amazon.awssdk.auth.credentials.SystemPropertyCredentialsProvider;

@Component
public class AWSS {

    private AWSCredentials credentials = null;

    private AwsCredentialsProvider provider = null;

    public AWSCredentials getAwsCredentials() {

        if (credentials == null) {
            // load from readme.md
            credentials = new BasicAWSCredentials(
                    "",
                    ""
            );
        }
        return credentials;
    }

    public AwsCredentialsProvider generateCredentials() {
        if (provider == null) {
            provider = SystemPropertyCredentialsProvider.create();
        }
        System.out.println(provider.resolveCredentials());
        return provider;
    }
}
