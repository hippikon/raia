package digital.pensieve.demo;

import com.amazonaws.services.sqs.AmazonSQS;
import com.amazonaws.services.sqs.AmazonSQSClientBuilder;
import com.amazonaws.services.sqs.model.Message;
import com.amazonaws.services.sqs.model.ReceiveMessageRequest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

public class AWSSQS {

    public class SQS_RDS_Integration {

        public static void main(String[] args) {

            // Create an SQS client
            AmazonSQS sqs = AmazonSQSClientBuilder.defaultClient();

            // Connect to the SQS queue
            String queueUrl = sqs.getQueueUrl("your_queue_name").getQueueUrl();

            while(true) {
                // Receive messages from the queue
                ReceiveMessageRequest receiveMessageRequest = new ReceiveMessageRequest(queueUrl);
                List<Message> messages = sqs.receiveMessage(receiveMessageRequest).getMessages();

                if(!messages.isEmpty()) {
                    for(Message message : messages) {
                        // Get the message body
                        String messageBody = message.getBody();

                        try {
                            // Connect to the RDS database
                            Connection conn = DriverManager.getConnection("jdbc:mysql://your_rds_endpoint:port/your_db_name", "username", "password");
                            Statement stmt = conn.createStatement();

                            // Insert the message body into the RDS database
                            stmt.executeUpdate("INSERT INTO your_table (column1, column2, column3) VALUES ('" + messageBody + "', 'value2', 'value3')");

                            // Delete the message from the queue
                            sqs.deleteMessage(queueUrl, message.getReceiptHandle());
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    // Sleep for 10 seconds if there are no messages in the queue
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
