package org.app.flashcardssd;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.*;

@Service
public class FileService {
   private final EntrySDRepository entryRepository;

   public FileService(EntrySDRepository entryRepository) {
      this.entryRepository = entryRepository;
   }

   // -- Initialize DB -- //
   // If database is empty, we initialize it with 2 default entries.
   public void initializeDB() {
      if(!new File("..\\flashcards\\workspace\\dictionary.mv.db").exists() || entryRepository.count() == 0) {
         Entry carEntry = new Entry("car", "samochod", "Auto");
         Entry catEntry = new Entry("cat", "kot", "Kitzen");
         entryRepository.save(carEntry);
         entryRepository.save(catEntry);
         System.out.println("Database was created and some initial date was inserted.");
      } else System.out.println("Database already exists and is not empty.");
   }

   public void addNewEntry(Entry entry) {
      if(entry == null) {
         System.out.println("Entry is null.");
         return;
      }

      if(searchEntry(entry.getENTranslation()) != null) {
         System.out.println("There is such word in database.");
         return;
      }

      entryRepository.save(entry);
      System.out.println("New entry was added to the database.");
   }

   public void updateEntry(Entry entry) throws EntryNotFoundException {
      Scanner tempScanner = new Scanner(System.in);
      if(entry.getId() == null)
         throw new EntryNotFoundException("Entry has no id.");
      Entry actuallEntry = entryRepository.findById(entry.getId())
              .orElseThrow(() -> new EntryNotFoundException("Entry was not found."));

      actuallEntry.setId(entry.getId());
      System.out.println("Enter changes to the word: ");
      String newWord = tempScanner.next().trim();
      if(searchEntry(newWord) != null){
         System.out.println("There is already such word in database. Nothing to update.");
         return;
      }
      actuallEntry.setENTranslation(newWord);
      System.out.println("Do you want to modify PL translation of this word? (y/n): ");
      if(tempScanner.next().trim().equals("y")) {
         System.out.println("Enter PL translation of this word: ");
         actuallEntry.setPLTranslation(tempScanner.next().trim());
         System.out.println("Do you want to modify DE translation of this word? (y/n): ");
         if(tempScanner.next().trim().equals("y")) {
            System.out.println("Enter DE translation of this word: ");
            actuallEntry.setDETranslation(tempScanner.next().trim());
         } else actuallEntry.setDETranslation(entry.getDETranslation());
      } else actuallEntry.setPLTranslation(entry.getPLTranslation());

      entryRepository.save(actuallEntry);
      System.out.println("Entry was updated.");
   }

   public Entry searchEntry(String word) {
      if(word == null) return null;

      String normalizedWord = word.trim().toLowerCase();

      List<Entry> entries = new ArrayList<>();
      entryRepository.findAll().forEach(entries::add);

      Optional<Entry> entryOptional = entries.stream().filter(e ->
              e.getENTranslation() != null && e.getENTranslation().equals(normalizedWord)
           || e.getPLTranslation() != null && e.getPLTranslation().equals(normalizedWord)
           || e.getDETranslation() != null && e.getDETranslation().equals(normalizedWord)
      ).findFirst();

      if(entryOptional.isPresent()) {
         return entryOptional.get();
      } else {
         System.out.println("There is no such word in database.");
         return null;
      }
   }

   // -- Remove Entry [By Entry] -- //
   public void removeEntry(Entry entry) {
      if(entry.getId() == null) {
         System.out.println("Entry id is null.");
         return;
      }
      if(!entryRepository.existsById(entry.getId())) {
         System.out.println("Entry does not exist in database.");
         return;
      }
      entryRepository.delete(entry);
      System.out.println("Entry has been removed from the database.");
   }
   // -- Remove Entry [By Id] -- //
   public void removeEntry(Long id) {
      entryRepository.deleteById(id);
      System.out.println("Entry has been removed from the database.");
   }
   // -- Remove Entry [By word] -- //
   public void removeWord(String word) {
      Entry entry = searchEntry(word);
      if(entry != null) {
         entryRepository.delete(entry);
         System.out.println("Entry has been removed from the database.");
      } else System.out.println("There is no such word in database.");
   }
   // -- Remove all Entries -- //
   public void removeEntries() {
      entryRepository.deleteAll();
      System.out.println("All entries were removed successfully.");
   }

   public List<Entry> sortById() {
      List<Entry> entries = new ArrayList<>();
      entryRepository.findAll().forEach(entries::add);

      entries.sort(Comparator.comparing(Entry::getId));
      return entries;
   }

   public List<Entry> sortByLanguage(String language, boolean ascending) {
      List<Entry> entries = new ArrayList<>();
      entryRepository.findAll().forEach(entries::add);

      Comparator<Entry> comparator;
      switch(language.toLowerCase()) {
         case "en": comparator = Comparator.comparing(Entry::getENTranslation); break;
         case "pl": comparator = Comparator.comparing(Entry::getPLTranslation); break;
         case "de": comparator = Comparator.comparing(Entry::getDETranslation); break;
         default: comparator = Comparator.comparing(Entry::getENTranslation);;
      }
      if(!ascending) comparator = comparator.reversed();
      entries.sort(comparator);

      return entries;
   }

   // -- Getters -- //
   public Entry getRandomlyChosenEntry(){
      List<Entry> entries = (List<Entry>) entryRepository.findAll();
      if(entries.isEmpty()) return null;
      int index = new Random().nextInt(entries.size());
      return entries.get(index);
   }

   public List<Entry> getAllEntries() {
      return (List<Entry>) entryRepository.findAll();
   }
}
