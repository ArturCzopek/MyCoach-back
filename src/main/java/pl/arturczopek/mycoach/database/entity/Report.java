package pl.arturczopek.mycoach.database.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Author Artur Czopek
 * @Date 10/9/16
 */

@Data
@Entity
@Table(name = "Reports")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Report implements Serializable{

    private static final long serialVersionUID = 1130767238111263379L;

    @Id
    @Column(name = "ReportId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "reports_reportid_seq")
    @SequenceGenerator(name = "reports_reportid_seq", sequenceName = "reports_reportid_seq", allocationSize = 1)
    private long reportId;

    @Column(name = "Content", nullable = false, length = 1903247980)
    private String content;

    @Column(name = "StartDate", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Timestamp startDate;

    @Column(name = "EndDate", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Timestamp endDate;
}
