package com.story.community.core.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.source.ImmutableSecret;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

public class TokenUtils {
    private TokenUtils() {
        throw new UnsupportedOperationException("Can't instance JwtUtils");
    }

    private static final String SECRET_KEY = "60fe2813-a157-452c-92c1-14c0c736dc3a";
    private static final String ALGORITHM = "HmacSHA256";
    private static final String JWS_HEADER_ALGORITHM = "HS256";
    private static final int MINUS_EXPIRE = 60;
    private static final JwtEncoder INTERNAL_JWT_ENCODER;
    private static final JwtDecoder INTERAL_JWT_DECODER;

    static {
        INTERNAL_JWT_ENCODER = encoder();
        INTERAL_JWT_DECODER = decoder();
    }

    private static JwtEncoder encoder() {
        SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        JWKSource<SecurityContext> immutableSecret = new ImmutableSecret<>(secretKey);
        return new NimbusJwtEncoder(immutableSecret);
    }

    private static JwtDecoder decoder() {
        SecretKey secretKey = new SecretKeySpec(SECRET_KEY.getBytes(), ALGORITHM);
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }

    private static Instant calculateJwtExpiredTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.plusMinutes(MINUS_EXPIRE).toInstant(ZoneOffset.UTC);
    }

    private static Instant calculateRefreshTokenExpiredTime() {
        LocalDateTime now = LocalDateTime.now();
        return now.plusYears(1).toInstant(ZoneOffset.UTC);
    }

    public static Jwt encodeJwt(String key, Object value) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put(key, value);
        return encodeJwt(payload);
    }

    public static Jwt encodeJwt(Map<String, Object> payload) {
        JwtClaimsSet claimsSet = JwtClaimsSet
                .builder()
                .claims(c -> c.putAll(payload))
                .expiresAt(calculateJwtExpiredTime())
                .build();
        JwsHeader jwsHeader = JwsHeader.with(() -> JWS_HEADER_ALGORITHM).build();
        return INTERNAL_JWT_ENCODER.encode(JwtEncoderParameters.from(jwsHeader, claimsSet));
    }

    public static Jwt encodeRefreshToken(String key, Object value) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put(key, value);
        return encodeRefreshToken(payload);
    }

    public static Jwt encodeRefreshToken(Map<String, Object> payload) {
        JwtClaimsSet claimsSet = JwtClaimsSet
                .builder()
                .claims(c -> c.putAll(payload))
                .expiresAt(calculateRefreshTokenExpiredTime())
                .build();
        JwsHeader jwsHeader = JwsHeader.with(() -> JWS_HEADER_ALGORITHM).build();
        return INTERNAL_JWT_ENCODER.encode(JwtEncoderParameters.from(jwsHeader, claimsSet));
    }

    public static Jwt decode(String token) {
        return INTERAL_JWT_DECODER.decode(token);
    }
}
