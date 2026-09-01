package com.duy.aicommerce.backend.common.security;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    private String secret;

    private long accessExpiration;

    private long refreshExpiration;
}
