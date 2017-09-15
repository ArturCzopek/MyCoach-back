package pl.arturczopek.mycoach.model.database

import com.fasterxml.jackson.annotation.JsonIgnore
import javax.persistence.*

/**
 * @Author Artur Czopek
 * @Date 17-02-2017
 */

@Entity
@Table(name = "DICTIONARIES")
data class DictionaryEntry(
        @Id
        @Column(name = "DCT_ID", nullable = false)
        @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "DICTIONARIES_DCT_ID_SEQ")
        @SequenceGenerator(name = "DICTIONARIES_DCT_ID_SEQ", sequenceName = "DICTIONARIES_DCT_ID_SEQ", allocationSize = 1)
        var dictionaryEntryId: Long = 0,

        @Column(name = "DCT_KEY", nullable = false, length = 100)
        var key: String = "",

        @Column(name = "DCT_VAL", nullable = false, length = 100)
        var value: String = "",

        @ManyToOne
        @JsonIgnore
        @JoinColumn(name = "DCT_LNG_ID")
        var language: Language = Language()
)