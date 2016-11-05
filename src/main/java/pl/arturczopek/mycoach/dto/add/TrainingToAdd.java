package pl.arturczopek.mycoach.dto.add;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 10-10-2016
 */

@Data
public class TrainingToAdd implements Serializable {

    private static final long serialVersionUID = 5743415892929405622L;

    @JsonProperty(required = true)
    private long setId;

    private Date trainingDate;

    @JsonProperty(required = true)
    List<ExerciseSessionToAdd> exerciseSessions;
}
