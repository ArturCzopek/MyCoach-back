package pl.arturczopek.mycoach.repository

import org.springframework.data.repository.PagingAndSortingRepository
import pl.arturczopek.mycoach.model.database.Cycle
import pl.arturczopek.mycoach.model.database.Set

import java.sql.Date

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

interface CycleRepository : PagingAndSortingRepository<Cycle, Long> {

    override fun findAll(): List<Cycle>

    fun findAllByUserIdOrderByIsFinishedDescEndDateAsc(userId: Long): List<Cycle>

    fun countByUserIdAndIsFinishedFalse(userId: Long): Long

    fun findOneByUserIdAndIsFinishedFalse(userId: Long): Cycle

    fun findFirstByUserIdOrderByEndDateDesc(userId: Long): Cycle

    fun findFirstByUserIdAndEndDateBeforeOrderByEndDateDesc(userId: Long, startDate: Date): Cycle

    fun findFirstByUserIdAndStartDateAfterOrderByStartDate(userId: Long, startDate: Date): Cycle

    fun findOneBySetsContains(set: Set): Cycle
}
