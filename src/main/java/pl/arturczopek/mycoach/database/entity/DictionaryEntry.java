package pl.arturczopek.mycoach.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Author Artur Czopek
 * @Date 17-02-2017
 */

@Data
@Entity
@Table(name = "DICTIONARIES")
public class DictionaryEntry implements Serializable {

    private static final long serialVersionUID = 3911599422311278880L;

    @Id
    @Column(name = "DCT_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "DICTIONARIES_DCT_ID_SEQ")
    @SequenceGenerator(name = "DICTIONARIES_DCT_ID_SEQ", sequenceName = "DICTIONARIES_DCT_ID_SEQ", allocationSize = 1)
    private long dictionaryEntryId;

    @Column(name = "DCT_KEY", nullable = false, length = 100)
    private String key;

    @Column(name = "DCT_VAL", nullable = false, length = 30)
    private String value;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "DCT_LNG_ID")
    private Language language;
}
