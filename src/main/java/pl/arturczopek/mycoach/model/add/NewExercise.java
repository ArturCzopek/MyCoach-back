package pl.arturczopek.mycoach.model.add;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Artur Czopek
 * @Date 14-04-2017
 */

@Data
public class NewExercise implements Serializable{

    private static final long serialVersionUID = 3386737927659545718L;

    @JsonProperty(required = true)
    private long setId;

    @JsonProperty(required = true)
    private String exerciseName;

    private String exerciseDescription;

}
