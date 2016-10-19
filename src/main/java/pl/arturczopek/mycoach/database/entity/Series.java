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
@Table(name = "Series")
@ToString(exclude  = "exercise")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Series implements Serializable {
    private static final long serialVersionUID = 601005672180488637L;

    @Id
    @Column(name = "SeriesId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "series_seriesid_seq")
    @SequenceGenerator(name = "series_seriesid_seq", sequenceName = "series_seriesid_seq", allocationSize = 1)
    private long seriesId;

    @ManyToOne
    @JoinColumn(name = "trainingId")
    private Training training;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "exerciseId")
    private Exercise exercise;

    @Column(name = "Weight", nullable = false)
    private float weight;

    @Column(name = "Repeats", nullable = false)
    private int repeats;

    @Column(name = "Comment")
    private String comment;
}
