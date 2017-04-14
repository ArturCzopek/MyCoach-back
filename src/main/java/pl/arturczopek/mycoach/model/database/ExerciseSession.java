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
 * @Date 05-11-2016
 */

@Data
@Entity
@Table(name = "EXERCISESESSIONS")
@ToString(exclude = "exercise")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ExerciseSession implements Serializable {

    private static final long serialVersionUID = 8492177724859436106L;

    @Id
    @Column(name = "EXS_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "EXERCISESESSIONS_EXS_ID")
    @SequenceGenerator(name = "EXERCISESESSIONS_EXS_ID", sequenceName = "EXERCISESESSIONS_EXS_ID", allocationSize = 1)
    private long exerciseSessionId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "EXS_EXR_ID")
    private Exercise exercise;

    @Column(name = "EXS_EMP")
    private boolean isEmpty;

    @OneToMany(mappedBy = "exerciseSession", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Series> series;

}
