package pl.arturczopek.mycoach.database.entity;

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
@Table(name = "Sets")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "cycle"})
public class Set implements Serializable {

    private static final long serialVersionUID = 6972473475416415119L;

    @Id
    @Column(name = "SetId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "sets_setid_seq")
    @SequenceGenerator(name = "sets_setid_seq", sequenceName = "sets_setid_seq", allocationSize = 1)
    private long setId;

    @Column(name = "SetName", nullable = false)
    private String setName;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "CycleId", nullable = false)
    private Cycle cycle;

    @OneToMany(mappedBy = "set", cascade = CascadeType.ALL)
    @JsonIgnore
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Training> trainings;

    @OneToMany(mappedBy = "set", cascade = CascadeType.ALL)
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Exercise> exercises;
}