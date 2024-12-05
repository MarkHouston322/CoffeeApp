package CoffeeApp.sellingservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
@EnableFeignClients
@EnableCaching
@EnableDiscoveryClient
public class SellingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SellingServiceApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		return mapper;
	}


//	@Bean
//	public SimpleDateFormat dateFormat(){
//		return new SimpleDateFormat("dd.MM.yyyy HH:mm");
//	}

//	@Bean
//	public Jackson2ObjectMapperBuilder objectMapperBuilder() {
//		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
//		builder.featuresToEnable(DeserializationFeature.READ_DATE_TIMESTAMPS_AS_NANOSECONDS);
//		builder.modulesToInstall(new JavaTimeModule());
//		return builder;
//	}


}
