package pl.arturczopek.mycoach.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.arturczopek.mycoach.dto.update.SetToUpdate;
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

    @PutMapping("/update")
    @ResponseStatus(value = HttpStatus.OK, reason = "Zaaktualizowano set")
    public void updateSet(@RequestBody SetToUpdate setToUpdate) {
        setService.updateSet(setToUpdate);
    }
}
