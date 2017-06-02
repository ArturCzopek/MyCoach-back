package pl.arturczopek.mycoach.service;

import lombok.Setter;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.arturczopek.mycoach.exception.DuplicatedNameException;
import pl.arturczopek.mycoach.exception.InvalidImageExtension;
import pl.arturczopek.mycoach.exception.WrongPermissionException;
import pl.arturczopek.mycoach.model.add.NewProduct;
import pl.arturczopek.mycoach.model.database.Product;
import pl.arturczopek.mycoach.repository.PriceRepository;
import pl.arturczopek.mycoach.repository.ProductRepository;

import javax.imageio.ImageIO;
import javax.transaction.Transactional;
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

    private DictionaryService dictionaryService;

    @Autowired
    public ProductService(ProductRepository productRepository, PriceRepository priceRepository, DictionaryService dictionaryService) {
        this.productRepository = productRepository;
        this.priceRepository = priceRepository;
        this.dictionaryService = dictionaryService;
    }

    @Cacheable(value = "productPreviews", key = "#userId")
    public List<Product> getProductPreviews(long userId) {
        List<Product> products = productRepository.findValidProducts(tmpProductSign, editedProductSignSuffix + "%", userId);
        products.forEach((Product product) -> product.countAveragePrice());
        return products;
    }

    @CacheEvict(value = "productPreviews", key = "#userId")
    public void addProduct(NewProduct productToAdd, long userId) throws DuplicatedNameException {

        if (!isNewProductNameValid(productToAdd.getProductName(), userId)) {
            throw new DuplicatedNameException(dictionaryService.translate("page.prices.product.error.duplicateName.message", userId).getValue());
        }

        Product product;

        // It is possible that product has already id because of earlier uploading photo
        if (productToAdd.getProductId() > 0) {
            product = productRepository.findOne(productToAdd.getProductId());
        } else {
            product = new Product();
            product.setScreen(null);
        }

        product.setProductName(productToAdd.getProductName());
        product.setUserId(userId);

        productRepository.save(product);
    }

    public Long uploadPhoto(MultipartFile file, long productId, long userId) throws IOException, InvalidImageExtension {
        if (!("image/" + imageExtension).equalsIgnoreCase(file.getContentType())) {
            throw new InvalidImageExtension(dictionaryService.translate("page.prices.product.error.wrongImageExtension.message", userId).getValue());
        }

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
            product = getProperSpecialProductWithImage(editedProductSignSuffix + productId, product, userId);
        } else {
            product = getProperSpecialProductWithImage(tmpProductSign, product, userId);
        }

        product.setScreen(productPhotoOutputStream.toByteArray());
        product.setUserId(userId);
        productRepository.save(product);

        return product.getProductId();
    }

    @Cacheable(value = "productPhoto", key ="#userId + ' ' + #productId")
    public byte[] getProductPhoto(long productId, long userId) throws IOException, WrongPermissionException {
        Product product = productRepository.findOne(productId);

        if (product == null) {
            return new byte[]{};
        }

        if (product.getUserId() != userId) {
            throw new WrongPermissionException(dictionaryService.translate("global.error.wrongPermission.message", userId).getValue());
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

    @Caching(evict = {
        @CacheEvict(value = "productPhoto", key ="#userId + ' ' + #productId"),
        @CacheEvict(value = "productPreviews", key = "#userId")
    })
    @Transactional
    public void deleteProduct(long productId, long userId) throws WrongPermissionException {
        Product product = productRepository.findOne(productId);

        if (product.getUserId() != userId) {
            throw new WrongPermissionException(dictionaryService.translate("global.error.wrongPermission.message", userId).getValue());
        }

        priceRepository.deleteByProductIdOrderByPriceDate(product.getProductId());
        productRepository.delete(product.getProductId());
    }

    @Caching(evict = {
            @CacheEvict(value = "productPhoto", key ="#userId + ' ' + #productId"),
            @CacheEvict(value = "productPreviews", key = "#userId")
    })
    public void updateProduct(Product product, long userId) throws DuplicatedNameException, WrongPermissionException {

        if (product.getUserId() != userId) {
            throw new WrongPermissionException(dictionaryService.translate("global.error.wrongPermission.message", userId).getValue());
        }

        if (!isUpdateProductNameValid(product.getProductName(), product.getProductId(), userId)) {
            throw new DuplicatedNameException(dictionaryService.translate("page.prices.product.error.duplicateName.message", userId).getValue());
        }

        Product productToUpdate = productRepository.findOne(product.getProductId());
        productToUpdate.setProductName(product.getProductName());

        String editProductName = editedProductSignSuffix + product.getProductId();

        Product productWithUpdatedImage = productRepository.findOneByProductNameIgnoreCaseAndUserId(editProductName, userId);

        if (productWithUpdatedImage != null) {
            productToUpdate.setScreen(productWithUpdatedImage.getScreen());
            productRepository.delete(productWithUpdatedImage.getProductId());
        }

        productRepository.save(productToUpdate);
    }

    public boolean isNewProductNameValid(String productName, long userId) {
        Product duplicatedProduct = productRepository.findOneByProductNameIgnoreCaseAndUserId(productName.trim(), userId);

        return duplicatedProduct == null;
    }

    public boolean isUpdateProductNameValid(String productName, long productId, long userId) {
        Product duplicatedProduct = productRepository.findOneByProductNameIgnoreCaseAndUserIdAndProductIdNot(productName.trim(), productId, userId);

        return duplicatedProduct == null;
    }

    private Product getProperSpecialProductWithImage(String name, Product product, long userId) {
        Product tmpProduct = productRepository.findOneByProductNameIgnoreCaseAndUserId(name, userId);
        Product productToReturn;

        if (tmpProduct != null) {
            productToReturn = tmpProduct;
        } else {
            productToReturn = product;
            productToReturn.setProductName(name);
        }

        return productToReturn;
    }
}
