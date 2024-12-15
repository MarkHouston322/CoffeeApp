package CoffeeApp.sellingservice.configs;

import CoffeeApp.sellingservice.serializers.LocalDateTimeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDateTime;

@Configuration
public class GsonConfig {

    @Bean
    public LocalDateTimeAdapter localDateTimeAdapter() {
        return new LocalDateTimeAdapter();
    }

    @Bean
    public Gson gson(LocalDateTimeAdapter localDateTimeAdapter) {
        return new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, localDateTimeAdapter)
                .create();
    }
}
