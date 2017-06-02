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
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

    @Override
    List<Product> findAll();

    @Query("select p from Product p where userId = :userId and UPPER(p.productName) not like upper(:tmpProduct) and upper(p.productName) not like upper(:editedProduct) order by p.productName desc")
    List<Product> findValidProducts(@Param("tmpProduct") String tmpProduct, @Param("editedProduct") String editedProduct, @Param("userId") long userId);

    Product findOneByProductNameIgnoreCaseAndUserId(String productName, long userId);

    Product findOneByProductNameIgnoreCaseAndUserIdAndProductIdNot(String productName, long userId, long productId);
}
