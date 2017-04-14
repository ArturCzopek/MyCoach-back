package pl.arturczopek.mycoach.repository;

import org.springframework.data.repository.CrudRepository;
import pl.arturczopek.mycoach.model.database.Price;

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

public interface PriceRepository extends CrudRepository<Price, Long> {
}
