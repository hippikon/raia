package digital.pensieve.demo.stash;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ecs.EcsClient;
import software.amazon.awssdk.services.ecs.model.*;

import javax.imageio.spi.RegisterableService;

public class ECS_Oracle_Cluster {

        public static void main(String[] args) {

            EcsClient ecs = EcsClient.builder()
                    .region(Region.US_EAST_1).build();


            // Create a new ECS cluster
            CreateClusterRequest createClusterRequest = CreateClusterRequest.builder()
                    .clusterName("your_cluster_name")
                    .build();
            CreateClusterResponse createClusterResponse = ecs.createCluster(createClusterRequest);
            String clusterArn = createClusterResponse.cluster().clusterArn();

            // Create a new task definition for the Oracle image
//            CreateTaskSetRequest createTaskSetRequest = CreateTaskSetRequest.builder()
//                    .family("your_task_definition_name")
//                    .containerDefinitions(
//                            ContainerDefinition.builder()
//                                    .name("oracle")
//                                    .image("oracle:latest")
//                                    .memory(1024)
//                                    .cpu(512)
//                                    .environment(
//                                            KeyValuePair.builder()
//                                                    .name("ORACLE_SID")
//                                                    .value("your_sid")
//                                                    .build()
//                                    )
//                                    .build()
//                    )
//                    .build();

            KeyValuePair kvPair = KeyValuePair.builder()
                    .name("ORACLE_SID")
                    .value("your_sid")
                    .build();


            // Register the task definition with the cluster
            RegisterTaskDefinitionRequest registerTaskDefinitionRequest = RegisterTaskDefinitionRequest.builder()
                    .family("example-task-definition")
                    .containerDefinitions(ContainerDefinition.builder()
                            .name("oracle")
                            .image("oracle:latest")
                            .memory(1024)
                            .cpu(512)
                            .environment(kvPair)
                            .build()).build();

//            CreateTaskSetResponse createTaskDefinitionResponse = ecs.createTaskSet(createTaskSetRequest);
//            String taskDefinitionArn = createTaskDefinitionResponse.taskSet().taskSetArn();

            RegisterTaskDefinitionResponse registerTaskDefinitionResponse = ecs.registerTaskDefinition(registerTaskDefinitionRequest);



            // Run a task using the new task definition
            ecs.runTask(r -> r.cluster(clusterArn).taskDefinition(registerTaskDefinitionResponse.taskDefinition().taskDefinitionArn()));

            // The task should now be running in the ECS cluster with the Oracle image
        }
}
