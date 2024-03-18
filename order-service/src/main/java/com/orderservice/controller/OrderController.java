
package com.orderservice.controller;

import java.util.List;
import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.orderservice.dto.OrderRequest;
import com.orderservice.dto.OrderResponse;
import com.orderservice.service.OrderService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  @PostMapping("/create")
  @ResponseStatus(HttpStatus.CREATED)
  @CircuitBreaker(name = "inventory", fallbackMethod = "fallbackMethod")
  public String placeOrder(@RequestBody OrderRequest orderRequest) throws ExecutionException, InterruptedException {
    return orderService.placeOrder(orderRequest);
  }

  public String fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException) {
    return "Oops!!, something went wrong";
  }

  @GetMapping("/all")
  public List<OrderResponse> fetchAllOrders() {
    return orderService.fetchAllOrders();
  }

}
