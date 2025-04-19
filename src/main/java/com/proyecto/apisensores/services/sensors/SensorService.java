package com.proyecto.apisensores.services.sensors;

import com.proyecto.apisensores.entities.Sensor;
import com.proyecto.apisensores.entities.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

@Service
public interface SensorService {
  Mono<Tuple2<List<Sensor>, Long>> getSensorsByPlantationPaginated(User user, String plantationId, PageRequest pageRequest);
}
