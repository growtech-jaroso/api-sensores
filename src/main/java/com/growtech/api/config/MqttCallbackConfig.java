package com.growtech.api.config;

import com.growtech.api.services.mqtt.MqttMessageService;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.IMqttToken;
import org.eclipse.paho.mqttv5.client.MqttCallback;
import org.eclipse.paho.mqttv5.client.MqttClient;
import org.eclipse.paho.mqttv5.client.MqttDisconnectResponse;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

@Slf4j
public class MqttCallbackConfig implements MqttCallback {

  private final MqttClient mqttClient;
  private final MqttMessageService mqttMessageService;
  private final List<String> topics = List.of(
    "plantation/+/sensor/+/event/reading",
    "plantation/+/event/status"
  );

  public MqttCallbackConfig(MqttClient mqttClient, MqttMessageService mqttMessageService) {
    this.mqttClient = mqttClient;
    this.mqttMessageService = mqttMessageService;
  }

  @Override
  public void disconnected(MqttDisconnectResponse disconnectResponse) {
    log.error("MQTT client disconnected: {}", disconnectResponse.getReasonString());
  }

  @Override
  public void mqttErrorOccurred(MqttException exception) {
    log.error("MQTT error occurred: {}", exception.getMessage());
  }

  @Override
  public void messageArrived(String topic, MqttMessage message) throws Exception {
    log.info("Message arrived for topic {}: {}", topic, message.toString());

    // Process the message
    this.mqttMessageService.processMessage(topic, message);
  }

  @Override
  public void deliveryComplete(IMqttToken token) {
    log.info("Delivery complete for token {}", token);
  }

  @Override
  public void connectComplete(boolean reconnect, String serverURI) {
    if (reconnect) {
      log.info("MQTT client reconnected to {}", serverURI);
    } else {
      log.info("MQTT client connected to {}", serverURI);
    }

    // Subscribe to topics after connection or reconnection
    this.subscribe(this.topics);
  }

  @Override
  public void authPacketArrived(int reasonCode, MqttProperties properties) {
    log.info("Auth packet arrived with reason code {}", reasonCode);
  }

  /**
   * Subscribe to the given topics
   * @param topics List of topics to subscribe to
   */
  public void subscribe(List<String> topics) {
    if (mqttClient == null || !this.mqttClient.isConnected()) {
      topics.forEach(topic -> log.error("MQTT client is not connected and cannot subscribe to topic {}", topic));
      return;
    }

    // Subscribe to each topic
    topics.forEach(topic -> {
      try {
        this.mqttClient.subscribe(topic, 1);
        log.info("Subscribed to topic {}", topic);
      } catch (MqttException e) {
        log.error("Error subscribing to topic {}", topic);
      }
    });
  }
}
