package pl.arturczopek.mycoach.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.arturczopek.mycoach.model.database.Product;
import pl.arturczopek.mycoach.repository.ProductRepository;

import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 08-04-2017
 */
@Service
@Slf4j
public class PhotosWatcher {

    private ProductRepository productRepository;

    @Value("$myCoach.tmp-product-sign")
    private String tmpProductSign;

    @Autowired
    public PhotosWatcher(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Scheduled(cron = "0 4 * * *")
    public void cleanDatabaseFromTmpImages() {
        List<Product> products = productRepository.findAll();
        log.info("Started cleaning database from temporary images");

        for (Product product : products) {
            if (product.getProductName().equals(tmpProductSign)) {
                productRepository.delete(product.getProductId());
                log.info("Removed tmp photo from database");
            }
        }
    }
}
