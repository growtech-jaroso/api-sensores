package com.growtech.api.security;

import com.growtech.api.enums.UserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketStrategies;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.rsocket.EnableRSocketSecurity;
import org.springframework.security.config.annotation.rsocket.RSocketSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.rsocket.core.PayloadSocketAcceptorInterceptor;
import org.springframework.security.rsocket.metadata.BearerTokenAuthenticationEncoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableRSocketSecurity
public class RSocketSecurityConfig {

  @Value("${app.security.jwt.secret}")
  private String jwtSecret;

  @Bean
  public PayloadSocketAcceptorInterceptor rsocketInterceptor(RSocketSecurity rsocket) {
    return rsocket
      .authorizePayload(authorize -> authorize
        .setup()
        .authenticated()
        .anyRequest().authenticated()
      )
      .jwt(Customizer.withDefaults()) // Aceptas JWT
      .build();
  }

  @Bean
  public RSocketStrategies rSocketStrategies() {
    return RSocketStrategies.builder()
      .encoders(encoders -> encoders.add(new BearerTokenAuthenticationEncoder()))
      .build();
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    String secret = jwtSecret;
    SecretKey secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    return NimbusJwtDecoder.withSecretKey(secretKey).build();
  }
}
