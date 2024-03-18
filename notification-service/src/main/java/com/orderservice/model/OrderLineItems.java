
package com.orderservice.model;

//import javax.persistence.*;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderLineItems {

  private Long id;
  private String skuCode;
  private BigDecimal price;
  private Integer quantity;
}
