package pl.arturczopek.mycoach.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.arturczopek.mycoach.model.database.User;
import pl.arturczopek.mycoach.service.DictionaryService;

import java.util.Map;

/**
 * @Author Artur Czopek
 * @Date 17-02-2017
 */

@RestController
@RequestMapping("/dictionary")
public class DictionaryController {

    private DictionaryService dictionaryService;

    @Autowired
    public DictionaryController(DictionaryService dictionaryService) {
        this.dictionaryService = dictionaryService;
    }

    @GetMapping("/")
    public Map<String, String> getDictionary(User user) {
        return dictionaryService.getDictionary(user);
    }
}
