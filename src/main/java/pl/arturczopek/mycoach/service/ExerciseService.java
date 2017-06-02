package pl.arturczopek.mycoach.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import pl.arturczopek.mycoach.exception.DuplicatedNameException;
import pl.arturczopek.mycoach.exception.WrongPermissionException;
import pl.arturczopek.mycoach.model.add.NewExercise;
import pl.arturczopek.mycoach.model.database.*;
import pl.arturczopek.mycoach.repository.*;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 15-04-2017
 */

@Service
public class ExerciseService {

    private ExerciseRepository exerciseRepository;
    private ExerciseSessionRepository exerciseSessionRepository;
    private SeriesRepository seriesRepository;
    private SetRepository setRepository;
    private TrainingRepository trainingRepository;
    private CycleRepository cycleRepository;
    private DictionaryService dictionaryService;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository, ExerciseSessionRepository exerciseSessionRepository,
                           SeriesRepository seriesRepository, SetRepository setRepository, TrainingRepository trainingRepository,
                           DictionaryService dictionaryService, CycleRepository cycleRepository) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseSessionRepository = exerciseSessionRepository;
        this.seriesRepository = seriesRepository;
        this.setRepository = setRepository;
        this.trainingRepository = trainingRepository;
        this.dictionaryService = dictionaryService;
        this.cycleRepository = cycleRepository;
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "cycle", allEntries = true),
            @CacheEvict(value = "activeCycle", key ="#userId")
    })
    public void addExercises(List<NewExercise> exercises, long userId) throws DuplicatedNameException, WrongPermissionException {

        for (NewExercise newExercise : exercises) {
            if (!isNewExerciseNameCorrect(newExercise.getExerciseName(), newExercise.getSetId())) {
                throw new DuplicatedNameException(dictionaryService.translate("page.trainings.exercise.error.invalidExerciseName.message", userId).getValue());
            }
        }

        for (NewExercise newExercise: exercises) {
            Set set = setRepository.findOne(newExercise.getSetId());
            Cycle cycle = cycleRepository.findOneBySetsContains(set);

            if (cycle.getUserId() != userId) {
                throw new WrongPermissionException(dictionaryService.translate("global.error.wrongPermission.message", userId).getValue());
            }
        }

        for (NewExercise newExercise : exercises) {

            Exercise exercise = new Exercise();
            exercise.setExerciseName(newExercise.getExerciseName());
            exercise.setSetId(newExercise.getSetId());
            exerciseRepository.save(exercise);

            Set set = setRepository.findOne(newExercise.getSetId());

            List<ExerciseSession> sessions = new LinkedList<>();

            for (int i = 0; i < set.getTrainings().size(); i++) {
                ExerciseSession session = new ExerciseSession();
                session.setExerciseId(exercise.getExerciseId());
                session.setEmpty(true);
                session.setSeries(Collections.emptyList());
                sessions.add(session);
            }

            exercise.setExerciseSessions(sessions);
            exercise.setExerciseDescription(newExercise.getExerciseDescription());
            exerciseRepository.save(exercise);
        }
    }

    @Caching(evict = {
            @CacheEvict(value = "cycle", allEntries = true),
            @CacheEvict(value = "activeCycle", key ="#userId")
    })
    public void deleteExercise(Exercise exercise, long userId) throws WrongPermissionException {

        Set setWithExercise = setRepository.findOne(exercise.getSetId());
        Cycle cycle = cycleRepository.findOneBySetsContains(setWithExercise);

        if (cycle.getUserId() != userId) {
            throw new WrongPermissionException(dictionaryService.translate("global.error.wrongPermission.message", userId).getValue());
        }

        boolean isLastExercise = setWithExercise.getExercises().size() == 1;

        exercise.getExerciseSessions().forEach((ExerciseSession session) -> {
            session.getSeries().forEach((Series series) -> seriesRepository.delete(series.getSeriesId()));
            exerciseSessionRepository.delete(session.getExerciseSessionId());
        });

        exerciseRepository.delete(exercise.getExerciseId());

        if (isLastExercise) {
            setWithExercise.getTrainings().forEach((Training training) -> trainingRepository.delete(training.getTrainingId()));
        }
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "cycle", allEntries = true),
            @CacheEvict(value = "activeCycle", key ="#userId")
    })
    public void updateExercise(Exercise exercise, long userId) throws DuplicatedNameException, WrongPermissionException {

        if (!isUpdateExerciseNameCorrect(exercise.getExerciseName(), exercise.getSetId(), exercise.getExerciseId())) {
            throw new DuplicatedNameException(dictionaryService.translate("page.trainings.exercise.error.invalidExerciseName.message", userId).getValue());
        }

        Set setWithExercise = setRepository.findOne(exercise.getSetId());
        Cycle cycle = cycleRepository.findOneBySetsContains(setWithExercise);

        if (cycle.getUserId() != userId) {
            throw new WrongPermissionException(dictionaryService.translate("global.error.wrongPermission.message", userId).getValue());
        }

        Exercise exerciseToUpdate = exerciseRepository.findOne(exercise.getExerciseId());
        exerciseToUpdate.setExerciseName(exercise.getExerciseName());
        exerciseToUpdate.setExerciseDescription(exercise.getExerciseDescription());

        exerciseRepository.save(exerciseToUpdate);
    }

    private boolean isNewExerciseNameCorrect(String exerciseName, long setId) {
        List<Exercise> exercisesInSet = setRepository.findOne(setId).getExercises();

        return !exercisesInSet
                .stream()
                .anyMatch(exerciseFromDb ->
                        exerciseFromDb.getExerciseName().trim().equalsIgnoreCase(exerciseName.trim())
                );
    }

    private boolean isUpdateExerciseNameCorrect(String exerciseName, long setId, long exerciseId) {
        List<Exercise> exercisesInSet = setRepository.findOne(setId).getExercises();

        return !exercisesInSet
                .stream()
                .anyMatch(exerciseFromDb ->
                        exerciseFromDb.getExerciseName().trim().equalsIgnoreCase(exerciseName.trim())
                                && exerciseFromDb.getExerciseId() != exerciseId
                );
    }
}
