package pl.arturczopek.mycoach.model.add;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 16-10-2016
 */

@Data
public class NewCycle implements Serializable {

    private static final long serialVersionUID = -4631036331723794177L;

    @JsonProperty(required = true)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startDate;

    @JsonProperty(required = true)
    private List<NewSet> sets;
}
