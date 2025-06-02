package com.growtech.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;

import java.net.URI;

@Configuration
public class RsocketConfig {

  @Bean
  public RSocketRequester rSocketRequester(RSocketRequester.Builder builder,
                                           @Value("${rsocket.client.url}") String url) {

    return builder.websocket(URI.create(url));
  }

}
