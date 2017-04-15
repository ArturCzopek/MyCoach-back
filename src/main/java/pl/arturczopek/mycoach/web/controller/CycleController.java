package pl.arturczopek.mycoach.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.arturczopek.mycoach.model.add.NewCycle;
import pl.arturczopek.mycoach.service.CycleService;

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

//TODO remove controller
@Slf4j
@RestController
@RequestMapping("/cycle")
public class CycleController {

    private CycleService cycleService;

    @Autowired
    public CycleController(CycleService cycleService) {
        this.cycleService = cycleService;
    }

    @PostMapping("/add")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Dodano cykl")
    public void addCycle(@RequestBody NewCycle newCycle) {
        cycleService.addCycle(newCycle);
    }

    @PostMapping("/end/{id}")
    @ResponseStatus(value = HttpStatus.OK, reason = "Zakończono cykl")
    public void endCycle(@PathVariable long id) {
        cycleService.endCycle(id);
    }

//    @PutMapping("/update")
//    @ResponseStatus(value = HttpStatus.OK, reason = "Zaaktualizowano cykl")
//    public void updateCycle(@RequestBody CycleToUpdate cycleToUpdate) {
//        cycleService.updateCycle(cycleToUpdate);
//    }
}