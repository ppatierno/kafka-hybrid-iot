package kafka.iot;

import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;

import static org.apache.camel.component.amqp.AMQPComponent.amqpComponent;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * Apache Camel AMQP to Kafka application
 */
public final class App {

    public static final String ENV_VAR_AMQP_SERVER = "AMQP_SERVER";
    public static final String ENV_VAR_KAFKA_BOOTSTRAP_SERVER = "KAFKA_BOOTSTRAP_SERVER";
    public static final String ENV_VAR_AMQP_ADDRESS = "AMQP_ADDRESS";
    public static final String ENV_VAR_KAFKA_TOPIC = "KAFKA_TOPIC";

    public static final String DEFAULT_AMQP_SERVER = "localhost:5672";
    public static final String DEFAULT_KAFKA_BOOTSTRAP_SERVERS = "localhost:9092";
    public static final String DEFAULT_AMQP_ADDRESS = "my-topic";
    public static final String DEFAULT_KAFKA_TOPIC = "my-topic";

    public static void main(String[] args) throws Exception {

        String amqpServer = System.getenv(ENV_VAR_AMQP_SERVER);
        if (amqpServer == null) {
            amqpServer = DEFAULT_AMQP_SERVER;
        }
        
        CamelContext camelContext = new DefaultCamelContext();
        camelContext.addComponent("amqp-endpoint", amqpComponent(String.format("amqp://%s", amqpServer)));

        camelContext.addRoutes(new RouteBuilder(){
        
            @Override
            public void configure() throws Exception {

                String kafkaBootstrapServers = System.getenv(ENV_VAR_KAFKA_BOOTSTRAP_SERVER);
                if (kafkaBootstrapServers == null) {
                    kafkaBootstrapServers = DEFAULT_KAFKA_BOOTSTRAP_SERVERS;
                }

                String amqpAddress = System.getenv(ENV_VAR_AMQP_ADDRESS);
                if (amqpAddress == null) {
                    amqpAddress = DEFAULT_AMQP_ADDRESS;
                }

                String kafkaTopic = System.getenv(ENV_VAR_KAFKA_TOPIC);
                if (kafkaTopic == null) {
                    kafkaTopic = DEFAULT_KAFKA_TOPIC;
                }
                
                from("amqp-endpoint:queue:" + amqpAddress)
                .process(new Processor(){
                    
                    @Override
                    public void process(Exchange exchange) throws Exception {
                        System.out.println(exchange.getIn().getHeader("deviceid"));
                        String deviceId = String.valueOf(exchange.getIn().getHeader("deviceid"));
                        exchange.getIn().setHeader(KafkaConstants.KEY, deviceId);
                    }
                })
                .to("kafka:" + kafkaTopic + "?brokers=" + kafkaBootstrapServers)
                .routeId("amqp-kafka-route")
                .log("${body}");
            }
        
        });

        camelContext.start();

        Thread.sleep(Long.MAX_VALUE);
    }
}
