package pl.arturczopek.mycoach.database.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

@Data
@Entity
@Table(name = "REPORTS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Report implements Serializable {

    private static final long serialVersionUID = 1130767238111263379L;

    @Id
    @Column(name = "REP_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "REPORTS_REP_ID_SEQ")
    @SequenceGenerator(name = "REPORTS_REP_ID_SEQ", sequenceName = "REPORTS_REP_ID_SEQ", allocationSize = 1)
    private long reportId;

    @Column(name = "REP_CONT", nullable = false, length = 10000)
    private String content;

    @Column(name = "REP_STRT_DT", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date startDate;

    @Column(name = "REP_END_DT", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date endDate;
}
