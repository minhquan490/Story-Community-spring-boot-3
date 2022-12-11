package com.story.community.resource.security;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.http.server.RequestPath;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.story.community.core.common.Constant;

import reactor.core.publisher.Mono;

public class IgnorePathFilter implements WebFilter {
    public static final List<String> PATH_IGNORED;

    static {
        PATH_IGNORED = Collections.unmodifiableList(Arrays.asList("/", "/home"));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest oldHttpServerRequest = exchange.getRequest();
        RequestPath path = exchange.getRequest().getPath();
        ServerWebExchange newExchange = null;
        if (PATH_IGNORED.contains(path.value())) {
            ServerWebExchange.Builder builder = exchange.mutate();
            builder.request(b -> {
                b.method(oldHttpServerRequest.getMethod());
                b.contextPath(path.contextPath().value());
                b.path(path.value());
                b.uri(oldHttpServerRequest.getURI());
                b.headers(h -> h.addAll(oldHttpServerRequest.getHeaders()));
                b.sslInfo(oldHttpServerRequest.getSslInfo());
                b.remoteAddress(oldHttpServerRequest.getRemoteAddress());
                b.header(Constant.IGNORE_AUTHORIZE_HEADER, "true");
            });
            builder.principal(exchange.getPrincipal());
            builder.response(exchange.getResponse());
            newExchange = builder.build();
        }
        return chain.filter(newExchange == null ? exchange : newExchange);
    }

}
