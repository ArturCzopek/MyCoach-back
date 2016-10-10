package pl.arturczopek.mycoach.database.entity.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import pl.arturczopek.mycoach.database.entity.Report;

import java.sql.Timestamp;

/**
 * @Author arturczopek
 * @Date 10/10/16
 */

@Data
public class ReportPreview {

    private long reportId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Timestamp startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Timestamp endDate;

    public static ReportPreview buildFromReport(Report report) {
        ReportPreview preview = new ReportPreview();
        preview.setReportId(report.getReportId());
        preview.setStartDate(report.getStartDate());
        preview.setEndDate(report.getEndDate());

        return preview;
    }
}
