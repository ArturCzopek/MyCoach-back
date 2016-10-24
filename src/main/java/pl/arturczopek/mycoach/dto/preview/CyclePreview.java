package pl.arturczopek.mycoach.dto.preview;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import pl.arturczopek.mycoach.database.entity.Cycle;

import java.io.Serializable;
import java.sql.Date;

/**
 * @Author Artur Czopek
 * @Date 16-10-2016
 */

@Data
public class CyclePreview implements Serializable {

    private static final long serialVersionUID = 5064522414912573866L;

    private long cycleId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date endDate;

    public static CyclePreview buildFromCycle(Cycle cycle) {
        CyclePreview preview = new CyclePreview();
        preview.setCycleId(cycle.getCycleId());
        preview.setStartDate(cycle.getStartDate());
        preview.setEndDate(cycle.getEndDate());

        return preview;
    }
}
