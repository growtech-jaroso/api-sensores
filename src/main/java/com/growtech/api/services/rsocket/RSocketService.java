package com.growtech.api.services.rsocket;

import com.growtech.api.entities.Sensor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public interface RSocketService {

  Mono<Sensor> getSensorId(String id);
}
