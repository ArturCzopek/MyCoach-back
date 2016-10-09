package pl.arturczopek.mycoach.database.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

/**
 * @Author Artur Czopek
 * @Date 10/9/16
 */

@Data
@Entity
@Table(name = "Prices")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler, product"})
public class Price implements Serializable{

    private static final long serialVersionUID = 4215492868990889864L;

    @Id
    @Column(name = "PriceId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "prices_priceid_seq")
    @SequenceGenerator(name = "prices_priceid_seq", sequenceName = "prices_priceid_seq", allocationSize = 1)
    private long priceId;

    @ManyToOne
    @JoinColumn(name = "ProductId", nullable = false)
    private Product product;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Column(name = "PriceDate", nullable = false)
    private Timestamp priceDate;

    @Column(name = "Price", nullable = false, length = 100)
    private float price;

    @Column(name = "Place", nullable = false)
    private String place;
}