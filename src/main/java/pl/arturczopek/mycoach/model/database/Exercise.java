package pl.arturczopek.mycoach.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

@Data
@Entity
@Table(name = "EXERCISES")
@ToString(exclude = "set")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Exercise implements Serializable {

    private static final long serialVersionUID = -8266637636238912062L;

    @Id
    @Column(name = "EXR_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "exercises_exerciseid_seq")
    @SequenceGenerator(name = "exercises_exerciseid_seq", sequenceName = "exercises_exerciseid_seq", allocationSize = 1)
    private long exerciseId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "EXR_SET_ID")
    private Set set;

    @Column(name = "EXR_NAME", nullable = false, length = 80)
    private String exerciseName;

    @Column(name = "EXR_DSCP", length = 300)
    private String exerciseDescription;

    @OneToMany(mappedBy = "exercise", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<ExerciseSession> exerciseSessions;
}
