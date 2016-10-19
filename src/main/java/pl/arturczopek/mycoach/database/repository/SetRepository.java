package pl.arturczopek.mycoach.database.repository;

import org.springframework.data.repository.CrudRepository;
import pl.arturczopek.mycoach.database.entity.Set;

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

public interface SetRepository extends CrudRepository<Set, Long> {
}
