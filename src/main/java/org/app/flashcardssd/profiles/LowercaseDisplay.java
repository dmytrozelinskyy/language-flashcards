package org.app.flashcardssd.profiles;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("lowercase")
public class LowercaseDisplay implements WordDisplayInterface {
   @Override
   public void displayWord(String word) {
      System.out.println(word.toLowerCase());
   }
}
