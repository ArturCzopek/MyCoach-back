package pl.arturczopek.mycoach.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import pl.arturczopek.mycoach.model.database.Product;

import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */

interface ProductRepository : PagingAndSortingRepository<Product, Long> {

    override fun findAll(): List<Product>

    @Query("select p from Product p where " +
            "p.userId = :userId " +
            "and UPPER(p.productName) not like UPPER(:tmpProduct) " +
            "and UPPER(p.productName) not like UPPER(:editedProduct) " +
            "order by p.productName desc")
    fun findValidProducts(@Param("tmpProduct") tmpProduct: String, @Param("editedProduct") editedProduct: String, @Param("userId") userId: Long): List<Product>

    fun findOneByProductNameIgnoreCaseAndUserId(productName: String, userId: Long): Product

    fun findOneByProductNameIgnoreCaseAndUserIdAndProductIdNot(productName: String, userId: Long, productId: Long): Product
}
