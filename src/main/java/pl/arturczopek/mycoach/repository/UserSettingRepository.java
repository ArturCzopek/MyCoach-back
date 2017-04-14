package pl.arturczopek.mycoach.repository;

import org.springframework.data.repository.CrudRepository;
import pl.arturczopek.mycoach.model.database.UserSetting;

/**
 * @Author Artur Czopek
 * @Date 17-02-2017
 */
public interface UserSettingRepository extends CrudRepository<UserSetting, Long> {

}
