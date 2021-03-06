package kafka.iot;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;
import io.vertx.kafka.client.common.TopicPartition;
import io.vertx.kafka.client.consumer.KafkaConsumer;
import io.vertx.proton.ProtonClient;
import io.vertx.proton.ProtonConnection;
import io.vertx.proton.ProtonHelper;
import io.vertx.proton.ProtonSender;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.qpid.proton.amqp.messaging.Rejected;
import org.apache.qpid.proton.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.Set;

public class Control {

    private static final Logger log = LoggerFactory.getLogger(Control.class);

    private static final String SEEK = "seek";
    private static final String SEEK_TO_BEGIN = "seektobegin";
    private static final String STATUS = "status";
    private static final String COMMAND = "command";

    private static final String BOOTSTRAP_SERVERS = "BOOTSTRAP_SERVERS";
    private static final String CONSUMER_GROUPID = "CONSUMER_GROUPID";
    private static final String CONSUMER_TOPIC = "CONSUMER_TOPIC";
    private static final String CONSUMER_AUTO_OFFSET_RESET = "CONSUMER_AUTO_OFFSET_RESET";
    private static final String AMQP_SERVER = "AMQP_SERVER";
    private static final String AMQP_ADDRESS = "AMQP_ADDRESS";

    private static final String DEFAULT_BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String DEFAULT_CONSUMER_GROUPID = "consumer-app";
    private static final String DEFAULT_CONSUMER_TOPIC = "iot-temperature-max";
    private static final String DEFAULT_CONSUMER_AUTO_OFFSET_RESET = "earliest";
    private static final String DEFAULT_AMQP_SERVER = "localhost:5672";
    private static final String DEFAULT_AMQP_ADDRESS = "iot-control";


    private static Set<TopicPartition> assignedTopicPartitions;

    private static ProtonSender sender;

    public static void main(String[] args) {

        String bootstrapServers = System.getenv().getOrDefault(BOOTSTRAP_SERVERS, DEFAULT_BOOTSTRAP_SERVERS);
        String groupId = System.getenv().getOrDefault(CONSUMER_GROUPID, DEFAULT_CONSUMER_GROUPID);
        String topic = System.getenv().getOrDefault(CONSUMER_TOPIC, DEFAULT_CONSUMER_TOPIC);
        String autoOffsetReset = System.getenv().getOrDefault(CONSUMER_AUTO_OFFSET_RESET, DEFAULT_CONSUMER_AUTO_OFFSET_RESET);
        String amqpServer = System.getenv().getOrDefault(AMQP_SERVER, DEFAULT_AMQP_SERVER);
        String amqpAddress = System.getenv().getOrDefault(AMQP_ADDRESS, DEFAULT_AMQP_ADDRESS);

        Vertx vertx = Vertx.vertx();

        Router router = Router.router(vertx);

        BridgeOptions options = new BridgeOptions();
        options
                .addOutboundPermitted(new PermittedOptions().setAddress("dashboard"))
                .addOutboundPermitted(new PermittedOptions().setAddress("status"))
                .addInboundPermitted(new PermittedOptions().setAddress("config"))
                .addInboundPermitted(new PermittedOptions().setAddress("control"));

        router.route("/eventbus/*").handler(SockJSHandler.create(vertx).bridge(options));
        router.route().handler(StaticHandler.create().setCachingEnabled(false));

        HttpServer httpServer = vertx.createHttpServer();
        httpServer
                .requestHandler(router::accept)
                .listen(8080, done -> {

                    if (done.succeeded()) {
                        log.info("HTTP server started on port {}", done.result().actualPort());
                    } else {
                        log.error("HTTP server not started", done.cause());
                    }
                });

        Properties config = new Properties();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        config.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);

        KafkaConsumer<String, String> consumer = KafkaConsumer.create(vertx, config, String.class, String.class);
        consumer.handler(record -> {
            log.info("Received on topic={}, partition={}, offset={}, key={}, value={}",
                    record.topic(), record.partition(), record.offset(), record.key(), record.value());
            JsonObject json = new JsonObject();
            json.put("key", record.key());
            json.put("value", record.value());
            vertx.eventBus().publish("dashboard", json);
        });

        consumer.partitionsAssignedHandler(topicPartitions -> {
            assignedTopicPartitions = topicPartitions;
            TopicPartition topicPartition = assignedTopicPartitions.stream().findFirst().get();
            String status = String.format("Joined group = [%s], topic = [%s], partition = [%d]", groupId, topicPartition.getTopic(), topicPartition.getPartition());
            vertx.eventBus().publish("status", status);
        });

        consumer.subscribe(topic);

        vertx.eventBus().consumer("config", message -> {

            String body = message.body().toString();

            switch (body) {

                case STATUS:

                    if (assignedTopicPartitions != null) {
                        TopicPartition topicPartition = assignedTopicPartitions.stream().findFirst().get();
                        String status = String.format("Joined group = [%s], topic = [%s], partition = [%d]", groupId, topicPartition.getTopic(), topicPartition.getPartition());
                        vertx.eventBus().publish("status", status);
                    } else {
                        vertx.eventBus().publish("status", String.format("Joining group = [%s] ...", groupId));
                    }
                    break;
            }
        });

        vertx.eventBus().consumer("control", message -> {

            String body = message.body().toString();

            switch (body) {

                case COMMAND:
                    
                    String command = message.headers().get("command");
                    log.info("Sending control command: '{}'", command);

                    Message msg = ProtonHelper.message(amqpAddress, command);
                    sender.send(msg, delivery -> {
                        log.info("Message delivered {}", delivery.getRemoteState());
                        if (delivery.getRemoteState() instanceof Rejected) {
                            Rejected rejected = (Rejected) delivery.getRemoteState();
                            log.info("... but rejected {} {}", rejected.getError().getCondition(), rejected.getError().getDescription());
                        }
                    });
                    break;
            }
        });
        
        log.info("Connecting via AMQP to {}", amqpServer);
        ProtonClient client = ProtonClient.create(vertx);       

        String[] amqpServerParts = amqpServer.split(":");
        client.connect(amqpServerParts[0], Integer.valueOf(amqpServerParts[1]), done -> {

            if (done.succeeded()) {
                log.info("Connected via AMQP");
                ProtonConnection conn = done.result();
                conn.open();

                sender = conn.createSender(amqpAddress);
                sender.open();
            } else {
                log.error("Error connecting via AMQP", done.cause());
            }
        });
    }
}
