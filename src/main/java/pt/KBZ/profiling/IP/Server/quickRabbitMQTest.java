package IP.Server;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

public class quickRabbitMQTest {

	private final static String QUEUE_NAME = "analytics_consumer";

	  public static void main(String[] argv) throws Exception {
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setUsername("rabbitmq");
		factory.setPassword("rabbitmq");
		factory.setVirtualHost("/");
		factory.setHost("localhost");
		factory.setPort(5672);
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();

	    channel.queueDeclare(QUEUE_NAME, true, false, false, null);
	    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

	    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
	        String message = new String(delivery.getBody(), "UTF-8");
	        System.out.println(" [x] Received '" + message + "'");
	    };
	    channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
	  }
	  
	  
}