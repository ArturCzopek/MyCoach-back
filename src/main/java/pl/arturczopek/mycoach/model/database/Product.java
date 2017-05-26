package pl.arturczopek.mycoach.model.database;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

@Data
@Entity
@Table(name = "PRODUCTS")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product implements Serializable {

    private static final long serialVersionUID = 5913443781036872464L;

    @Id
    @Column(name = "PRD_ID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "PRODUCTS_PRD_ID_SEQ")
    @SequenceGenerator(name = "PRODUCTS_PRD_ID_SEQ", sequenceName = "PRODUCTS_PRD_ID_SEQ", allocationSize = 1)
    private long productId;

    @Column(name = "PRD_NAME", nullable = false, length = 60)
    private String productName;

    @Column(name = "PRD_SCRN", length = 100)
    @Lob
    private byte[] screen;

    @Transient
    private float average = 0;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "PRC_PRD_ID")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Price> prices;

    @JsonIgnore
    @Column(name = "CYC_PRD_ID")
    private long userId;

    public void countAveragePrice() {


        BigDecimal pricesSum = new BigDecimal(0).setScale(2, BigDecimal.ROUND_UP);
        BigDecimal quantitySum = new BigDecimal(0).setScale(2, BigDecimal.ROUND_UP);

        if (!prices.isEmpty()) {
            for (Price price : prices) {
                pricesSum = pricesSum.add(BigDecimal.valueOf(price.getValue()));
                quantitySum = quantitySum.add(BigDecimal.valueOf(price.getQuantity()));
            }
        }

        if (prices.isEmpty()) {
            this.average = 0;
        } else {
            this.average = pricesSum.divide(quantitySum, 2, BigDecimal.ROUND_UP).floatValue();
        }
    }
}
