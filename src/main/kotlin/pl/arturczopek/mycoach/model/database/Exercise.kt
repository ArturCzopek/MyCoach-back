package pl.arturczopek.mycoach.model.database

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.hibernate.annotations.LazyCollection
import org.hibernate.annotations.LazyCollectionOption
import javax.persistence.*

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

@Entity
@Table(name = "EXERCISES")
@JsonIgnoreProperties("hibernateLazyInitializer", "handler")
data class Exercise(

        @Id
        @Column(name = "EXR_ID", nullable = false)
        @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "exercises_exerciseid_seq")
        @SequenceGenerator(name = "exercises_exerciseid_seq", sequenceName = "exercises_exerciseid_seq", allocationSize = 1)
        var exerciseId: Long = 0,

        @Column(name = "EXR_NAME", nullable = false, length = 80)
        var exerciseName: String = "",

        @Column(name = "EXR_DSCP", length = 300)
        var exerciseDescription: String = "",

        @Column(name = "EXR_SET_ID")
        var setId: Long = 0,

        @OneToMany(cascade = arrayOf(CascadeType.ALL))
        @JoinColumn(name = "EXS_EXR_ID")
        @LazyCollection(LazyCollectionOption.FALSE)
        val exerciseSessions: List<ExerciseSession> = mutableListOf()
)
