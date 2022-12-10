package com.story.community.core.common.support;

import org.springframework.data.domain.ReactiveAuditorAware;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;

import com.story.community.core.common.sercurity.PrincipalObject;
import com.story.community.core.common.sercurity.PrincipalWrapper;
import com.story.community.core.resource.entities.customer.Customer;

import reactor.core.publisher.Mono;

/**
 * Provide way to auditing when customer modify resource
 * 
 * @author hoangquan
 */
public class AuditorProvider implements ReactiveAuditorAware<Customer> {

    @Override
    public Mono<Customer> getCurrentAuditor() {
        PrincipalWrapper<Customer> wrapper = new PrincipalWrapper<>();
        ReactiveSecurityContextHolder.getContext()
                .doOnSuccess(c -> {
                    PrincipalObject principalObject = ((PrincipalObject) c.getAuthentication().getPrincipal());
                    wrapper.wrap(principalObject.getCustomer());
                })
                .subscribe();
        return Mono.just(wrapper.isPresent() ? wrapper.getPrincipal() : null);
    }

}
