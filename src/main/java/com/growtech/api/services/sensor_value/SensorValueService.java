package com.growtech.api.services.sensor_value;

import com.growtech.api.dtos.mqtt.SensorValueDto;
import com.growtech.api.entities.SensorValue;
import com.growtech.api.entities.User;
import com.growtech.api.enums.SensorType;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public interface SensorValueService {

  Flux<SensorValue> getAllSensorValuesBySensorId(User user, String sensorId, String plantationId, Pair<LocalDateTime, LocalDateTime> dateTimePair);

  Flux<SensorValue> getAllSensorValuesByTypeByPlantation(User user, String plantationId, SensorType sensorType, Pair<LocalDateTime, LocalDateTime> checkedDates);
}
