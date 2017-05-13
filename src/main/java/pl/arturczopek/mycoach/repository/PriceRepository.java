package pl.arturczopek.mycoach.repository;

import org.springframework.data.repository.CrudRepository;
import pl.arturczopek.mycoach.model.database.Price;

import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

public interface PriceRepository extends CrudRepository<Price, Long> {

    List<Price> findByProductIdOrderByPriceDateAsc(long productId);

    void deleteByProductIdOrderByPriceDate(long productId);
}
