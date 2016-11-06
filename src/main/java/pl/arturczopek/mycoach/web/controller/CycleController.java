package pl.arturczopek.mycoach.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.arturczopek.mycoach.database.entity.Cycle;
import pl.arturczopek.mycoach.dto.preview.CyclePreview;
import pl.arturczopek.mycoach.dto.add.CycleToAdd;
import pl.arturczopek.mycoach.dto.update.CycleToUpdate;
import pl.arturczopek.mycoach.service.CycleService;

import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

@Slf4j
@RestController
@RequestMapping("/cycle")
public class CycleController {

    private CycleService cycleService;

    @Autowired
    public CycleController(CycleService cycleService) {
        this.cycleService = cycleService;
    }

    @GetMapping("/previews")
    public List<CyclePreview> getPreviews() {
        return cycleService.getCyclePreviews();
    }

    @GetMapping("/{id}")
    public Cycle getCycleDetails(@PathVariable long id) {
        return cycleService.getCycleById(id);
    }

    @PostMapping("/add")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Dodano cykl")
    public void addCycle(@RequestBody CycleToAdd cycleToAdd) {
        cycleService.addCycle(cycleToAdd);
    }

    @PostMapping("/end/{id}")
    @ResponseStatus(value = HttpStatus.OK, reason = "Zakończono cykl")
    public void endCycle(@PathVariable long id) {
        cycleService.endCycle(id);
    }

    @PutMapping("/update")
    @ResponseStatus(value = HttpStatus.OK, reason = "Zaaktualizowano cykl")
    public void updateCycle(@RequestBody CycleToUpdate cycleToUpdate) {
        cycleService.updateCycle(cycleToUpdate);
    }
}