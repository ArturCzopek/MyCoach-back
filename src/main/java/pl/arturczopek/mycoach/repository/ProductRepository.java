package pl.arturczopek.mycoach.repository;

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

    List<Product> findAllByOrderByProductName();

    Product findOneByProductName(String productName);

}
