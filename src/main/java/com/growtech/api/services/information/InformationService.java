package com.growtech.api.services.information;

import com.growtech.api.enums.DeviceType;
import com.growtech.api.enums.SensorType;
import com.growtech.api.enums.SensorUnit;
import com.growtech.api.enums.UserRole;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public interface InformationService {
  Mono<List<UserRole>> getAllUserRoles();
  Mono<List<SensorType>> getAllSensorTypes();
  Mono<List<SensorUnit>> getAllSensorUnits();
  Mono<List<DeviceType>> getAllDeviceSensor();
}
