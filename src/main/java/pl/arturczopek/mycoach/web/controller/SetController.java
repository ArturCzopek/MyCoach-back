package pl.arturczopek.mycoach.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.arturczopek.mycoach.service.SetService;

/**
 * @Author Artur Czopek
 * @Date 16-10-2016
 */

@RestController
@RequestMapping("/set")
public class SetController {

    private SetService setService;

    @Autowired
    public SetController(SetService setService) {
        this.setService = setService;
    }

//    @PutMapping("/update")
//    @ResponseStatus(value = HttpStatus.OK, reason = "Zaaktualizowano set")
//    public void updateSet(@RequestBody SetToUpdate setToUpdate) {
//        setService.updateSet(setToUpdate);
//    }
}
