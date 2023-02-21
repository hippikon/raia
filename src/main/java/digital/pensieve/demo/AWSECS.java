package digital.pensieve.demo;

import digital.pensieve.demo.auth.AWSS;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ecs.EcsClient;
import software.amazon.awssdk.services.ecs.model.*;

@Component
public class AWSECS {

    @Autowired
    private AWSS awsAuth;

    private EcsClient ecs;

    private Ec2Client ec2Client;

    private String serviceArn;

    private String taskArn;

    private String clusterArn;

    @PostConstruct
    public void getECSClient() {

        // Create an EC2 client
        if (ec2Client == null) {
            ec2Client = Ec2Client.builder().credentialsProvider(awsAuth.generateCredentials())
                    .region(Region.US_EAST_1).build();
        }

        // Create an ECS client
        if (ecs == null) {
            ecs = EcsClient.builder().credentialsProvider(awsAuth.generateCredentials())
                    .region(Region.US_EAST_1).build();
        }
    }

    public String createCluster(String clusterName, String service, String taskSetName) {
        // Create a new ECS cluster
        CreateClusterRequest createClusterRequest = CreateClusterRequest.builder()
            .clusterName(clusterName)
            .build();
        CreateClusterResponse createClusterResponse = ecs.createCluster(createClusterRequest);
        clusterArn = createClusterResponse.cluster().clusterArn();

        KeyValuePair kvPair = KeyValuePair.builder()
                .name("ORACLE_SID")
                .value("db1")
                .build();

        // Register the task definition with the cluster
        RegisterTaskDefinitionRequest registerTaskDefinitionRequest = RegisterTaskDefinitionRequest.builder()
                .family("my-task-definition")
                .containerDefinitions(ContainerDefinition.builder()
                .name(taskSetName)
                .image("oracle:latest")
                .memory(1024)
                .cpu(512)
                .environment(kvPair)
                .build()).build();

        RegisterTaskDefinitionResponse registerTaskDefinitionResponse = ecs.registerTaskDefinition(registerTaskDefinitionRequest);
        taskArn = registerTaskDefinitionResponse.taskDefinition().taskDefinitionArn();

        CreateServiceRequest createServiceRequest = CreateServiceRequest.builder()
            .cluster(clusterArn)
            .serviceName(service)
            .taskDefinition(taskArn)
            .desiredCount(2).build();

        CreateServiceResponse response = ecs.createService(createServiceRequest);
        serviceArn = response.service().serviceArn();

//        CreateTaskSetRequest taskSetRequest = CreateTaskSetRequest.builder().cluster(clusterName).service(serviceArn).taskDefinition(taskArn).build();
//        ecs.createTaskSet(taskSetRequest);
        // Run a task using the new task definition
        RunTaskResponse runTaskResponse = ecs.runTask(r -> r.cluster(clusterArn).taskDefinition(taskArn));

        return serviceArn;
    }

    public String deleteCluster() {
        // Delete ECS cluster
        DeleteTaskSetRequest deleteTaskSetRequest = DeleteTaskSetRequest.builder().cluster(clusterArn).service(serviceArn).taskSet(taskArn).force(true).build();
        ecs.deleteTaskSet(deleteTaskSetRequest);

        DeleteServiceRequest deleteServiceRequest = DeleteServiceRequest.builder().cluster(clusterArn).service(serviceArn).force(true).build();
        ecs.deleteService(deleteServiceRequest);

        DeleteClusterRequest deleteClusterRequest = DeleteClusterRequest.builder()
                .cluster(clusterArn)
                .build();
        DeleteClusterResponse deleteClusterResponse = ecs.deleteCluster(deleteClusterRequest);
        String clusterArn = deleteClusterResponse.cluster().clusterArn();
        return clusterArn;
    }
}
