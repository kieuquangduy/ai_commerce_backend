package com.duy.aicommerce.backend.user.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter

public class CustomerUserDetails extends User
{
    private final Long id;

    public CustomerUserDetails(
              Long id
            , String username
            , String password
              , boolean enabled
            , Collection<? extends GrantedAuthority> authorities) {

        super(username, password, enabled, true, true, true, authorities);
        this.id = id;
    }
}
