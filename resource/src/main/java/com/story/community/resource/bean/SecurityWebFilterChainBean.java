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
import com.story.community.resource.security.AuthorizeFilter;
import com.story.community.resource.security.IgnorePathFilter;
import com.story.community.resource.security.LoggingFilter;

@Configuration
class SecurityWebFilterChainBean {
    private final CustomerService customerService;

    public SecurityWebFilterChainBean(CustomerService customerService) {
        super();
        this.customerService = customerService;
    }

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
                .anonymous()
                .and()
                .authorizeExchange()
                .pathMatchers(IgnorePathFilter.PATH_IGNORED.toArray(new String[0])).permitAll()
                .pathMatchers("/admin/**").hasAuthority(Role.ADMIN.name())
                .pathMatchers("/author/**").hasAuthority(Role.AUTHOR.name())
                .pathMatchers("/reader/**").hasAuthority(Role.READER.name())
                .and()
                .httpBasic()
                .disable()
                .headers()
                .cache()
                .disable()
                .and()
                .addFilterAt(new LoggingFilter(), SecurityWebFiltersOrder.FIRST)
                .addFilterAt(new IgnorePathFilter(), SecurityWebFiltersOrder.HTTP_HEADERS_WRITER)
                .addFilterAt(new AuthorizeFilter(customerService), SecurityWebFiltersOrder.AUTHENTICATION)
                .exceptionHandling()
                .accessDeniedHandler(new CustomAccessDeniedHandler())
                .authenticationEntryPoint(new CustomServerEntryPoint())
                .and()
                .build();
    }
}
