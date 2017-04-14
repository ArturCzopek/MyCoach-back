package pl.arturczopek.mycoach.model.database;

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
@Table(name = "WEIGHTS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Weight implements Serializable {

    private static final long serialVersionUID = -4251427345815202610L;

    @Id
    @Column(name = "WGH_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "WEIGHTS_WGH_ID_SEQ")
    @SequenceGenerator(name = "WEIGHTS_WGH_ID_SEQ", sequenceName = "WEIGHTS_WGH_ID_SEQ", allocationSize = 1)
    private long weightId;

    @Column(name = "WGH_VAL", nullable = false)
    private float value;

    @Column(name = "WGH_MSRM_DT", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date measurementDate;
}
