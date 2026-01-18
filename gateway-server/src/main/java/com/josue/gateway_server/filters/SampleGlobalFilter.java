package com.josue.gateway_server.filters;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class SampleGlobalFilter implements GlobalFilter, Ordered {

    private final Logger logger = LoggerFactory.getLogger(SampleGlobalFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        logger.info("SampleGlobalFilter, ejecutando el filtro antes del request, pre-filter");
        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(
                        exchange.getRequest()
                                .mutate()
                                .header("token", "token21393lxm")
                                .build()
                )
                .build();

        return chain.filter(exchange)
                .then(
                        Mono.fromRunnable(() -> {
                            logger.info("SampleGlobalFilter, ejecutando el filtro despues del request, post-filter");
                            String token = mutatedExchange.getRequest().getHeaders().getFirst("token");
                            if (token != null) {
                                logger.info("Token: " + token);
                            }

                            // exchange.getResponse().getHeaders().setContentType(MediaType.TEXT_PLAIN);
                        })
                );
    }

    @Override
    public int getOrder() {
        return 100;
    }
}
