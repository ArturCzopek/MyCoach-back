package pl.arturczopek.mycoach.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.arturczopek.mycoach.database.entity.Product;
import pl.arturczopek.mycoach.database.repository.ProductRepository;
import pl.arturczopek.mycoach.dto.preview.ProductPreview;
import pl.arturczopek.mycoach.dto.update.ProductToUpdate;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author Artur Czopek
 * @Date 10-10-2016
 */

@Service
public class ProductService {

    @Value("${myCoach.default-product-photo}")
    private String noPhotoUrl;

    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductPreview> getProductPreviews() {
        List<Product> products = productRepository.findAllByOrderByProductName();

        return products
                .stream().map(ProductPreview::buildFromProduct)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public void addProduct(Product productToAdd) {
        Product product = new Product();
        product.setProductName(productToAdd.getProductName());
        if (StringUtils.isEmpty(productToAdd.getScreenUrl())) {
            product.setScreenUrl(noPhotoUrl);
        } else {
            product.setScreenUrl(productToAdd.getScreenUrl());
        }
        productRepository.save(product);
    }

    public void updateProduct(ProductToUpdate productToUpdate) {
        Product product = productRepository.findOne(productToUpdate.getProductId());

        if (!StringUtils.isEmpty(productToUpdate.getProductName())) {
            product.setProductName(productToUpdate.getProductName());
        }

        if (!StringUtils.isEmpty(productToUpdate.getScreenUrl())) {
            product.setScreenUrl(productToUpdate.getScreenUrl());
        }

        productRepository.save(product);
    }
}
