package pl.arturczopek.mycoach.repository

import org.springframework.data.repository.CrudRepository
import pl.arturczopek.mycoach.model.database.DictionaryEntry
import pl.arturczopek.mycoach.model.database.Language

/**
 * @Author Artur Czopek
 * @Date 17-02-2017
 */

interface DictionaryRepository : CrudRepository<DictionaryEntry, Long> {

    fun findAllByLanguage(language: Language): List<DictionaryEntry>

    fun findOneByLanguageAndKey(language: Language, key: String): DictionaryEntry
}
