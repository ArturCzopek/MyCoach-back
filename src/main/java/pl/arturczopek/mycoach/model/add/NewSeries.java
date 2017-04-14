package pl.arturczopek.mycoach.model.add;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Artur Czopek
 * @Date 10-10-2016
 */

@Data
public class NewSeries implements Serializable {

    private static final long serialVersionUID = 2146480910059109272L;
    
    @JsonProperty(required = true)
    private float weight;

    @JsonProperty(required = true)
    private int repeats;

    private String comment;
}
