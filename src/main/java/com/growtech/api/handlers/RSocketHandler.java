package com.growtech.api.handlers;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RSocketHandler {

  @MessageMapping("example.route")
  public Mono<String> handlerRequest(String message){
    return Mono.just("Recibido: " + message);
  }

}
