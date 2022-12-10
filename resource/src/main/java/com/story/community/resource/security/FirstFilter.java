package com.story.community.resource.security;

import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

/**
 * The first filter of resource server. This filter is used for tracking
 * customer
 * 
 * @author hoangquan
 */
@Log4j2
public class FirstFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        RequestPath path = request.getPath();
        log.info("Request come from [{}] use method [{}] to path [{}]",
                request.getRemoteAddress(),
                request.getMethod().name(),
                path.pathWithinApplication().value());
        return chain.filter(exchange);
    }

}
