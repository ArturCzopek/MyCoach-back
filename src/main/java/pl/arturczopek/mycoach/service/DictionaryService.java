package pl.arturczopek.mycoach.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.arturczopek.mycoach.model.database.DictionaryEntry;
import pl.arturczopek.mycoach.model.database.UserSetting;
import pl.arturczopek.mycoach.repository.DictionaryRepository;
import pl.arturczopek.mycoach.repository.UserSettingRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author Artur Czopek
 * @Date 17-02-2017
 */

@Slf4j
@Service
public class DictionaryService {

    private UserSettingRepository userSettingRepository;
    private DictionaryRepository dictionaryRepository;

    @Autowired
    public DictionaryService(UserSettingRepository userSettingRepository, DictionaryRepository dictionaryRepository) {
        this.userSettingRepository = userSettingRepository;
        this.dictionaryRepository = dictionaryRepository;
    }

    public Map<String, String> getDictionary(long userId) {
        UserSetting userSetting = userSettingRepository.findOne(userId);

        List<DictionaryEntry> dictionaryEntries = dictionaryRepository.findAllByLanguage(userSetting.getLanguage());

        Map<String, String> dictionary = new HashMap<>();

        for (DictionaryEntry entry: dictionaryEntries) {
            dictionary.put(entry.getKey(), entry.getValue());
        }

        log.debug("Created dictionary for {} language, size: {}", userId, dictionary.size());

        return dictionary;
    }

    public DictionaryEntry translate(String key) {
        UserSetting userSetting = userSettingRepository.findOne(1l);
        return dictionaryRepository.findOneByLanguageAndAndKey(userSetting.getLanguage(), key);
    }
}
