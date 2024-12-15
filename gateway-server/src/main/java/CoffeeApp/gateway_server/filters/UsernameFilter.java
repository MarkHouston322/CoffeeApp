package CoffeeApp.gateway_server.filters;

import org.keycloak.representations.AccessToken;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.keycloak.adapters.springsecurity.token.KeycloakAuthenticationToken;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.web.server.ServerWebExchange;

@Configuration
public class UsernameFilter extends AbstractGatewayFilterFactory<Object> {

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
}
