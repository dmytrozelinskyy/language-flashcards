package org.app.flashcardssd;

public class EntryNotFoundException extends Exception {

   public EntryNotFoundException(String msg) {
      super("EntryNotFoundException: " + msg);
   }
}
