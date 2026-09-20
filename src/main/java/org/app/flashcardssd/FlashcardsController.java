package org.app.flashcardssd;

import org.app.flashcardssd.profiles.LowercaseDisplay;
import org.app.flashcardssd.profiles.OriginalDisplay;
import org.app.flashcardssd.profiles.UppercaseDisplay;
import org.app.flashcardssd.profiles.WordDisplayInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class FlashcardsController {
   // -- Necessary Delete Phrases -- //
   private final String deleteWordsConfirmationPhrase = "TPO03_repository_delete";

   // -- Patterns -- //
   private static final Pattern PATTERN_EN = Pattern.compile("^[A-Za-z]+$");
   private static final Pattern PATTERN_PL = Pattern.compile("^[A-Za-zĄĆĘŁŃÓŚŹŻąćęłńóśźż]+$");
   private static final Pattern PATTERN_DE = Pattern.compile("^[A-Za-zÄÖÜäöüß]+$");

   private final FileService fileService;
   private Scanner readLine;

   @Autowired
   public FlashcardsController(FileService fileService,
                               Scanner readLine)
   {
      this.fileService = fileService;
      this.readLine = readLine;
   }

   public void initializeDB() {
      fileService.initializeDB();
   }

   public void addWord() {
      System.out.println("Enter new word: ");
      String word = readLine.next();

      if(searchWord(word) != null) {
         System.out.println("There is already such word in the database");
         return;
      }

      Matcher matcher = PATTERN_EN.matcher(word.trim());
      if(matcher.matches()) {
         System.out.println("Enter Polish translation: ");
         String pl  = readLine.next();
         matcher = PATTERN_PL.matcher(pl.trim());
         if(matcher.matches()) {
            System.out.println("Enter German translation: ");
            String de  = readLine.next();
            matcher = PATTERN_DE.matcher(de.trim());
            if(matcher.matches()) {
               Entry inputEntry = new Entry(word.trim(), pl.trim(), de.trim());
               fileService.addNewEntry(inputEntry);
            } else System.out.println("German translation is incorrect!");
         } else System.err.println("Polish translation is incorrect!");
      } else System.err.println("Entered word is incorrect! Correct word: " +
                 "\n\t- English Language " +
                 "\n\t- Consists of only letters (not numbers or symbols)" +
                 "\n\t- Only 1 word (not a sentence)");
   }

   public Entry searchWord(String word) {
      return fileService.searchEntry(word);
   }

   public void updateEntry(String word) throws EntryNotFoundException {
      fileService.updateEntry(searchWord(word));
   }

   // -- Delete ONLY one word -- //
   public void deleteWord(String word) {
      fileService.removeWord(word);
      System.out.println("Word was removed from the database.");
   }

   // -- Delete all words (whole repository) -- //
   public void deleteAllWords() {
      System.out.println("You are attempting to delete all words. If that's what you wanted to do, you must enter 'TPO03_repository_delete': ");
      if(readLine.next().trim().equals(deleteWordsConfirmationPhrase))
         fileService.removeEntries();
   }


   // -- Display all words from repository -- //
   public void displayWords() {
      WordDisplayInterface enProfile, plProfile, deProfile;
      System.out.println("Do you want to manually choose ways of displaying words of different languages? (y/n): ");
      if(readLine.next().trim().equals("y")) {
         System.out.println("Choose profile for english word display: ");
         enProfile = chooseProfile(readLine);
         System.out.println("Choose profile for polish word display: ");
         plProfile = chooseProfile(readLine);
         System.out.println("Choose profile for german word display: ");
         deProfile = chooseProfile(readLine);
      } else enProfile = plProfile = deProfile = new OriginalDisplay();
      List<Entry> entries = fileService.getAllEntries();
      System.out.println("Do you want to sort words by some language? (y/n): ");
      if(readLine.next().trim().equals("y")) {
         System.out.println("Ascending - y, Descending - n: ");
         boolean asc = readLine.next().trim().equals("y");
         entries = fileService.sortByLanguage(chooseSortingLanguage(), asc);
      }
      for (Entry entry : entries) {
         enProfile.displayWord(entry.getENTranslation());
         plProfile.displayWord(entry.getPLTranslation());
         deProfile.displayWord(entry.getDETranslation());
      }
   }

   // -- Create a test with a randomly chosen word and randomly chosen language -- //
   public void createTest() {
      Entry testEntry = fileService.getRandomlyChosenEntry();
      String testWord = testEntry.getRandomWord();
      String correctEN = testEntry.getENTranslation();
      String correctPL = testEntry.getPLTranslation();
      String correctDE = testEntry.getDETranslation();

      System.out.println("Task -> Translate '" + testWord + "'");

      if(testWord.equals(correctEN)) {
         System.out.println("Translate to Polish: ");
         String userInputPL = readLine.next();
         System.out.println("Translate to German: ");
         String userInputDE = readLine.next();
         checkAnswer(userInputPL, userInputDE, correctPL, correctDE);
      } else if (testWord.equals(correctPL)) {
         System.out.println("Translate to English: ");
         String userInputEN = readLine.next();
         System.out.println("Translate to German: ");
         String userInputDE = readLine.next();
         checkAnswer(userInputEN, userInputDE, correctEN, correctDE);
      } else {
         System.out.println("Translate to Polish: ");
         String userInputPL = readLine.next();
         System.out.println("Translate to English: ");
         String userInputEN = readLine.next();
         checkAnswer(userInputPL, userInputEN, correctPL, correctEN);
      }
   }

   public void checkAnswer(String userInput1, String userInput2,
                           String correctTranslation1, String correctTranslation2) {
      if(userInput1.trim().equalsIgnoreCase(correctTranslation1.trim()) && userInput2.trim().equalsIgnoreCase(correctTranslation2.trim()))
         System.out.println("Correct!");
      else System.out.println("Incorrect! Correct translations: " + correctTranslation1 + ", " + correctTranslation2);
   }


   // -- Main Method run -- //
   public void run() throws EntryNotFoundException {
      while (true) {
         int choice = menu();
         if(choice == 0) System.exit(0);
         else {
            switch(choice) {
               case 1: addWord(); break;
               case 2: displayWords(); break;
               case 3: createTest(); break;
               case 4:
                  System.out.println("Enter word you want to find: ");
                  System.out.println(searchWord(readLine.next().trim()));
                  break;
               case 5:
                  System.out.println("Enter word you want to update: ");
                  updateEntry(readLine.next().trim());
                  break;
               case 6: {
                  System.out.println("Enter word you want to delete: ");
                  deleteWord(readLine.next().trim());
                  break;
               }
               case 7: deleteAllWords(); break;
               default: System.out.println("Invalid choice! Enter again.");
            }
         }
      }
   }

   // -- Choose Sorting Language -- //
   public String chooseSortingLanguage() {
      System.out.println(" -- Sort by ... -- ");
      System.out.println(" 1. English");
      System.out.println(" 2. Polish");
      System.out.println(" 3. German");
      System.out.println("Enter your choice: ");
      int choice = readLine.nextInt();
      return switch (choice) {
         case 1 -> "en";
         case 2 -> "pl";
         case 3 -> "de";
         default -> throw new IllegalStateException("Invalid value: " + choice);
      };
   }

   // -- Choose Profile -- //
   private WordDisplayInterface chooseProfile(Scanner tempScanner) {
      System.out.println(" -- Profiles -- ");
      System.out.println("1 - Original Profile");
      System.out.println("2 - Uppercase Profile");
      System.out.println("3 - Lowercase Profile");
      System.out.println("Enter number of profile you prefer: ");

      int choice = tempScanner.nextInt();
      String profile = "original";
      WordDisplayInterface profileDisplay = new OriginalDisplay();
      switch (choice) {
         case 1: {
            profile = "original";
            profileDisplay = new OriginalDisplay();
            break;
         }
         case 2: {
            profile = "uppercase";
            profileDisplay = new UppercaseDisplay();
            break;
         }
         case 3: {
            profile = "lowercase";
            profileDisplay = new LowercaseDisplay();
            break;
         }
      };

      System.setProperty("spring.profiles.active", profile);
      return profileDisplay;
   }

   // -- User Menu -- //
    public int menu() {
      System.out.println("\t-- Menu -- \t");
      System.out.println("0 - Exit");
      System.out.println("1 - Add New Word");
      System.out.println("2 - Display All Words");
      System.out.println("3 - Test Yourself");
      System.out.println("4 - Search Word");
      System.out.println("5 - Modify Word");
      System.out.println("6 - Remove Word");
      System.out.println("7 - Remove All Words");
      System.out.println("Enter your choice: ");
      return readLine.nextInt();
   }
}
