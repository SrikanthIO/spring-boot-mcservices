
package com.orderservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(OrderServiceApplication.class, args);
  }

  // private final CreateOrderProducer producer;

  //  @Bean
  //  public CommandLineRunner CommandLineRunnerBean() {
  //    return (args) -> {
  //      OrderLineItems item = OrderLineItems.builder().skuCode("1001").id(202l).quantity(20).build();
  //      List<OrderLineItems> list = new ArrayList<OrderLineItems>();
  //      list.add(item);
  //      Order a = Order.builder().orderLineItemsList(list).build();
  //      this.producer.sendCreateOrderEvent(a);
  //      // MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer("myConsumer");
  //      //listenerContainer.start();
  //    };
  //  }

  //  @Autowired
  //  OrderServiceApplication(CreateOrderProducer producer) {
  //    this.producer = producer;
  //  }
  //
  //  @Autowired
  //  private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

}