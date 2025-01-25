package my.edu.um.umpoint.common.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaConfig {
    public final static String BATCH_ADD_CLOSURE_TOPIC = "UMPOINT-BATCH_ADD_CLOSURE";

    @Bean
    public NewTopic batchAddTopic() {
        return TopicBuilder.name(BATCH_ADD_CLOSURE_TOPIC)
                .partitions(1)
                .replicas(0)
                .build();
    }
}
