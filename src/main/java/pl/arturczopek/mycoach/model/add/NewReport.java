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
public class NewReport {

    @JsonProperty(required = true)
    private String content;

    @JsonProperty(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date startDate;

    @JsonProperty(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date endDate;
}
