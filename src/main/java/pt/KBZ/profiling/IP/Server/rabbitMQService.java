package IP.Server;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import com.rabbitmq.client.Channel;

public class rabbitMQService {
	
	public ConnectionFactory factory;
	public Connection conn;
	public Channel channel;
	public String exchange;
	private String queue;
	public static final String ROUTING_KEY = "analytics_consumer";
	
	public rabbitMQService() {
		factory = new ConnectionFactory();
		factory.setUsername("rabbitmq");
		factory.setPassword("rabbitmq");
		factory.setVirtualHost("/");
		factory.setHost("qualichain.epu.ntua.gr");
		factory.setPort(5672);
		this.exchange = "";
		try {
			conn = factory.newConnection();
			System.out.println(conn.getServerProperties());
			channel = conn.createChannel();
//			channel.exchangeDeclare(exchange, "direct", true);
			
			channel.queueDeclarePassive(ROUTING_KEY);
//			channel.queueDeclare(ROUTING_KEY, true, false, false, null);
//			String queueName = channel.queueDeclare().getQueue(); 
			
//			channel.queueBind(ROUTING_KEY, exchange, ROUTING_KEY);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void bindQueue(String routingKey) {
		try {
//			channel.exchangeDeclare("", "direct", true);
//			String queueName = channel.queueDeclare().getQueue();
			channel.queueBind("analytics_consumer", "", ROUTING_KEY);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public void disableConnection() {
		try {
			channel.close();
			conn.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
