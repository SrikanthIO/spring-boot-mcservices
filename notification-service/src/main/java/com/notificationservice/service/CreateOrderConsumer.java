
package com.notificationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.orderservice.model.Order;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;

@Service("orderConsumerNotificationService")
public class CreateOrderConsumer {

  private static final Logger log = LoggerFactory.getLogger(CreateOrderConsumer.class);

  @KafkaListener(topics = "${spring.kafka.topic.create-order}", containerFactory = "containerFactoryNotificationService")
  public void createOrderListener(@Payload Order order, Acknowledgment ack) {
    log.info("Notification service received order {} ", order);
    ack.acknowledge();
    postToWorkflow(order);
  }

  @Value("${spring.cloudio.workflow_url}")
  private String cldWorkflowUrl;

  @Value("${spring.cloudio.app}")
  private String cldAppName;

  @Value("${spring.cloudio.api_key}")
  private String cldApiKey;

  @Value("${spring.cloudio.api_secret}")
  private String cldApiSecret;

  private void postToWorkflow(Order order) {
    HttpResponse<JsonNode> response = Unirest.post(cldWorkflowUrl)
        .header("accept", "application/json")
        .queryString("apiKey", "123")
        .field("parameter", "value")
        .field("firstname", "Gary")
        .asJson();
  }

}
