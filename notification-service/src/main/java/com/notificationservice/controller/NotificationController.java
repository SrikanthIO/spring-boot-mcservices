
package com.notificationservice.controller;

import java.util.concurrent.ExecutionException;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.orderservice.dto.OrderRequest;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

  @PostMapping("/")
  @ResponseStatus(HttpStatus.CREATED)
  public String notify(@RequestBody OrderRequest orderRequest)
      throws ExecutionException, InterruptedException {
    return "Ok";
  }

}
