package pl.arturczopek.mycoach.database.repository;

import org.springframework.data.repository.CrudRepository;
import pl.arturczopek.mycoach.database.entity.DictionaryEntry;
import pl.arturczopek.mycoach.database.entity.Language;

import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 17-02-2017
 */
public interface DictionaryRepository extends CrudRepository<DictionaryEntry, Long> {

    List<DictionaryEntry> findAllByLanguage(Language language);
}
