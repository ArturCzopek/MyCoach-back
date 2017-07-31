package pl.arturczopek.mycoach.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.arturczopek.mycoach.model.database.DictionaryEntry;
import pl.arturczopek.mycoach.model.database.User;
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

    @Cacheable(value = "dictionary", key = "#user.userId")
    public Map<String, String> getDictionary(User user) {
        UserSetting userSetting = userSettingRepository.findOne(user != null && !user.equals(User.Companion.getEmptyUser()) ? user.getUserId() : 1l);

        List<DictionaryEntry> dictionaryEntries = dictionaryRepository.findAllByLanguage(userSetting.getLanguage());

        Map<String, String> dictionary = new HashMap<>();

        for (DictionaryEntry entry: dictionaryEntries) {
            dictionary.put(entry.getKey(), entry.getValue());
        }

        log.debug("Created dictionary for {} language, size: {}", user != null ? user.getUserId() : 1l, dictionary.size());

        return dictionary;
    }

    @Cacheable(value = "dictionaryKey", key = "#key + ' ' + #userId")
    public DictionaryEntry translate(String key, long userId) {
        UserSetting userSetting = userSettingRepository.findOne(userId);
        return dictionaryRepository.findOneByLanguageAndAndKey(userSetting.getLanguage(), key);
    }
}
