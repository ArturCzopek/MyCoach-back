package pl.arturczopek.mycoach.web.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.arturczopek.mycoach.database.entity.Product;
import pl.arturczopek.mycoach.database.entity.dto.ProductPreview;
import pl.arturczopek.mycoach.database.repository.ProductRepository;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author arturczopek
 * @Date 10/10/16
 */

@Service
public class ProductService {

    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductPreview> getProductPreviews() {
        List<Product> products = productRepository.findAll();
        List<ProductPreview> previews = products
                .stream().map(ProductPreview::buildFromProduct)
                .collect(Collectors.toCollection(LinkedList::new));

        return previews;
    }

    public Product getProductById(long id) {
        return productRepository.findOne(id);
    }
}
