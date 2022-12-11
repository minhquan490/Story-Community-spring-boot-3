package com.story.community.resource.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.web.server.authorization.ServerAccessDeniedHandler;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.story.community.core.common.Constant;
import com.story.community.core.common.sercurity.CustomAccessDeniedHandler;
import com.story.community.core.resource.entities.customer.Role;

import reactor.core.publisher.Mono;

/**
 * Filter check user has permission to access url
 * 
 * @author hoangquan
 */
public class PermissionFilter implements WebFilter {

    private static final Map<String, List<Role>> PERMISSTION_RULE;

    static {
        PERMISSTION_RULE = new HashMap<>();
        PERMISSTION_RULE.put("admin", Arrays.asList(Role.ADMIN));
        PERMISSTION_RULE.put("author", Arrays.asList(Role.ADMIN, Role.AUTHOR));
        PERMISSTION_RULE.put("reader", Arrays.asList(Role.ADMIN, Role.AUTHOR, Role.READER));
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        try {
            return process(exchange, chain);
        } catch (AccessDeniedException e) {
            return handleAccessDenied(exchange, e);
        }
    }

    private Mono<Void> process(ServerWebExchange exchange, WebFilterChain chain) {
        boolean isIgnore = checkIgnore(exchange);
        if (isIgnore) {
            return chain.filter(exchange);
        }
        List<String> role = new ArrayList<>();
        Authentication authentication = getAuthentication();
        if (authentication == null) {
            throw new AccessDeniedException("Can't access to this resources, login is required");
        } else {
            authentication.getAuthorities().forEach(g -> role.add(g.getAuthority()));
            List<Role> roles = new ArrayList<>();
            role.forEach(r -> roles.add(Role.valueOf(r)));
            String path = exchange.getRequest().getPath().value().split("/")[1];
            List<Role> permissionRoles = PERMISSTION_RULE.get(path);
            if (!permissionRoles.containsAll(roles)) {
                throw new AccessDeniedException("You can't access this resource");
            }
        }
        return chain.filter(exchange);
    }

    private Mono<Void> handleAccessDenied(ServerWebExchange exchange, AccessDeniedException exception) {
        ServerAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();
        return accessDeniedHandler.handle(exchange, exception);
    }

    private Authentication getAuthentication() {
        AtomicReference<Authentication> reference = new AtomicReference<>();
        ReactiveSecurityContextHolder
                .getContext()
                .doOnSuccess(context -> reference.set(context.getAuthentication()));
        return reference.get();
    }

    private boolean checkIgnore(ServerWebExchange exchange) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        List<String> ignoreHeder = headers.get(Constant.IGNORE_AUTHORIZE_HEADER);
        if (ignoreHeder != null && !ignoreHeder.isEmpty()) {
            String ignorePath = ignoreHeder.get(0);
            if (ignorePath.equals("true")) {
                return true;
            }
        }
        return false;
    }
}
