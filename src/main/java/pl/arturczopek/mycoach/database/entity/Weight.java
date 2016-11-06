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
@Table(name = "Weights")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Weight implements Serializable {

    private static final long serialVersionUID = -4251427345815202610L;

    @Id
    @Column(name = "WeightId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "weights_weightid_seq")
    @SequenceGenerator(name = "weights_weightid_seq", sequenceName = "weights_weightid_seq", allocationSize = 1)
    private long weightId;

    @Column(name = "Value", nullable = false)
    private float value;

    @Column(name = "MeasurementDate", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date measurementDate;
}
