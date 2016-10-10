package pl.arturczopek.mycoach.database.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 10/9/16
 */

@Data
@Entity
@Table(name = "Cycles")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Cycle implements Serializable {

    private static final long serialVersionUID = 3865329223753972142L;

    @Id
    @Column(name = "CycleId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "cycles_cycleid_seq")
    @SequenceGenerator(name = "cycles_cycleid_seq", sequenceName = "cycles_cycleid_seq", allocationSize = 1)
    private long cycleId;

    @Column(name = "StartDate", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Timestamp startDate;

    @Column(name = "EndDate", nullable = false)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private Timestamp endDate;

    @OneToMany(mappedBy = "cycle")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Set> sets;
}
