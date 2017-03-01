package pl.arturczopek.mycoach.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.arturczopek.mycoach.database.entity.Set;
import pl.arturczopek.mycoach.database.repository.SetRepository;
import pl.arturczopek.mycoach.dto.update.SetToUpdate;

/**
 * @Author Artur Czopek
 * @Date 16-10-2016
 */

@Service
public class SetService {

    private SetRepository setRepository;

    @Autowired
    public SetService(SetRepository setRepository) {
        this.setRepository = setRepository;
    }

    public void updateSet(SetToUpdate setToUpdate) {
        Set set = setRepository.findOne(setToUpdate.getSetId());

        if (!StringUtils.isEmpty(setToUpdate.getSetName())) {
            set.setSetName(setToUpdate.getSetName());
            setRepository.save(set);
        }
    }
}