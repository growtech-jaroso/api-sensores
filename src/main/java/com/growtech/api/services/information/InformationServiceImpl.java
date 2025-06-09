package com.growtech.api.services.information;

import com.growtech.api.enums.DeviceType;
import com.growtech.api.enums.SensorType;
import com.growtech.api.enums.SensorUnit;
import com.growtech.api.enums.UserRole;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class InformationServiceImpl implements InformationService {
  @Override
  public Mono<List<UserRole>> getAllUserRoles() {
    return Mono.just(List.of(UserRole.values()));
  }

  @Override
  public Mono<List<SensorType>> getAllSensorTypes() {
    return Mono.just(List.of(SensorType.values()));
  }

  @Override
  public Mono<List<SensorUnit>> getAllSensorUnits() {
    return Mono.just(List.of(SensorUnit.values()));
  }

  @Override
  public Mono<List<DeviceType>> getAllDeviceSensor() {
    return Mono.just(List.of(DeviceType.values()));
  }
}
