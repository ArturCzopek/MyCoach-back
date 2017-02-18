package pl.arturczopek.mycoach.database.entity;

import lombok.Data;
import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @Author Artur Czopek
 * @Date 17-02-2017
 */

@Data
@Entity
@Table(name = "USERSETTINGS")
public class UserSetting implements Serializable {
    private static final long serialVersionUID = -4742374380575086257L;

    @Id
    @Column(name = "USS_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "USERSETTINGS_USS_ID_SEQ")
    @SequenceGenerator(name = "USERSETTINGS_USS_ID_SEQ", sequenceName = "USERSETTINGS_USS_ID_SEQ", allocationSize = 1)
    private long userId;

    @Email
    @Column(name = "USS_MAIL", length = 100)
    private String infoMail;

    @ManyToOne
    @JoinColumn(name = "USS_LNG_ID")
    private Language language;
}
