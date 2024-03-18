
package com.orderservice.service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.common.Uuid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.google.gson.JsonObject;
import com.orderservice.dto.InventoryResponse;
import com.orderservice.dto.OrderLineItemsDto;
import com.orderservice.dto.OrderRequest;
import com.orderservice.dto.OrderResponse;
import com.orderservice.model.Order;
import com.orderservice.model.OrderLineItems;
import com.orderservice.repository.OrderRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderService {

  private final OrderRepository orderRepository;

  private final WebClient.Builder webClientBuilder;

  private final CreateOrderProducer createOrderProducer;

  public String placeOrder(OrderRequest orderRequest) throws ExecutionException, InterruptedException {

    Order order = getOrderObject(orderRequest);

    //collecting all skuCodes from the order which user requested
    List<String> skuCodes = order.getOrderLineItemsList().stream()
        .map(OrderLineItems::getSkuCode).toList();

    //check in inventory if that order is available in stock or not
    InventoryResponse[] inventoryResponseArray = webClientBuilder.build().get()
        .uri("http://inventory-service/api/inventory",
            uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
        .retrieve()
        .bodyToMono(InventoryResponse[].class)
        .block();

    if (inventoryResponseArray == null || inventoryResponseArray.length != skuCodes.size()) {
      throw new IllegalArgumentException("Out of stock, please try again later...");
    } else {
      orderRepository.save(order);
      // create order event
      createOrderProducer.sendCreateOrderEvent(order);
      JsonObject obj = new JsonObject();
      obj.addProperty("status", "Success");
      obj.addProperty("internalId", order.getInternalId());

      return obj.toString();
    }
  }

  private Order getOrderObject(OrderRequest orderRequest) {
    List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsListDto()
        .stream()
        .map(this::mapToOrderLineItem)
        .toList();
    return Order.builder()
        .orderNumber(UUID.randomUUID().toString())
        .orderLineItemsList(orderLineItems)
        .internalId(Uuid.randomUuid().toString())
        .build();
  }

  private OrderLineItems mapToOrderLineItem(OrderLineItemsDto orderLineItemsDto) {
    return OrderLineItems.builder()
        .skuCode(orderLineItemsDto.getSkuCode())
        .price(orderLineItemsDto.getPrice())
        .quantity(orderLineItemsDto.getQuantity())
        .build();
  }

  public List<OrderResponse> fetchAllOrders() {
    List<Order> orders = orderRepository.findAll();
    return orders.stream().map(this::mapToDTO).toList();
  }

  private OrderResponse mapToDTO(Order order) {
    List<OrderLineItemsDto> orderLineItemsDtoList = order.getOrderLineItemsList().stream()
        .map(this::mapToOrderLineItemsDTO).toList();
    return OrderResponse.builder()
        .orderNumber(order.getOrderNumber())
        .orderLineItemsDtoList(orderLineItemsDtoList)
        .build();
  }

  private OrderLineItemsDto mapToOrderLineItemsDTO(OrderLineItems orderLineItem) {
    return OrderLineItemsDto.builder()
        .id(orderLineItem.getId())
        .skuCode(orderLineItem.getSkuCode())
        .price(orderLineItem.getPrice())
        .quantity(orderLineItem.getQuantity())
        .build();
  }

}
