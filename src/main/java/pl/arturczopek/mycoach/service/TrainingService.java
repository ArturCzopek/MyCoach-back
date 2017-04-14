package pl.arturczopek.mycoach.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.arturczopek.mycoach.model.database.Training;
import pl.arturczopek.mycoach.repository.ExerciseRepository;
import pl.arturczopek.mycoach.repository.ExerciseSessionRepository;
import pl.arturczopek.mycoach.repository.SetRepository;
import pl.arturczopek.mycoach.repository.TrainingRepository;

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

//    @Transactional
//    public void addTraining(TrainingToAdd trainingToAdd) {
//
//        Training training = new Training();
//        Set set = setRepository.findOne(trainingToAdd.getSetId());
//
//        if (trainingToAdd.getDate() != null) {
//            training.setTrainingDate(trainingToAdd.getDate());
//        } else {
//            training.setTrainingDate(dateService.getCurrentDate());
//        }
//
//        training.setSet(set);
//        trainingRepository.save(training);
//
//        List<Series> series = new LinkedList<>();
//
//        for (NewExerciseSession newExerciseSession : trainingToAdd.getExerciseSessions()) {
//
//            Exercise exercise = exerciseRepository.findOne(newExerciseSession.getExerciseId());
//
//            ExerciseSession exerciseSession = new ExerciseSession();
//            exerciseSession.setExercise(exercise);
//
//            for (NewSeries newSeries : newExerciseSession.getSeries()) {
//
//                Series tmpSeries = new Series();
//                tmpSeries.setWeight(newSeries.getWeight());
//                tmpSeries.setRepeats(newSeries.getRepeats());
//                tmpSeries.setExerciseSession(exerciseSession);
//
//                if (!StringUtils.isEmpty(newSeries.getComment())) {
//                    tmpSeries.setComment(newSeries.getComment());
//                }
//
//                series.add(tmpSeries);
//            }
//
//            exerciseSession.setSeries(series);
//            exerciseSessionRepository.save(exerciseSession);
//
//            exercise.getExerciseSessions().add(exerciseSession);
//
//            exerciseRepository.save(exercise);
//        }
//    }

    public List<Training> getTrainingDatesForSet(long id) {
        return setRepository.findOne(id).getTrainings();
    }
}
