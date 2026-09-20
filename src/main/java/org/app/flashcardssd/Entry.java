package org.app.flashcardssd;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.util.Random;

@Entity
public class Entry {

   @Id
   @GeneratedValue(strategy = GenerationType.AUTO)
   private Long Id;
   private String ENTranslation; // maybe word?
   private String PLTranslation;
   private String DETranslation;

   public Entry() { }

   public Entry(String ENTranslation, String PLTranslation, String DETranslation) {
      this.ENTranslation = ENTranslation;
      this.PLTranslation = PLTranslation;
      this.DETranslation = DETranslation;
   }

   // -- Setters and Getters -- //
   public void setId(Long id){
      this.Id = id;
   }
   public void setENTranslation(String ENTranslation) {
      this.ENTranslation = ENTranslation;
   }
   public void setPLTranslation(String PLTranslation) {
      this.PLTranslation = PLTranslation;
   }
   public void setDETranslation(String DETranslation) {
      this.DETranslation = DETranslation;
   }

   public Long getId() { return Id; }
   public String getENTranslation() {return ENTranslation;}
   public String getPLTranslation() {return PLTranslation;}
   public String getDETranslation() {return DETranslation;}

   // -- Get random word -- //
   public String getRandomWord() {
      Random random = new Random();
      return switch (random.nextInt(3)) {
         case 0 -> ENTranslation;
         case 1 -> PLTranslation;
         case 2 -> DETranslation;
         default -> ENTranslation;
      };
   }

   @Override
   public String toString() {
      return "Word: " + ENTranslation + "\nPolish Translation: "
                      + PLTranslation + "\nGerman Translation: "
                      + DETranslation;
   }
}
