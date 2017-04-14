package pl.arturczopek.mycoach.model.preview;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import pl.arturczopek.mycoach.model.database.Report;

import java.io.Serializable;
import java.sql.Date;

/**
 * @Author Artur Czopek
 * @Date 10-10-2016
 */

@Data
public class ReportPreview implements Serializable{

    private static final long serialVersionUID = -6799302021991715914L;

    private long reportId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date endDate;

    public static ReportPreview buildFromReport(Report report) {
        ReportPreview preview = new ReportPreview();
        preview.setReportId(report.getReportId());
        preview.setStartDate(report.getStartDate());
        preview.setEndDate(report.getEndDate());

        return preview;
    }
}
