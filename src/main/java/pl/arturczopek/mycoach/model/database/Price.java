package pl.arturczopek.mycoach.model.database;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

@Data
@Entity
@Table(name = "PRICES")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Price implements Serializable{

    private static final long serialVersionUID = 4215492868990889864L;

    @Id
    @Column(name = "PRC_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "PRICES_PRC_ID_SEQ")
    @SequenceGenerator(name = "PRICES_PRC_ID_SEQ", sequenceName = "PRICES_PRC_ID_SEQ", allocationSize = 1)
    private long priceId;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "PRC_PRD_ID", nullable = false)
    private Product product;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(name = "PRC_DT", nullable = false)
    private Date priceDate;

    @Column(name = "PRC_VAL", nullable = false)
    private float value;

    @Column(name = "PRC_QNT")
    private float quantity;

    @Column(name = "PRC_PLC", nullable = false, length = 60)
    private String place;
}