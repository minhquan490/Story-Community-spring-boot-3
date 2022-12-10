package com.story.community.core.common.sercurity;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.story.community.core.resource.entities.customer.Account;
import com.story.community.core.resource.entities.customer.Customer;

import io.netty.util.internal.StringUtil;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@RequiredArgsConstructor
@NoArgsConstructor
@Setter
public class PrincipalObject implements UserDetails {

    @NonNull
    private Object id;

    @NonNull
    private Customer customer;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        GrantedAuthority authority = new SimpleGrantedAuthority(customer.getRole().name());
        authorities.add(authority);
        return authorities;
    }

    @Override
    public String getPassword() {
        return StringUtil.EMPTY_STRING;
    }

    @Override
    public boolean isAccountNonExpired() {
        return getAccount().isAccountNonExpired();
    }

    @Override
    public boolean isAccountNonLocked() {
        return getAccount().isAccountNonLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return getAccount().isEnabled();
    }

    @Override
    public String getUsername() {
        return getAccount().getUsername();
    }

    private Account getAccount() {
        return customer.getAccount();
    }
}
