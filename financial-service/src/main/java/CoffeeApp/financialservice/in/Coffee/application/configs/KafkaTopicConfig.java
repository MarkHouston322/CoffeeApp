package CoffeeApp.financialservice.in.Coffee.application.configs;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic sessionTopic() {
        return TopicBuilder.name("sessions")
                .build();
    }

    @Bean
    public NewTopic transactionTopic() {
        return TopicBuilder.name("transactions")
                .build();
    }
}
