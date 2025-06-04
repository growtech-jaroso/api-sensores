package com.growtech.api.services.rsocket;


import com.growtech.api.entities.Sensor;
import com.growtech.api.repositories.sensor.SensorRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class RsocketServiceImpl implements RSocketService{

  private final SensorRepository sensorRepository;

  public RsocketServiceImpl(SensorRepository sensorRepository) {
    this.sensorRepository = sensorRepository;
  }

  @Override
  public Mono<Sensor> getSensorId(String id) {
    return this.sensorRepository.findById(id);
  }
}
