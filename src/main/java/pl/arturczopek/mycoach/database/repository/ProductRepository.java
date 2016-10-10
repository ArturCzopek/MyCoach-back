package pl.arturczopek.mycoach.database.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import pl.arturczopek.mycoach.database.entity.Product;

import java.util.List;

/**
 * @Author arturczopek
 * @Date 10/9/16
 */
public interface ProductRepository extends PagingAndSortingRepository<Product, Long> {

    @Override
    List<Product> findAll();

    List<Product> findAllByOrderByProductName(Pageable pageable);
}
