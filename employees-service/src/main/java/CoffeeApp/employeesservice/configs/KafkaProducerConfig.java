package CoffeeApp.employeesservice.configs;

import CoffeeApp.employeesservice.dto.messages.ProceedSalaryMessage;
import CoffeeApp.employeesservice.serializers.SalarySerializer;
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

    public Map<String, Object> salaryProducerConfig(){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, SalarySerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, ProceedSalaryMessage> salaryProducerFactory(){
        return new DefaultKafkaProducerFactory<>(salaryProducerConfig());
    }

    @Bean
    public KafkaTemplate<String, ProceedSalaryMessage> salaryKafkaTemplate(ProducerFactory<String, ProceedSalaryMessage> salaryProducerFactory){
        return new KafkaTemplate<>(salaryProducerFactory);
    }
}
