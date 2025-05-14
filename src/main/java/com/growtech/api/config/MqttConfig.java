package com.growtech.api.config;

import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.mqttv5.client.*;
import org.eclipse.paho.mqttv5.common.MqttException;
import org.eclipse.paho.mqttv5.common.MqttMessage;
import org.eclipse.paho.mqttv5.common.packet.MqttProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class MqttConfig {
  @Value("${app.mqtt.broker.url}")
  private String broker;
  @Value("${app.mqtt.broker.client_id}")
  private String clientId;
  @Value("${app.mqtt.broker.password}")
  private String password;

  private MqttClient mqttClient;

  /**
   * Create the MQTT client and connect to the broker
   * This method is called when the application is ready
   */
  @EventListener(ApplicationReadyEvent.class)
  public void createMqttConfig() {
    try {
      this.mqttClient = new MqttClient(broker, clientId);
      this.setCallback();
      this.connect();
    } catch (MqttException e) {
      log.error("Error creating MQTT client {}", e.getMessage());
    }
  }

  /**
   * Set the callback for the MQTT client
   */
  private void setCallback() {
    this.mqttClient.setCallback(new MqttCallbackConfig(this.mqttClient));
  }

  /**
   * Connect to the MQTT broker using connection options
   */
  private void connect() {
    MqttConnectionOptions connectionOptions = this.getConnectionOptions();
    try {
      this.mqttClient.connect(connectionOptions);
    } catch (MqttException e) {
      log.error("Error connecting to MQTT broker {}", e.getMessage());
    }
  }

  /**
   * Get the connection options for the MQTT client
   * @return MqttConnectionOptions
   */
  private MqttConnectionOptions getConnectionOptions() {
    return new MqttConnectionOptionsBuilder()
      .cleanStart(true)
      .automaticReconnect(true)
      .password(this.password.getBytes(StandardCharsets.UTF_8))
      .build();
  }
}
