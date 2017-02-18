package pl.arturczopek.mycoach.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

@Data
@Entity
@Table(name = "SERIES")
@ToString(exclude  = "exerciseSession")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Series implements Serializable {
    private static final long serialVersionUID = 601005672180488637L;

    @Id
    @Column(name = "SER_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SERIES_SER_ID_SEQ")
    @SequenceGenerator(name = "SERIES_SER_ID_SEQ", sequenceName = "SERIES_SER_ID_SEQ", allocationSize = 1)
    private long seriesId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "SER_EXS_ID")
    private ExerciseSession exerciseSession;

    @Column(name = "SER_WGH", nullable = false)
    private float weight;

    @Column(name = "SER_REP", nullable = false)
    private int repeats;

    @Column(name = "SER_COM", length = 250)
    private String comment;
}
