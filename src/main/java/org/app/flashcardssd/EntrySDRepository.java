package org.app.flashcardssd;

import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface EntrySDRepository extends CrudRepository<Entry, Long>
{
   // -- Additional manually created method to return Entry by word -- //
   Optional<Entry> findByENTranslation(String word);
}
