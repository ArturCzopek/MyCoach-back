package pl.arturczopek.mycoach.database.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Author Artur Czopek
 * @Date 17-02-2017
 */

@Data
@Entity
@Table(name = "Languages")
public class Language implements Serializable {

    private static final long serialVersionUID = -597542711641061972L;

    @Id
    @Column(name = "LanguageId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "languages_languageid_seq")
    @SequenceGenerator(name = "languages_languageid_seq", sequenceName = "languages_languageid_seq", allocationSize = 1)
    private long languageId;

    @Column(name = "ShortName", nullable = false, length = 3)
    private String shortName;

    @Column(name = "FullName", nullable = false, length = 20)
    private String fullName;
}
