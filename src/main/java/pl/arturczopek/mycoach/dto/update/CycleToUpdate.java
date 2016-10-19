package pl.arturczopek.mycoach.dto.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author Artur Czopek
 * @Date 16-10-2016
 */

@Data
public class CycleToUpdate implements Serializable{

    private static final long serialVersionUID = -7337307260516865608L;

    @JsonProperty(required = true)
    private long cycleId;

    private Date endDate;

    private Date startDate;
}
