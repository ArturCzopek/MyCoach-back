package pl.arturczopek.mycoach.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.arturczopek.mycoach.exception.NotFoundUserException;
import pl.arturczopek.mycoach.model.database.User;
import pl.arturczopek.mycoach.service.DictionaryService;
import pl.arturczopek.mycoach.service.UserStorage;

import java.util.Map;

/**
 * @Author Artur Czopek
 * @Date 17-02-2017
 */

@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    private DictionaryService dictionaryService;
    private UserStorage userStorage;

    @Autowired
    public DictionaryController(DictionaryService dictionaryService, UserStorage userStorage) {
        this.dictionaryService = dictionaryService;
        this.userStorage = userStorage;
    }

    @GetMapping("/")
    public Map<String, String> getDictionary(@RequestHeader(value = "oauth-token", required = false) String token) {

        User user;

        try {
            user = userStorage.getUserByToken(token);
            return dictionaryService.getDictionary(user);
        } catch (NotFoundUserException ex) {
            return dictionaryService.getDictionary(User.Companion.getEmptyUser());
        }
    }
}
