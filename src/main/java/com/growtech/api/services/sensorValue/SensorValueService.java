package com.growtech.api.services.sensorValue;

import com.growtech.api.entities.SensorValue;
import com.growtech.api.entities.User;
import com.growtech.api.enums.SensorType;
import com.growtech.api.services.sensors.SensorIdProjection;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface SensorValueService {

  Flux<SensorValue> getAllSensorValuesBySensorId(User user, String sensorId, String plantationId, Pair<LocalDateTime, LocalDateTime> dateTimePair);

  Flux<SensorIdProjection> getAllSensorValuesByPlantation(String plantationId, Pair<LocalDateTime, LocalDateTime> checkedDates);

  Flux<Object> getAllSensorValuesByTypeByPlantation(String plantationId, String sensorType, Pair<LocalDateTime, LocalDateTime> checkedDates);

  Flux<SensorValue> getAllSensorValuesBySensorIdAndPlantationId(String sensorId, String plantationId, Pair<LocalDateTime, LocalDateTime> dateTimePair);

  Flux<SensorValue> getAllBySensorIdIn(List<String> sensorIds);

  Flux<SensorValue> getAllBySensorIdInAndReadingTimestampBetween(List<String> sensorIds, LocalDateTime timestampAfter, LocalDateTime timestampBefore);
}
