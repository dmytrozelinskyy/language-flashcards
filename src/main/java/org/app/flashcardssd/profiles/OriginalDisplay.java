package org.app.flashcardssd.profiles;

import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Primary
@Component
@Profile("original")
public class OriginalDisplay implements WordDisplayInterface {
   @Override
   public void displayWord(String word) {
      System.out.println(word);
   }
}
