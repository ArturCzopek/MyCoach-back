package pl.arturczopek.mycoach.repository

import org.springframework.data.repository.PagingAndSortingRepository
import pl.arturczopek.mycoach.model.database.Weight

import java.sql.Date

/**
 * @Author Artur Czopek
 * @Date 09-10-2016f
 */

interface WeightRepository : PagingAndSortingRepository<Weight, Long> {

    override fun findAll(): List<Weight>

    fun findAllByUserIdOrderByMeasurementDateDesc(userId: Long): List<Weight>

    fun findByUserIdAndMeasurementDateBetweenOrderByMeasurementDate(userId: Long, after: Date, before: Date): List<Weight>

    fun findOneByMeasurementDateAndWeightIdNot(measurementDate: Date, weightId: Long): Weight

    fun findOneByMeasurementDate(measurementDate: Date): Weight
}
