package pl.arturczopek.mycoach.dto.preview;

import lombok.Data;
import pl.arturczopek.mycoach.database.entity.Product;

import java.io.Serializable;

/**
 * @Author Artur Czopek
 * @Date 10-10-2016
 */

@Data
public class ProductPreview implements Serializable {

    private static final long serialVersionUID = 1737456229392745343L;

    private long productId;

    private String productName;

    private byte[] screen;

    public static ProductPreview buildFromProduct(Product product) {
        ProductPreview productPreview = new ProductPreview();
        productPreview.setProductId(product.getProductId());
        productPreview.setProductName(product.getProductName());
        productPreview.setScreen(product.getScreen());

        return productPreview;
    }
}
