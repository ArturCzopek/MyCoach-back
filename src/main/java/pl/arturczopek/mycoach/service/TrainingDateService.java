package pl.arturczopek.mycoach.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import pl.arturczopek.mycoach.database.entity.*;
import pl.arturczopek.mycoach.database.repository.ExerciseRepository;
import pl.arturczopek.mycoach.database.repository.SetRepository;
import pl.arturczopek.mycoach.database.repository.TrainingDateRepository;
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
public class TrainingDateService {

    private DateService dateService;
    private ExerciseRepository exerciseRepository;
    private SetRepository setRepository;
    private TrainingDateRepository trainingDateRepository;

    @Autowired
    public TrainingDateService(DateService dateService, ExerciseRepository exerciseRepository, SetRepository setRepository, TrainingDateRepository trainingDateRepository) {
        this.dateService = dateService;
        this.exerciseRepository = exerciseRepository;
        this.setRepository = setRepository;
        this.trainingDateRepository = trainingDateRepository;
    }

    @Transactional
    public void addTraining(TrainingToAdd trainingToAdd) {

        TrainingDate trainingDate = new TrainingDate();
        Set set = setRepository.findOne(trainingToAdd.getSetId());

        if (trainingToAdd.getTrainingDate() != null) {
            trainingDate.setTrainingDate(trainingToAdd.getTrainingDate());
        } else {
            trainingDate.setTrainingDate(dateService.getCurrentDate());
        }

        trainingDate.setSet(set);
        trainingDateRepository.save(trainingDate);

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
            exercise.getExerciseSessions().add(exerciseSession);

            exerciseRepository.save(exercise);
        }
    }

    public List<TrainingDate> getTrainingDatesForSet(long id) {
        return setRepository.findOne(id).getTrainingDates();
    }
}
