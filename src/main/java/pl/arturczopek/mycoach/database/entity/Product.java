package pl.arturczopek.mycoach.database.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

@Data
@Entity
@Table(name = "Products")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product implements Serializable{

    private static final long serialVersionUID = 5913443781036872464L;

    @Id
    @Column(name = "ProductId", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "products_productid_seq")
    @SequenceGenerator(name = "products_productid_seq", sequenceName = "products_productid_seq", allocationSize = 1)
    private long productId;

    @Column(name = "ProductName", nullable = false, length = 200, unique = true)
    private String productName;

    @Column(name = "ScreenUrl", nullable = false, length = 1903247980)
    private String screenUrl;

    @OneToMany(mappedBy = "product")
    @LazyCollection(LazyCollectionOption.FALSE)
    private List<Price> prices;
}
