package com.story.community.resource.bean;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.csrf.CookieServerCsrfTokenRepository;

import com.story.community.core.common.sercurity.CustomAccessDeniedHandler;
import com.story.community.core.common.sercurity.CustomServerEntryPoint;
import com.story.community.core.resource.entities.customer.Role;
import com.story.community.core.resource.service.CustomerService;
import com.story.community.resource.security.FirstFilter;
import com.story.community.resource.security.LastFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
class SecurityWebFilterChainBean {
    private final CustomerService customerService;

    @Bean
    SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity httpSecurity) {
        return httpSecurity
                .csrf(csrf -> csrf.csrfTokenRepository(CookieServerCsrfTokenRepository.withHttpOnlyFalse()))
                .cors()
                .and()
                .redirectToHttps()
                .and()
                .logout()
                .disable()
                .formLogin()
                .disable()
                .authorizeExchange()
                .pathMatchers("/admin/**").hasAuthority(Role.ADMIN.name())
                .pathMatchers("/author/**").hasAuthority(Role.AUTHOR.name())
                .pathMatchers("/reader/**").hasAuthority(Role.READER.name())
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new CustomServerEntryPoint())
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .and()
                .anonymous()
                .disable()
                .httpBasic()
                .disable()
                .headers()
                .cache()
                .disable()
                .and()
                .addFilterBefore(new FirstFilter(), SecurityWebFiltersOrder.FIRST)
                .addFilterAfter(new LastFilter(customerService), SecurityWebFiltersOrder.LAST)
                .build();
    }
}
