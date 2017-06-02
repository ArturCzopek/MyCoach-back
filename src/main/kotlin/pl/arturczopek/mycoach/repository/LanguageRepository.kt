package pl.arturczopek.mycoach.repository

import org.springframework.data.repository.PagingAndSortingRepository
import pl.arturczopek.mycoach.model.database.Language


/**
 * @Author Artur Czopek
 * @Date 19-05-2017
 */
interface LanguageRepository: PagingAndSortingRepository<Language, Long>