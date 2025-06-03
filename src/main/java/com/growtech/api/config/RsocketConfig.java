package com.growtech.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.messaging.rsocket.annotation.support.RSocketMessageHandler;

import java.net.URI;

@Configuration
public class RsocketConfig {

  @Bean
  public RSocketMessageHandler messageHandler() {
    return new RSocketMessageHandler();
  }

}
