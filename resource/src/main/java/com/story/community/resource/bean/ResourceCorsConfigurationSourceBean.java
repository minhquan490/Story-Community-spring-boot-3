package com.story.community.resource.bean;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.story.community.core.common.Constant;

@Configuration
class ResourceCorsConfigurationSourceBean {

    @Bean
    UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource() {
        UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();

        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.setAllowedMethods(Collections.singletonList("*"));
        corsConfiguration.addAllowedOrigin(Constant.GATE_WAY_URL);
        corsConfiguration.addAllowedOrigin(Constant.AUTHENTICATION_SERVER_URL);

        configurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return configurationSource;
    }
}
