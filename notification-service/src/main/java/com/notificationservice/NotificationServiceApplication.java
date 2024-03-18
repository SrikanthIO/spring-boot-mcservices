
package com.notificationservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class NotificationServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(NotificationServiceApplication.class, args);
  }

  // private final CreateOrderProducer producer;

  //  @Bean
  //  public CommandLineRunner CommandLineRunnerBean() {
  //    return (args) -> {
  //      MessageListenerContainer listenerContainer = kafkaListenerEndpointRegistry.getListenerContainer("notification");
  //      listenerContainer.start();
  //    };
  //  }
  //
  //  //
  //  @Autowired
  //  private KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;

}