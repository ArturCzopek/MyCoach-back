package pl.arturczopek.mycoach.model.request.dto;

import lombok.Data;
import pl.arturczopek.mycoach.model.database.Exercise;
import pl.arturczopek.mycoach.model.database.Training;

import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 15-04-2017
 */

@Data
public class ExercisesWithTrainingToEdit {

    private Training training;

    private List<Exercise> exercises;
}
