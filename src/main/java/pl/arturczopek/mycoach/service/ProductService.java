package pl.arturczopek.mycoach.service;

import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import pl.arturczopek.mycoach.model.database.Product;
import pl.arturczopek.mycoach.repository.ProductRepository;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Author Artur Czopek
 * @Date 10-10-2016
 */

@Service
public class ProductService {

    @Value("${myCoach.default-product-image}")
    private String noImageUrl;

    @Value("${myCoach.image-size}")
    private int imageSize;

    @Value("$myCoach.tmp-product-sign")
    private String tmpProductSign;

    @Value("$myCoach.edited-product-sign-suffix")
    private String editedProductSignSuffix;

    @Value("$myCoach.image-extension")
    private String imageExtension;

    private ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

//    public List<ProductPreview> getProductPreviews() {
//        List<Product> products = productRepository.findAllByOrderByProductName();
//
//        return products
//                .stream().map(ProductPreview::buildFromProduct)
//                .collect(Collectors.toCollection(LinkedList::new));
//    }

    public void addProduct(Product productToAdd) {
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

//    public void updateProduct(ProductToUpdate productToUpdate) {
//        Product product = productRepository.findOne(productToUpdate.getProductId());
//
//        if (!StringUtils.isEmpty(product.getProductName())) {
//            product.setProductName(product.getProductName());
//        }
//
//        String possibleEditProductName = editedProductSignSuffix + productToUpdate.getProductId();
//        Product possibleEditProduct = productRepository.findOneByProductName(possibleEditProductName);
//
//        if (possibleEditProduct != null) {
//            product.setScreen(possibleEditProduct.getScreen());
//            productRepository.delete(possibleEditProduct.getProductId());
//        }
//
//        productRepository.save(product);
//    }

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
            String editProductName = editedProductSignSuffix + productId;
            Product inEditModeProduct = productRepository.findOneByProductName(editProductName);

            if (inEditModeProduct != null) {
                product = inEditModeProduct;
            } else {
                product.setProductName(editProductName);
            }
        } else {
            product.setProductName(tmpProductSign);
        }

        productRepository.save(product);

        product.setScreen(productPhotoOutputStream.toByteArray());
        productRepository.save(product);

        return product.getProductId();
    }

    public byte[] getProductPhoto(long productId) throws IOException {
        Product product = productRepository.findOne(productId);

        if (product == null) {
            return null;
        }

        if (product.getScreen() == null) {
            ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();

            BufferedImage image = ImageIO.read(getClass().getResource(noImageUrl));
            ImageIO.write(image, imageExtension, jpegOutputStream);
            return jpegOutputStream.toByteArray();
        }

        return product.getScreen();
    }
}
