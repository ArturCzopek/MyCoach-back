package pl.arturczopek.mycoach.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
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
@Table(name = "SETS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "cycle"})
public class Set implements Serializable {

    private static final long serialVersionUID = 6972473475416415119L;

    @Id
    @Column(name = "SET_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "SETS_SET_ID_SEQ")
    @SequenceGenerator(name = "SETS_SET_ID_SEQ", sequenceName = "SETS_SET_ID_SEQ", allocationSize = 1)
    private long setId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "SET_CYC_ID", nullable = false)
    private Cycle cycle;

    @Column(name = "SET_NAME", nullable = false, length = 50)
    private String setName;

    @OneToMany(mappedBy = "set", cascade = CascadeType.ALL)
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Training> trainings;

    @OneToMany(mappedBy = "set", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Exercise> exercises;
}