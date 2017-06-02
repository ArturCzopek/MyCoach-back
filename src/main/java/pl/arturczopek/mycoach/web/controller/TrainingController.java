package pl.arturczopek.mycoach.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.arturczopek.mycoach.exception.DuplicatedNameException;
import pl.arturczopek.mycoach.exception.InvalidDateException;
import pl.arturczopek.mycoach.exception.InvalidPropsException;
import pl.arturczopek.mycoach.exception.WrongPermissionException;
import pl.arturczopek.mycoach.model.add.NewCycle;
import pl.arturczopek.mycoach.model.add.NewExercise;
import pl.arturczopek.mycoach.model.add.NewTraining;
import pl.arturczopek.mycoach.model.database.Cycle;
import pl.arturczopek.mycoach.model.database.Exercise;
import pl.arturczopek.mycoach.model.database.Training;
import pl.arturczopek.mycoach.model.database.User;
import pl.arturczopek.mycoach.model.preview.CyclePreview;
import pl.arturczopek.mycoach.model.request.dto.ExerciseForTrainingPreview;
import pl.arturczopek.mycoach.model.request.dto.ExercisesWithTrainingToEdit;
import pl.arturczopek.mycoach.service.CycleService;
import pl.arturczopek.mycoach.service.ExerciseService;
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
    private ExerciseService exerciseService;

    @Autowired
    public TrainingController(TrainingService trainingService, CycleService cycleService, ExerciseService exerciseService) {
        this.trainingService = trainingService;
        this.cycleService = cycleService;
        this.exerciseService = exerciseService;
    }

    @GetMapping("/cycle/active")
    public Cycle getActiveCycle(User user) {
        return cycleService.getActiveCycle(user.getUserId());
    }

    @GetMapping("/cycle/previews")
    public List<CyclePreview> getCyclePreviews(User user) {
        return cycleService.getCyclePreviews(user.getUserId());
    }

    @GetMapping("/cycle/finished")
    public boolean hasUserEveryCycleFinished(User user) {
        return cycleService.hashUserEveryCycleFinished(user.getUserId());
    }

    @GetMapping("/cycle/{cycleId}")
    public Cycle getCycleById(@PathVariable long cycleId, User user) throws WrongPermissionException {
        return cycleService.getCycleById(cycleId, user.getUserId());
    }

    @GetMapping("/exercise/{trainingId}")
    public List<ExerciseForTrainingPreview> getExercisesWithSessionsForTraining(@PathVariable long trainingId, User user) throws WrongPermissionException {
        return trainingService.findExercisesByTrainingId(trainingId, user.getUserId());
    }

    @PostMapping("cycle/add")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Added cycle")
    public void addCycle(@RequestBody NewCycle cycle, User user) throws InvalidPropsException {
        cycleService.addCycle(cycle, user.getUserId());
    }

    @PostMapping("exercise/add")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Added exercises")
    public void addExercise(@RequestBody List<NewExercise> exercises, User user) throws DuplicatedNameException, WrongPermissionException {
        exerciseService.addExercises(exercises, user.getUserId());
    }

    @PostMapping("/add")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Added training")
    public void addTraining(@RequestBody NewTraining training, User user) throws InvalidDateException, WrongPermissionException {
        trainingService.addTraining(training, user.getUserId());
    }

    @DeleteMapping("cycle/delete")
    @ResponseStatus(value = HttpStatus.OK, reason = "Removed cycle")
    public void deleteCycle(@RequestBody Cycle cycle, User user) throws WrongPermissionException {
        cycleService.deleteCycle(cycle, user.getUserId());
    }

    @DeleteMapping("exercise/delete")
    @ResponseStatus(value = HttpStatus.OK, reason = "Removed exercise")
    public void deleteExercise(@RequestBody Exercise exercise, User user) throws WrongPermissionException {
        exerciseService.deleteExercise(exercise, user.getUserId());
    }

    @DeleteMapping("/delete")
    @ResponseStatus(value = HttpStatus.OK, reason = "Removed training")
    public void deleteTraining(@RequestBody Training training, User user) throws WrongPermissionException {
        trainingService.deleteTraining(training, user.getUserId());
    }

    @PutMapping("cycle/update")
    @ResponseStatus(value = HttpStatus.OK, reason = "Updated cycle")
    public void updateCycle(@RequestBody Cycle cycle, User user) throws InvalidPropsException, WrongPermissionException {
        cycleService.updateCycle(cycle, user.getUserId());
    }

    @PutMapping("exercise/update")
    @ResponseStatus(value = HttpStatus.OK, reason = "Updated exercise")
    public void updateExercise(@RequestBody Exercise exercise, User user) throws DuplicatedNameException, WrongPermissionException {
        exerciseService.updateExercise(exercise, user.getUserId());
    }

    @PutMapping("/update")
    @ResponseStatus(value = HttpStatus.OK, reason = "Updated training")
    public void updateTraining(@RequestBody ExercisesWithTrainingToEdit exercisesWithTrainingToEdit, User user) throws InvalidDateException, WrongPermissionException {
        trainingService.updateTraining(exercisesWithTrainingToEdit.getTraining(), exercisesWithTrainingToEdit.getExercises(), user.getUserId());
    }
}
