
package com.orderservice.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {

  private Long id;
  private String orderNumber;
  private String internalId;
  private List<OrderLineItems> orderLineItemsList;
}
