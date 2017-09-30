package pl.arturczopek.mycoach.model.database;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import javax.persistence.*

/**
 * @Author Artur Czopek
 * @Date 05-11-2016
 */

@Entity
@Table(name = "EXERCISESESSIONS")
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class ExerciseSession(

    @Id
    @Column(name = "EXS_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "EXERCISESESSIONS_EXS_ID")
    @SequenceGenerator(name = "EXERCISESESSIONS_EXS_ID", sequenceName = "EXERCISESESSIONS_EXS_ID", allocationSize = 1)
    var exerciseSessionId: Long = 0,

    @Column(name = "EXS_EMP")
    var isEmpty: Boolean = true,

    @Column(name = "EXS_EXR_ID")
    var exerciseId: Long = 0,

    @OneToMany(cascade = arrayOf(CascadeType.ALL))
    @JoinColumn(name = "SER_EXS_ID")
    @LazyCollection(LazyCollectionOption.FALSE)
    val series: List<Series> = mutableListOf()
)