package com.growtech.api.services.sensorValue;

import com.growtech.api.entities.SensorValue;
import com.growtech.api.entities.User;
import org.springframework.cglib.core.Local;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;

@Service
public interface SensorValueService {

  Flux<SensorValue> getAllSensorValuesBySensorId(User user, String sensorId, String plantationId, Pair<LocalDateTime, LocalDateTime> dateTimePair);

}
