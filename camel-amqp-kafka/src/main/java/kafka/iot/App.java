package kafka.iot;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import static org.apache.camel.component.amqp.AMQPComponent.amqpComponent;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * Hello world!
 */
public final class App {
    private App() {
    }

    /**
     * Says hello to the world.
     * @param args The arguments of the program.
     */
    public static void main(String[] args) throws Exception {
        
        CamelContext camelContext = new DefaultCamelContext();
        camelContext.addComponent("amqp-component", amqpComponent("amqp://localhost:5672"));

        camelContext.addRoutes(new RouteBuilder(){
        
            @Override
            public void configure() throws Exception {
                
                from("amqp-component:queue:my-topic")
                .to("kafka:my-topic?brokers=localhost:9092")
                .log("${body}");
            }
        
        });

        camelContext.start();

        Thread.sleep(Long.MAX_VALUE);
    }
}
