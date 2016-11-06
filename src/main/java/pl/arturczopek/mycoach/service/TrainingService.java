package pl.arturczopek.mycoach.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pl.arturczopek.mycoach.database.entity.*;
import pl.arturczopek.mycoach.database.repository.ExerciseRepository;
import pl.arturczopek.mycoach.database.repository.ExerciseSessionRepository;
import pl.arturczopek.mycoach.database.repository.SetRepository;
import pl.arturczopek.mycoach.database.repository.TrainingRepository;
import pl.arturczopek.mycoach.dto.add.ExerciseSessionToAdd;
import pl.arturczopek.mycoach.dto.add.SeriesToAdd;
import pl.arturczopek.mycoach.dto.add.TrainingToAdd;

import java.util.LinkedList;
import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 10-10-2016
 */

@Service
public class TrainingService {

    private DateService dateService;
    private ExerciseRepository exerciseRepository;
    private ExerciseSessionRepository exerciseSessionRepository;
    private SetRepository setRepository;
    private TrainingRepository trainingRepository;

    @Autowired
    public TrainingService(DateService dateService, ExerciseRepository exerciseRepository, ExerciseSessionRepository exerciseSessionRepository, SetRepository setRepository, TrainingRepository trainingRepository) {
        this.dateService = dateService;
        this.exerciseRepository = exerciseRepository;
        this.exerciseSessionRepository = exerciseSessionRepository;
        this.setRepository = setRepository;
        this.trainingRepository = trainingRepository;
    }

    @Transactional
    public void addTraining(TrainingToAdd trainingToAdd) {

        Training training = new Training();
        Set set = setRepository.findOne(trainingToAdd.getSetId());

        if (trainingToAdd.getDate() != null) {
            training.setTrainingDate(trainingToAdd.getDate());
        } else {
            training.setTrainingDate(dateService.getCurrentDate());
        }

        training.setSet(set);
        trainingRepository.save(training);

        List<Series> series = new LinkedList<>();

        for (ExerciseSessionToAdd exerciseSessionToAdd : trainingToAdd.getExerciseSessions()) {

            Exercise exercise = exerciseRepository.findOne(exerciseSessionToAdd.getExerciseId());

            ExerciseSession exerciseSession = new ExerciseSession();
            exerciseSession.setExercise(exercise);

            for (SeriesToAdd seriesToAdd : exerciseSessionToAdd.getSeries()) {

                Series tmpSeries = new Series();
                tmpSeries.setWeight(seriesToAdd.getWeight());
                tmpSeries.setRepeats(seriesToAdd.getRepeats());
                tmpSeries.setExerciseSession(exerciseSession);

                if (!StringUtils.isEmpty(seriesToAdd.getComment())) {
                    tmpSeries.setComment(seriesToAdd.getComment());
                }

                series.add(tmpSeries);
            }

            exerciseSession.setSeries(series);
            exerciseSessionRepository.save(exerciseSession);

            exercise.getExerciseSessions().add(exerciseSession);

            exerciseRepository.save(exercise);
        }
    }

    public List<Training> getTrainingDatesForSet(long id) {
        return setRepository.findOne(id).getTrainings();
    }
}
