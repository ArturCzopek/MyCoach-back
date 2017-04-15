package pl.arturczopek.mycoach.model.requestDTO;

import lombok.Data;
import pl.arturczopek.mycoach.model.database.ExerciseSession;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 15-04-2017
 */

@Data
public class ExerciseForTrainingPreview implements Serializable{

    private static final long serialVersionUID = 1561535396971479182L;

    private long exerciseId;

    private String exerciseName;

    private String exerciseDescription;

    private List<ExerciseSession> exerciseSessions;
}
