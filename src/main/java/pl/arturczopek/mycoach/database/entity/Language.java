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
@Table(name = "LANGUAGES")
public class Language implements Serializable {

    private static final long serialVersionUID = -597542711641061972L;

    @Id
    @Column(name = "LNG_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "LANGUAGES_LNG_ID")
    @SequenceGenerator(name = "LANGUAGES_LNG_ID", sequenceName = "LANGUAGES_LNG_ID", allocationSize = 1)
    private long languageId;

    @Column(name = "LNG_SHRT_NAME", nullable = false, length = 3)
    private String shortName;

    @Column(name = "LNG_FULL_NAME", nullable = false, length = 20)
    private String fullName;
}
