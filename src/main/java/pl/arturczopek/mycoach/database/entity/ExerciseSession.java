package pl.arturczopek.mycoach.database.entity;

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
 * @Date 05-11-2016
 */

@Data
@Entity
@Table(name = "ExerciseSessions")
@ToString(exclude = "exercise")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ExerciseSession implements Serializable {

    private static final long serialVersionUID = 8492177724859436106L;

    @Id
    @Column(name = "ExerciseSessionId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "exercisesessions_exercisesessionid_seq")
    @SequenceGenerator(name = "exercisesessions_exercisesessionid_seq", sequenceName = "exercisesessions_exercisesessionid_seq", allocationSize = 1)
    private long exerciseSessionId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "ExerciseId")
    private Exercise exercise;

    @OneToMany(mappedBy = "exerciseSession", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Series> series;

}
