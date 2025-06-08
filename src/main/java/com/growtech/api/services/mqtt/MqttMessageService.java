package com.growtech.api.services.mqtt;

import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.springframework.stereotype.Service;

@Service
public interface MqttMessageService {
  void processMessage(String topic, MqttMessage message);
  void publishActuatorStatus(String plantationId, String sensorId, String newStatus);
}
