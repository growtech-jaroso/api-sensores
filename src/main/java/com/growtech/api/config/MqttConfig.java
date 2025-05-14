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
      this.subscribe("plantation/+/sensor/+/event/reading");
      this.subscribe("plantation/+/sensor/+/event/status");
    } catch (MqttException e) {
      log.error("Error creating MQTT client {}", e.getMessage());
    }
  }

  private void setCallback() {
    this.mqttClient.setCallback(new MqttCallback() {
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
        String payload = new String(message.getPayload());
        log.info("Message arrived for topic {}: {}", topic, payload);

        // Process the message
        log.info("Processing message for topic {}", topic);
      }

      @Override
      public void deliveryComplete(IMqttToken token) {
        log.info("Delivery complete for token {}", token);
      }

      @Override
      public void connectComplete(boolean reconnect, String serverURI) {
        log.info("MQTT client connected to {}", serverURI);
      }

      @Override
      public void authPacketArrived(int reasonCode, MqttProperties properties) {
        log.info("Auth packet arrived with reason code {}", reasonCode);
      }
    });
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

  public void subscribe(String topic) {
    if (mqttClient == null || !this.mqttClient.isConnected()) {
      log.error("MQTT client is not connected and cannot subscribe to topic {}", topic);
      return;
    }

    try {
      this.mqttClient.subscribe(topic, 1);
    } catch (MqttException e) {
      log.error("Error subscribing to topic {}", topic);
    }

  }
}
