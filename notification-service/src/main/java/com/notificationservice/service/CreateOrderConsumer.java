
package com.notificationservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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

  @Value("${spring.cloudio.object}")
  private String cldObject;

  private void postToWorkflow(Order order) {
    JsonObject bodyjson = new JsonObject();
    JsonObject orderStatus = new JsonObject();
    orderStatus.addProperty("ds", cldObject);
    JsonArray dataArray = new JsonArray();
    JsonObject dataObj = new JsonObject();
    dataObj.addProperty("_rs", "I");
    dataObj.addProperty("orderId", order.getId().toString());
    if (order.getOrderLineItemsList() != null && order.getOrderLineItemsList().size() > 0) {
      dataObj.addProperty("productCode", order.getOrderLineItemsList().get(0).getSkuCode());
      dataObj.addProperty("productQty", order.getOrderLineItemsList().get(0).getQuantity());
    }
    dataObj.addProperty("internalId", order.getInternalId());

    dataArray.add(dataObj);
    orderStatus.add("data", dataArray);
    bodyjson.add("OrderStatusAlias", orderStatus);
    HttpResponse<JsonNode> response = Unirest.post(cldWorkflowUrl)
        .header("X-Application", cldAppName)
        .header("X-Api-Key", cldApiKey)
        .header("Content-Type", "application/json")
        .header("Authorization", "Bearer " + cldApiSecret)
        .body(bodyjson.toString())
        .asJson();

    log.info("Response Status:{}", response.getStatus());
    log.info("Response:{}", response.getBody().toPrettyString());
  }

}
