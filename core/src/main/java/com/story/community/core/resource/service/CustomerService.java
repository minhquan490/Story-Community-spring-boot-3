package com.story.community.core.resource.service;

import org.springframework.security.core.userdetails.ReactiveUserDetailsService;

import com.story.community.core.resource.entities.customer.Customer;

public interface CustomerService extends ReactiveUserDetailsService, Service<Customer> {
}
