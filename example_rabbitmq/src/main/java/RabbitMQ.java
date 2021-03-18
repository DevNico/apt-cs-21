import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.GetResponse;
import java.io.IOException;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

public class RabbitMQ {

  public static void main(String[] args) {
    String uri = String.format("amqp://%s:%s@%s:%s", "guest", "guest", "localhost", "5672");

    try {
      ConnectionFactory connectionFactory = new ConnectionFactory();
      connectionFactory.setUri(uri);

      Connection connection = connectionFactory.newConnection();
      Channel channel = connection.createChannel();

      channel.queueDeclare("a", false, false, false, null);
      channel.queueDeclare("b", false, false, false, null);

      channel.exchangeDeclare("broadcast", BuiltinExchangeType.FANOUT);
      channel.queueBind("a", "broadcast", "");
      channel.queueBind("b", "broadcast", "");

      channel.basicPublish("broadcast", "", null, "Nachricht".getBytes());

      GetResponse response = channel.basicGet("a", true);
      System.out.println(new String(response.getBody()));

      channel.close();
      connection.close();

    } catch (URISyntaxException | NoSuchAlgorithmException | KeyManagementException e) {
      throw new SecurityException("Error setting url: " + uri, e);
    } catch (IOException | TimeoutException e) {
      throw new SecurityException("Error creating connection / channel to url: " + uri, e);
    }
  }
}