package pl.arturczopek.mycoach.model.add;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.sql.Date;

/**
 * @Author Artur Czopek
 * @Date 14-04-2017
 */

@Data
public class NewWeight {

    @JsonProperty(required = true)
    private float value;

    @JsonProperty(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date measurementDate;
}
