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
@Table(name = "UserSettings")
public class UserSetting implements Serializable {
    private static final long serialVersionUID = -4742374380575086257L;

    @Id
    @Column(name = "UserId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "usersettings_userid_seq")
    @SequenceGenerator(name = "usersettings_userid_seq", sequenceName = "usersettings_userid_seq", allocationSize = 1)
    private long userId;

    @Column(name = "InfoMail", length = 100)
    private String infoMail;

    @ManyToOne
    @JoinColumn(name = "languageId")
    private Language language;
}
