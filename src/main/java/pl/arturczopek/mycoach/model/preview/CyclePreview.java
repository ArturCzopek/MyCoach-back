package pl.arturczopek.mycoach.model.preview;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import pl.arturczopek.mycoach.model.database.Cycle;

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

    private boolean isFinished;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endDate;

    public static CyclePreview buildFromCycle(Cycle cycle) {
        CyclePreview preview = new CyclePreview();
        preview.setCycleId(cycle.getCycleId());
        preview.setFinished(cycle.isFinished());
        preview.setStartDate(cycle.getStartDate());
        preview.setEndDate(cycle.getEndDate());

        return preview;
    }
}
