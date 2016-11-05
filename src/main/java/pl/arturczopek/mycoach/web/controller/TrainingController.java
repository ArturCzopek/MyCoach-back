package pl.arturczopek.mycoach.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.arturczopek.mycoach.database.entity.TrainingDate;
import pl.arturczopek.mycoach.dto.add.TrainingToAdd;
import pl.arturczopek.mycoach.service.TrainingDateService;

import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 10-10-2016
 */

@Slf4j
@RestController
@RequestMapping("/training")
public class TrainingController {

    private TrainingDateService trainingDateService;

    @Autowired
    public TrainingController(TrainingDateService trainingDateService) {
        this.trainingDateService = trainingDateService;
    }

    @GetMapping("/{id}")
    public List<TrainingDate> getTrainingDatesForSet(@PathVariable long id) {
        return trainingDateService.getTrainingDatesForSet(id);
    }

    @PostMapping("/add")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Dodano trening")
    public void addTraining(@RequestBody TrainingToAdd trainingToAdd) {
        trainingDateService.addTraining(trainingToAdd);
    }
}
