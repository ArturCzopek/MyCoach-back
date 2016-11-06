package pl.arturczopek.mycoach.database.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

@Data
@Entity
@Table(name = "TrainingDates")
@ToString(exclude = {"set"})
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Training implements Serializable {

    private static final long serialVersionUID = 3305548865433699112L;

    @Id
    @Column(name = "TrainingId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "trainings_trainingid_seq")
    @SequenceGenerator(name = "trainings_trainingid_seq", sequenceName = "trainings_trainingid_seq", allocationSize = 1)
    private long trainingDateId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "setId")
    private Set set;

    @Column(name = "Training", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Date trainingDate;
}