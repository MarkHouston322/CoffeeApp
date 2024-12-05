package CoffeeApp.sellingservice.configs;

import CoffeeApp.sellingservice.dto.messages.ProceedOrderMessage;
import CoffeeApp.sellingservice.dto.messages.SoldDrinkMessage;
import CoffeeApp.sellingservice.serializers.OrderSerializer;
import CoffeeApp.sellingservice.serializers.SoldDrinkSerializer;
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

    public Map<String, Object> orderProducerConfig(){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, OrderSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, ProceedOrderMessage> orderProducerFactory(){
        return new DefaultKafkaProducerFactory<>(orderProducerConfig());
    }

    @Bean
    public KafkaTemplate<String, ProceedOrderMessage> orderKafkaTemplate(ProducerFactory<String, ProceedOrderMessage> orderProducerFactory){
        return new KafkaTemplate<>(orderProducerFactory);
    }

    public Map<String, Object> soldDrinkProducerConfig(){
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServer);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, SoldDrinkSerializer.class);
        return props;
    }

    @Bean
    public ProducerFactory<String, SoldDrinkMessage> soldDrinkProducerFactory(){
        return new DefaultKafkaProducerFactory<>(soldDrinkProducerConfig());
    }

    @Bean
    public KafkaTemplate<String, SoldDrinkMessage> drinkKafkaTemplate(ProducerFactory<String, SoldDrinkMessage> soldDrinkProducerFactory){
        return new KafkaTemplate<>(soldDrinkProducerFactory);
    }
}
