package pl.arturczopek.mycoach.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.arturczopek.mycoach.model.database.Product;

import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 09-10-2016
 */
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

    @Override
    List<Product> findAll();

    @Query("select p from Product p where p.productName not like ?1 and p.productName not like ?2 order by p.productName desc")
    List<Product> findValidProducts(String tmpProduct, String editedProduct);

    Product findOneByProductName(String productName);
}
