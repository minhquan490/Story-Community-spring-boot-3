package com.story.community.core.common.sercurity;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.ServerAuthenticationEntryPoint;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

public class CustomServerEntryPoint implements ServerAuthenticationEntryPoint {
    private static final int UNAUTHORIZE_CODE = 401;

    @Override
    public Mono<Void> commence(ServerWebExchange exchange, AuthenticationException ex) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatusCode.valueOf(UNAUTHORIZE_CODE));
        return response.writeWith(Mono.just(response.bufferFactory().wrap(ex.getMessage().getBytes())));
    }

}