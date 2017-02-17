package pl.arturczopek.mycoach.database.repository;

import org.springframework.data.repository.CrudRepository;
import pl.arturczopek.mycoach.database.entity.UserSetting;

/**
 * @Author Artur Czopek
 * @Date 17-02-2017
 */
public interface UserSettingRepository extends CrudRepository<UserSetting, Long> {

}
