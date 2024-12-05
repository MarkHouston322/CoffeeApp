package CoffeeApp.gateway_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.time.LocalDateTime;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayServerApplication.class, args);
	}

	@Bean
	public RouteLocator eazyBankRouteConfig(RouteLocatorBuilder routeLocatorBuilder) {
		return routeLocatorBuilder.routes()
				.route(p -> p
						.path("/coffee-app/customer-service/**")
						.filters(f -> f.rewritePath("/coffee-app/customer-service/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
						.uri("lb://CUSTOMERS"))
				.route(p -> p
						.path("/coffee-app/employees-service/**")
						.filters(f -> f.rewritePath("/coffee-app/employees-service/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
						.uri("lb://EMPLOYEES"))
				.route(p -> p
						.path("/coffee-app/financial-service/**")
						.filters(f -> f.rewritePath("/coffee-app/financial-service/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
						.uri("lb://FINANCIAL"))
				.route(p -> p
						.path("/coffee-app/selling-service/**")
						.filters(f -> f.rewritePath("/coffee-app/selling-service/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
						.uri("lb://SELLING"))
				.route(p -> p
						.path("/coffee-app/storage-service/**")
						.filters(f -> f.rewritePath("/coffee-app/storage-service/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-Response-Time", LocalDateTime.now().toString()))
						.uri("lb://STORAGE"))
				.build();}

}
