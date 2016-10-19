package pl.arturczopek.mycoach.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.arturczopek.mycoach.database.entity.Series;
import pl.arturczopek.mycoach.database.entity.Set;
import pl.arturczopek.mycoach.database.entity.Training;
import pl.arturczopek.mycoach.database.repository.ExerciseRepository;
import pl.arturczopek.mycoach.database.repository.SetRepository;
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
    private SetRepository setRepository;

    @Autowired
    public TrainingService(DateService dateService, ExerciseRepository exerciseRepository, SetRepository setRepository) {
        this.dateService = dateService;
        this.exerciseRepository = exerciseRepository;
        this.setRepository = setRepository;
    }

    @Transactional
    public void addTraining(TrainingToAdd trainingToAdd) {
        Training training = new Training();

        Set set = setRepository.findOne(trainingToAdd.getSetId());

        if(trainingToAdd.getTrainingDate() != null) {
            training.setTrainingDate(trainingToAdd.getTrainingDate());
        } else {
            training.setTrainingDate(dateService.getCurrentDate());
        }

        List<SeriesToAdd> seriesToAdd = trainingToAdd.getSeries();

        List<Series> series = new LinkedList();

        for (SeriesToAdd oneSeriesToAdd :  seriesToAdd) {

            Series tmpSeries = new Series();
            tmpSeries.setExercise(exerciseRepository.findOne(oneSeriesToAdd.getExerciseId()));
            tmpSeries.setWeight(oneSeriesToAdd.getWeight());
            tmpSeries.setRepeats(oneSeriesToAdd.getRepeats());
            tmpSeries.setTraining(training);

            if(oneSeriesToAdd.getComment() != null && oneSeriesToAdd.getComment().length() > 0) {
                tmpSeries.setComment(oneSeriesToAdd.getComment());
            }
            series.add(tmpSeries);
        }

        training.setSeries(series);
        training.setSet(set);

        set.getTrainings().add(training);

        setRepository.save(set);
    }
}
