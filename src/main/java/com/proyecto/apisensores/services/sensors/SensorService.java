package com.proyecto.apisensores.services.sensors;

import com.proyecto.apisensores.entities.Sensor;
import com.proyecto.apisensores.entities.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface SensorService {
  Flux<Sensor> getSensorsByPlantationPaginated(User user, String plantationId, PageRequest pageRequest);
  Mono<Long> getTotalSensorsByPlantation(String plantationId);
}
