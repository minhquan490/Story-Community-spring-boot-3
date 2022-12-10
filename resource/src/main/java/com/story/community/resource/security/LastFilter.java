package com.story.community.resource.security;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import com.story.community.core.common.Constant;
import com.story.community.core.resource.service.CustomerService;
import com.story.community.core.utils.TokenUtils;
import com.story.community.resource.exception.UnAuthorizeException;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * This filter use for authorize customer
 * 
 * @author hoangquan
 */
@RequiredArgsConstructor
public class LastFilter implements WebFilter {
    private final CustomerService customerService;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        HttpHeaders headers = request.getHeaders();
        List<String> jwtHeaders = headers.get(Constant.AUTHORIZE_HEADER);
        if (jwtHeaders == null || jwtHeaders.isEmpty()) {
            throw new UnAuthorizeException("Login is required");
        }
        String jwt = jwtHeaders.get(0);
        exchange.getResponse().getHeaders().add(Constant.AUTHORIZE_HEADER, jwt);
        String username = null;
        try {
            username = TokenUtils.decode(jwt).getClaim("username");
        } catch (JwtException e) {
            doOnJwtException(e, exchange, jwt);
        }
        customerService
                .findByUsername(username)
                .doOnSuccess(this::processUserDetails)
                .doOnError(e -> doWhenUnAuthorize(e, jwt))
                .subscribe();
        return chain.filter(exchange);
    }

    /**
     * Add {@code UserDetails} to {@code SecurityContext}
     * 
     * @param context     the {@link SecurityContext}
     * @param userDetails user detail
     */
    private void addUserDetails(SecurityContext context, UserDetails userDetails) {
        context.setAuthentication(new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities()));
    }

    private void processUserDetails(UserDetails userDetails) {
        ReactiveSecurityContextHolder
                .getContext()
                .doOnSuccess(context -> addUserDetails(context, userDetails));
    }

    private void doWhenUnAuthorize(Throwable e, String jwt) {
        throw new UnAuthorizeException("Can't find customer with token " + jwt, e);
    }

    /**
     * Process {@link JwtException}, if jwt is expired jwt will be revoked by
     * refresh token and add to response
     * 
     * @param e        {@link JwtException}
     * @param exchange server exchange for extract response and request
     * @param jwt      old jwt
     */
    private void doOnJwtException(JwtException e, ServerWebExchange exchange, String jwt) {
        if (e instanceof JwtValidationException jve) {
            if (jve.getMessage().equals("Unable to validate Jwt")) {
                doWhenUnAuthorize(jve, jwt);
            } else {
                jwtValidationAvaible(jve, exchange, jwt);
            }
        } else {
            doWhenUnAuthorize(e, jwt);
        }
    }

    /**
     * Invoked when able to validate jwt. Check return error, revoke jwt with
     * refresh token and set them to response
     * 
     * @param jve      the {@code JwtValidationException} to process
     * @param exchange {@code ServerWebExchange} to get response and request in it
     * @param jwt      after extract request
     */
    private void jwtValidationAvaible(JwtValidationException jve, ServerWebExchange exchange, String jwt) {
        Collection<OAuth2Error> errors = jve.getErrors();
        for (OAuth2Error oAuth2Error : errors) {
            if (StringUtils.startsWithIgnoreCase(oAuth2Error.getDescription(), "Jwt expired")) {
                List<String> refreshHeaders = exchange.getRequest().getHeaders()
                        .get(Constant.REFRESH_TOKEN_HEADER);
                if (refreshHeaders == null || refreshHeaders.isEmpty()) {
                    doWhenUnAuthorize(jve, jwt);
                } else {
                    exchange.getResponse()
                            .getHeaders()
                            .add(Constant.AUTHORIZE_HEADER, revokeJwt(refreshHeaders.get(0)));
                    exchange.getResponse().getHeaders().add(Constant.REFRESH_TOKEN_HEADER, refreshHeaders.get(0));
                }
            }
        }
    }

    /**
     * Revoke to jwt with refresh token if refresh token is valid
     * 
     * @param refreshToken token for revoke jwt
     * @return revoked jwt
     */
    private String revokeJwt(String refreshToken) {
        String customerId = null;
        try {
            customerId = TokenUtils.decode(refreshToken).getClaim("customerId");
        } catch (Exception e) {
            doWhenUnAuthorize(e, refreshToken);
        }
        AtomicReference<String> atomicReference = new AtomicReference<>();
        customerService
                .findById(customerId)
                .doOnSuccess(c -> atomicReference
                        .set(TokenUtils.encodeJwt("username", c.getAccount().getUsername()).getTokenValue()))
                .doOnError(e -> doWhenUnAuthorize(e, refreshToken));
        return atomicReference.get();
    }
}
