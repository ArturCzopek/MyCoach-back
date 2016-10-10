package pl.arturczopek.mycoach.database.entity.dto;

import lombok.Data;
import pl.arturczopek.mycoach.database.entity.Cycle;

@Data
public class CyclePreview {

    private long cycleId;

    public static CyclePreview buildFromCycle(Cycle cycle) {
        CyclePreview preview = new CyclePreview();
        preview.setCycleId(cycle.getCycleId());

        return preview;
    }
}
