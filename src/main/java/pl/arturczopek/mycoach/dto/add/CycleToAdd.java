package pl.arturczopek.mycoach.dto.add;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 16-10-2016
 */

@Data
public class CycleToAdd implements Serializable {

    private static final long serialVersionUID = -4631036331723794177L;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date startDate;

    @JsonProperty(required = true)
    private List<SetToAdd> sets;
}
