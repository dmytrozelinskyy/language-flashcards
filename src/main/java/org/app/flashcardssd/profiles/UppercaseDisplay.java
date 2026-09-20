package org.app.flashcardssd.profiles;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("uppercase")
public class UppercaseDisplay implements WordDisplayInterface {
   @Override
   public void displayWord(String word) {
      System.out.println(word.toUpperCase());
   }
}
