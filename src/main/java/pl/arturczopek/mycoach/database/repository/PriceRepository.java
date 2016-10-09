package pl.arturczopek.mycoach.database.repository;

import org.springframework.data.repository.CrudRepository;
import pl.arturczopek.mycoach.database.entity.Price;

/**
 * @Author arturczopek
 * @Date 10/9/16
 */
public interface PriceRepository extends CrudRepository<Price, Long> {
}
