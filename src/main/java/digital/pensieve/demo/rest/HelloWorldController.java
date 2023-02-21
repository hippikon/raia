package digital.pensieve.demo.rest;

import digital.pensieve.demo.AWSECS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @Autowired
    AWSECS ecs;

    @GetMapping("/")
    public String helloWorld() {
        return "Hello World";
    }

    @GetMapping("/create/ecs")
    public String createEcs() {
        return ecs.createCluster("new_oracle_cluster", "oracle_db_service", "start_oracle_server");
    }

    @GetMapping("/delete/ecs")
    public String deleteEcs() {
        return ecs.deleteCluster();
    }

}
