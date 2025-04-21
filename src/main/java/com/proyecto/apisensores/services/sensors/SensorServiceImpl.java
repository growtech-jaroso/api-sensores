package com.proyecto.apisensores.services.sensors;

import com.proyecto.apisensores.entities.Plantation;
import com.proyecto.apisensores.entities.Sensor;
import com.proyecto.apisensores.entities.User;
import com.proyecto.apisensores.exceptions.CustomException;
import com.proyecto.apisensores.repositories.PlantationRepository;
import com.proyecto.apisensores.repositories.SensorRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.util.List;

@Service
public class SensorServiceImpl implements SensorService {

  private final SensorRepository sensorRepository;
  private final PlantationRepository plantationRepository;

  public SensorServiceImpl(SensorRepository sensorRepository, PlantationRepository plantationRepository) {
    this.sensorRepository = sensorRepository;
    this.plantationRepository = plantationRepository;
  }

  @Override
  public Mono<Tuple2<List<Sensor>, Long>> getSensorsByPlantationPaginated(User user, String plantationId, PageRequest pageRequest) {
    // Check if plantation exists
    Mono<Plantation> plantation = this.plantationRepository.findById(plantationId)
      .switchIfEmpty(Mono.error(new CustomException(HttpStatus.NOT_FOUND, "Plantation not found")));

    return plantation
      .flatMap(pl -> {
        // Check if the user is associated with the plantation
        if (!pl.getUsers().contains(user.getId()) && !user.canViewAnything()) {
          return Mono.error(new CustomException(HttpStatus.FORBIDDEN, "Forbidden"));
        }

        // Get the sensors by plantation id
        return Mono.zip(
          this.sensorRepository.getAllByPlantationId(plantationId, pageRequest).collectList(),
          this.sensorRepository.countAllByPlantationId(plantationId)
        );
      });
  }
}
