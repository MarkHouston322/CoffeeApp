package CoffeeApp.financialservice.in.Coffee.application.configs;

import CoffeeApp.financialservice.in.Coffee.application.dto.messages.ProceedSessionMessage;
import CoffeeApp.financialservice.in.Coffee.application.dto.messages.ProceedTransactionMessage;
import CoffeeApp.financialservice.in.Coffee.application.serializers.SessionSerializer;
import CoffeeApp.financialservice.in.Coffee.application.serializers.TransactionSerializer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServer;

    public Map<String, Object> sessionProducerConfig(){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, SessionSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, ProceedSessionMessage> sessionProducerFactory(){
        return new DefaultKafkaProducerFactory<>(sessionProducerConfig());
    }

    @Bean
    public KafkaTemplate<String, ProceedSessionMessage> sessionKafkaTemplate(ProducerFactory<String, ProceedSessionMessage> sessionProducerFactory){
        return new KafkaTemplate<>(sessionProducerFactory);
    }

    public Map<String, Object> transactionProducerConfig(){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, TransactionSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, ProceedTransactionMessage> transactionProducerFactory(){
        return new DefaultKafkaProducerFactory<>(transactionProducerConfig());
    }

    @Bean
    public KafkaTemplate<String, ProceedTransactionMessage> transactionKafkaTemplate(ProducerFactory<String, ProceedTransactionMessage> transactionProducerFactory){
        return new KafkaTemplate<>(transactionProducerFactory);
    }

}
