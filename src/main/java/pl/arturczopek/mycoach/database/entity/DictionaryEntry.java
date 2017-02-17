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
@Table(name = "Dictionaries")
public class DictionaryEntry implements Serializable {

    private static final long serialVersionUID = 3911599422311278880L;

    @Id
    @Column(name = "DictionaryEntryId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "dictionaries_dictionaryentryid_seq")
    @SequenceGenerator(name = "dictionaries_dictionaryentryid_seq", sequenceName = "dictionaries_dictionaryentryid_seq", allocationSize = 1)
    private long dictionaryEntryId;

    @Column(name = "Key", nullable = false, length = 60)
    private String key;

    @Column(name = "Value", nullable = false, length = 60)
    private String value;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "languageId")
    private Language language;
}
