package pl.arturczopek.mycoach.model.add;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 05-11-2016
 */

@Data
public class NewExerciseSession implements Serializable{

    private static final long serialVersionUID = -6451200851024914180L;

    @JsonProperty(required = true)
    private long exerciseId;

    @JsonProperty(required = true)
    private List<NewSeries> series;

    private boolean isEmpty;
}
