package com.story.community.core.bean.resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.ReactiveAuditorAware;

import com.story.community.core.common.support.AuditorProvider;
import com.story.community.core.resource.entities.customer.Customer;

@Configuration
public class AuditorProviderBean {

    @Bean
    ReactiveAuditorAware<Customer> auditorProvider() {
        return new AuditorProvider();
    }
}
