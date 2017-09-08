package pl.arturczopek.mycoach.repository

import org.springframework.data.repository.PagingAndSortingRepository
import pl.arturczopek.mycoach.model.database.Report

import java.sql.Date

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

interface ReportRepository : PagingAndSortingRepository<Report, Long> {

    override fun findAll(): List<Report>

    fun findAllByUserId(userId: Long): List<Report>

    fun findAllByUserIdOrderByEndDate(userId: Long): List<Report>

    fun findFirstByEndDateBeforeAndReportIdNotOrderByEndDateDesc(date: Date, reportId: Long): Report

    fun findFirstByStartDateAfterAndReportIdNotOrderByStartDate(date: Date, reportId: Long): Report
}
