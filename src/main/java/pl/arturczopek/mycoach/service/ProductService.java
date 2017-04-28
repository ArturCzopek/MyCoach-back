package pl.arturczopek.mycoach.service;

import lombok.Setter;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.arturczopek.mycoach.model.add.NewProduct;
import pl.arturczopek.mycoach.model.database.Product;
import pl.arturczopek.mycoach.repository.PriceRepository;
import pl.arturczopek.mycoach.repository.ProductRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 10-10-2016
 */

@Service
@Setter
@ConfigurationProperties(prefix = "my-coach.images")
public class ProductService {

    private String defaultProductImage;

    private int imageSize;

    private String tmpProductSign;

    private String editedProductSignSuffix;

    private String imageExtension;

    private ProductRepository productRepository;

    private PriceRepository priceRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, PriceRepository priceRepository) {
        this.productRepository = productRepository;
        this.priceRepository = priceRepository;
    }

    public List<Product> getProductPreviews() {
        List<Product> products = productRepository.findValidProducts(tmpProductSign, editedProductSignSuffix + "%");
        products.forEach((Product product) -> product.countAveragePrice());
        return products;
    }

    public void addProduct(NewProduct productToAdd) {
        Product product;

        // It is possible that product has already id because of earlier uploading photo
        if (productToAdd.getProductId() > 0) {
            product = productRepository.findOne(productToAdd.getProductId());
        } else {
            product = new Product();
            product.setScreen(null);
        }

        product.setProductName(productToAdd.getProductName());

        productRepository.save(product);
    }

    public Long uploadPhoto(MultipartFile file, long productId) throws IOException {
        final ByteArrayOutputStream productPhotoOutputStream = new ByteArrayOutputStream();

        Thumbnails.of(new ByteArrayInputStream(file.getBytes()))
                .forceSize(imageSize, imageSize)
                .outputFormat(imageExtension)
                .toOutputStream(productPhotoOutputStream);

        Product product = new Product();

        // This is tmp storage for image
        // Redundant files will be removed by PhotoWatcher
        // Copy file will be when user want to really update photo

        if (productId > 0) {
            product = getProperSpecialProductWithImage(editedProductSignSuffix + productId, product);
        } else {
            product = getProperSpecialProductWithImage(tmpProductSign, product);
        }

        product.setScreen(productPhotoOutputStream.toByteArray());
        productRepository.save(product);

        return product.getProductId();
    }

    private Product getProperSpecialProductWithImage(String name, Product product) {
        Product tmpProduct = productRepository.findOneByProductName(name);
        Product productToReturn;

        if (tmpProduct != null) {
            productToReturn = tmpProduct;
        } else {
            productToReturn = product;
            productToReturn.setProductName(name);
        }

        return productToReturn;
    }

    public byte[] getProductPhoto(long productId) throws IOException {
        Product product = productRepository.findOne(productId);

        if (product == null) {
            return new byte[]{};
        }

        if (product.getScreen() == null) {
            BufferedImage image = ImageIO.read(this.getClass().getResource(defaultProductImage));

            ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
            jpegOutputStream.reset();

            ImageIO.write(image, imageExtension, jpegOutputStream);
            jpegOutputStream.flush();
            jpegOutputStream.close();
            return jpegOutputStream.toByteArray();
        }

        return product.getScreen();
    }

    public void deleteProduct(Product product) {
        priceRepository.deleteByProductIdOrderByPriceDate(product.getProductId());
        productRepository.delete(product.getProductId());
    }

    public void updateProduct(Product product) {
        Product productToUpdate = productRepository.findOne(product.getProductId());
        productToUpdate.setProductName(product.getProductName());

        String editProductName = editedProductSignSuffix + product.getProductId();

        Product productWithUpdatedImage = productRepository.findOneByProductName(editProductName);

        if (productWithUpdatedImage != null) {
            productToUpdate.setScreen(productWithUpdatedImage.getScreen());
            productRepository.delete(productWithUpdatedImage.getProductId());
        }

        productRepository.save(productToUpdate);
    }
}
