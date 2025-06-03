package com.growtech.api.services.rsocket;


import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class RsocketServiceImpl {

  private final RSocketRequester rSocketRequester;

  public RsocketServiceImpl(RSocketRequester rSocketRequester) {
    this.rSocketRequester = rSocketRequester;
  }
  public Mono<String> sendRequest(String message){
    return rSocketRequester.route("")
      .data(message)
      .retrieveMono(String.class);
  }

}
