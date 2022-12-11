package com.story.community.core.common.sercurity;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

public class CustomAccessDeniedHandler implements ServerAccessDeniedHandler {
    private static final int ACCESS_DENIED_CODE = 403;

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, AccessDeniedException denied) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatusCode.valueOf(ACCESS_DENIED_CODE));
        return response.writeWith(Mono.just(response.bufferFactory().wrap(denied.getMessage().getBytes())));
    }

}
