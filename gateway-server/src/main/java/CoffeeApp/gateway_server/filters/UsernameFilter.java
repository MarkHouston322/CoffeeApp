package CoffeeApp.gateway_server.filters;

import org.keycloak.representations.AccessToken;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

import java.security.Principal;
import java.util.List;

@Configuration
public class UsernameFilter extends AbstractGatewayFilterFactory<Object>  {

    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> ReactiveSecurityContextHolder.getContext()
                .filter(c -> c.getAuthentication() instanceof KeycloakAuthenticationToken)
                .map(c -> (KeycloakAuthenticationToken) c.getAuthentication())
                .doOnNext(keycloakAuthenticationToken -> {
                    AccessToken accessToken = keycloakAuthenticationToken.getAccount().getKeycloakSecurityContext().getToken();
                    String tokenValue = accessToken.toString();
                    mutateRequest(exchange, tokenValue);
                })
                .then(chain.filter(exchange));
    }

    private void mutateRequest(ServerWebExchange exchange, String username) {
        ServerHttpRequest newRequest = exchange.getRequest().mutate()
                .header("Preferred-Username", username)
                .build();
        exchange.mutate().request(newRequest).build();
    }

//    private void mutateRequest(ServerWebExchange exchange, String jwt) {
//        ServerHttpRequest newRequest = exchange.getRequest().mutate()
//                .header("Authorization", "Bearer " + jwt)
//                .build();
//        exchange.mutate().request(newRequest).build();
//    }

//    @Bean
//    @Order(Ordered.HIGHEST_PRECEDENCE)
//    public WebFilter tokenRelayFilter() {
//        return (exchange, chain) -> {
//            exchange.getRequest()
//                    .mutate()
//                    .headers(headers -> headers.add("Authorization", exchange.getPrincipal().map(principal -> {
//                        if (principal instanceof OAuth2AuthenticatedPrincipal) {
//                            return "Bearer " + ((OAuth2AuthenticatedPrincipal) principal).getAttributes();
//                        }
//                        return "";
//                    }).block()));
//            return chain.filter(exchange);
//        };
//    }

//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        exchange.getRequest()
//                .mutate()
//                .headers(headers -> headers.add("Authorization", exchange.getPrincipal().map(principal -> {
//                    if (principal instanceof OAuth2AuthenticatedPrincipal) {
//                        return "Bearer " + ((OAuth2AuthenticatedPrincipal) principal).getAttributes();
//                    }
//                    return "";
//                }).block()));
//        return chain.filter(exchange);
//    }
}
