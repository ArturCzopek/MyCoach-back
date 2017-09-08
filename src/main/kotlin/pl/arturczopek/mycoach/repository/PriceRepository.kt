package pl.arturczopek.mycoach.repository

import org.springframework.data.repository.CrudRepository
import pl.arturczopek.mycoach.model.database.Price

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

interface PriceRepository : CrudRepository<Price, Long> {

    fun findByProductIdOrderByPriceDateAsc(productId: Long): List<Price>

    fun deleteByProductIdOrderByPriceDate(productId: Long)
}
