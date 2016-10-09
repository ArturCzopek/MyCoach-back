package pl.arturczopek.mycoach.database.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Author arturczopek
 * @Date 10/9/16
 */

@Data
@Entity
@Table(name = "Trainings")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "series"})
public class Training implements Serializable {

    private static final long serialVersionUID = 3305548865433699112L;

    @Id
    @Column(name = "TrainingId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "trainings_trainingid_seq")
    @SequenceGenerator(name = "trainings_trainingid_seq", sequenceName = "trainings_trainingid_seq", allocationSize = 1)
    private long trainingId;

    @ManyToOne
    @JoinColumn(name = "setId")
    private Set set;

    @Column(name = "TrainingDate", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Timestamp trainingDate;

    @OneToMany(mappedBy = "training")
    private List<Series> series;
}