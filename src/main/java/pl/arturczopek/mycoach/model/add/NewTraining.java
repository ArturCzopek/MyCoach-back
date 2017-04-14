package pl.arturczopek.mycoach.model.add;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Date;
import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 14-04-2017
 */

@Data
public class NewTraining {

    @JsonProperty(required = true)
    private long setId;

    @JsonProperty(required = true)
    private List<NewExerciseSession> exerciseSessions;

    @JsonProperty(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date trainingDate;
}
