package pl.arturczopek.mycoach.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository, ExerciseSessionRepository exerciseSessionRepository, SeriesRepository seriesRepository, SetRepository setRepository, TrainingRepository trainingRepository) {
        this.exerciseRepository = exerciseRepository;
        this.exerciseSessionRepository = exerciseSessionRepository;
        this.seriesRepository = seriesRepository;
        this.setRepository = setRepository;
        this.trainingRepository = trainingRepository;
    }

    @Transactional
    public void addExercises(List<NewExercise> exercises) {
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

    public void deleteExercise(Exercise exercise) {

        Set setWithExercise = setRepository.findOne(exercise.getSetId());

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
    public void updateExercise(Exercise exercise) {
        Exercise exerciseToUpdate = exerciseRepository.findOne(exercise.getExerciseId());
        exerciseToUpdate.setExerciseName(exercise.getExerciseName());
        exerciseToUpdate.setExerciseDescription(exercise.getExerciseDescription());

        exerciseRepository.save(exerciseToUpdate);
    }
}
