package com.growtech.api.services.sensors;

import com.growtech.api.dtos.mqtt.SensorStatusDto;
import com.growtech.api.dtos.requests.SensorDto;
import com.growtech.api.entities.Sensor;
import com.growtech.api.entities.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

@Service
public interface SensorService {
  Mono<Tuple2<List<Sensor>, Long>> getSensorsByPlantationPaginated(User user, String plantationId, PageRequest pageRequest);
  Mono<Sensor> createSensor(SensorDto sensorDto, String plantationId);
  Mono<Sensor> createActuatorSensor(String plantationId);

  Mono<Sensor> updateActuatorSensor(String sensorId, String plantationId, SensorStatusDto statusDto);

  Mono<String> deleteSensor(String sensorId, String plantationId);

}
