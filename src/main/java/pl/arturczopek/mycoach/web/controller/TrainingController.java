package pl.arturczopek.mycoach.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.arturczopek.mycoach.model.add.NewCycle;
import pl.arturczopek.mycoach.model.add.NewExercise;
import pl.arturczopek.mycoach.model.add.NewTraining;
import pl.arturczopek.mycoach.model.database.Cycle;
import pl.arturczopek.mycoach.model.database.Exercise;
import pl.arturczopek.mycoach.model.database.Training;
import pl.arturczopek.mycoach.model.preview.CyclePreview;
import pl.arturczopek.mycoach.model.request.dto.ExerciseForTrainingPreview;
import pl.arturczopek.mycoach.model.request.dto.ExercisesWithTrainingToEdit;
import pl.arturczopek.mycoach.service.CycleService;
import pl.arturczopek.mycoach.service.ExerciseService;
import pl.arturczopek.mycoach.service.TrainingService;

import javax.management.InvalidAttributeValueException;
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
    private ExerciseService exerciseService;

    @Autowired
    public TrainingController(TrainingService trainingService, CycleService cycleService, ExerciseService exerciseService) {
        this.trainingService = trainingService;
        this.cycleService = cycleService;
        this.exerciseService = exerciseService;
    }

    @GetMapping("/cycle/active")
    public Cycle getActiveCycle() {
        return cycleService.getActiveCycle();
    }

    @GetMapping("/cycle/previews")
    public List<CyclePreview> getCyclePreviews() {
        return cycleService.getCyclePreviews();
    }

    @GetMapping("/cycle/finished")
    public boolean hasUserEveryCycleFinished() {
        return cycleService.hashUserEveryCycleFinished();
    }

    @GetMapping("/cycle/{id}")
    public Cycle getCycleById(@PathVariable long id) {
        return cycleService.getCycleById(id);
    }

    @GetMapping("/exercise/{trainingId}")
    public List<ExerciseForTrainingPreview> getExercisesWithSessionsForTraining(@PathVariable long trainingId) {
        return trainingService.findExercisesByTrainingId(trainingId);
    }

    @PostMapping("cycle/add")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Added cycle")
    public void addCycle(@RequestBody NewCycle cycle) throws InvalidAttributeValueException {
        cycleService.addCycle(cycle);
    }

    @PostMapping("exercise/add")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Added exercises")
    public void addExercise(@RequestBody List<NewExercise> exercises) {
        exerciseService.addExercises(exercises);
    }

    @PostMapping("/add")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Added training")
    public void addTraining(@RequestBody NewTraining training) {
        trainingService.addTraining(training);
    }

    @DeleteMapping("cycle/delete")
    @ResponseStatus(value = HttpStatus.OK, reason = "Removed cycle")
    public void deleteCycle(@RequestBody Cycle cycle) {
        cycleService.deleteCycle(cycle);
    }

    @DeleteMapping("exercise/delete")
    @ResponseStatus(value = HttpStatus.OK, reason = "Removed exercise")
    public void deleteExercise(@RequestBody Exercise exercise) {
        exerciseService.deleteExercise(exercise);
    }

    @DeleteMapping("/delete")
    @ResponseStatus(value = HttpStatus.OK, reason = "Removed training")
    public void deleteTraining(@RequestBody Training training) {
        trainingService.deleteTraining(training);
    }

    @PutMapping("cycle/update")
    @ResponseStatus(value = HttpStatus.OK, reason = "Updated cycle")
    public void updateCycle(@RequestBody Cycle cycle) {
        cycleService.updateCycle(cycle);
    }

    @PutMapping("exercise/update")
    @ResponseStatus(value = HttpStatus.OK, reason = "Updated exercise")
    public void updateExercise(@RequestBody Exercise exercise) {
        exerciseService.updateExercise(exercise);
    }

    @PutMapping("/update")
    @ResponseStatus(value = HttpStatus.OK, reason = "Updated training")
    public void updateTraining(@RequestBody ExercisesWithTrainingToEdit exercisesWithTrainingToEdit) {
        trainingService.updateTraining(exercisesWithTrainingToEdit.getTraining(), exercisesWithTrainingToEdit.getExercises());
    }
}
