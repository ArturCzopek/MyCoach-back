package pl.arturczopek.mycoach.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.arturczopek.mycoach.model.database.Cycle;
import pl.arturczopek.mycoach.model.database.Training;
import pl.arturczopek.mycoach.model.preview.CyclePreview;
import pl.arturczopek.mycoach.service.CycleService;
import pl.arturczopek.mycoach.service.TrainingService;

import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 10-10-2016
 */

@Slf4j
@RestController
@RequestMapping("/training")
public class TrainingController {

    private TrainingService trainingService;
    private CycleService cycleService;

    @Autowired
    public TrainingController(TrainingService trainingService, CycleService cycleService) {
        this.trainingService = trainingService;
        this.cycleService = cycleService;
    }

    @GetMapping("/cycle/active")
    public Cycle getActiveCycle() {
        return cycleService.getActiveCycle();
    }

    @GetMapping("/cycle/previews")
    public List<CyclePreview> getCyclePreviews() {
        return cycleService.getCyclePreviews();
    }
    @GetMapping("/cycle/{id}")
    public Cycle getCycleById(@PathVariable long id) {
        return cycleService.getCycleById(id);
    }


    @GetMapping("/{id}")
    public List<Training> getTrainingDatesForSet(@PathVariable long id) {
        return trainingService.getTrainingDatesForSet(id);
    }

//    @PostMapping("/add")
//    @ResponseStatus(value = HttpStatus.CREATED, reason = "Dodano trening")
//    public void addTraining(@RequestBody TrainingToAdd trainingToAdd) {
//        trainingService.addTraining(trainingToAdd);
//    }
}
