package org.app.flashcardssd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class FlashcardsSdApplication {

   public static void main(String[] args) throws EntryNotFoundException {
      ConfigurableApplicationContext context = SpringApplication.run(FlashcardsSdApplication.class, args);
      FlashcardsController controller = context.getBean(FlashcardsController.class);
      controller.initializeDB();
      controller.run();
   }
}
