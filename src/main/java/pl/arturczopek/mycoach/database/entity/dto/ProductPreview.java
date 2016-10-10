package pl.arturczopek.mycoach.database.entity.dto;

import lombok.Data;
import pl.arturczopek.mycoach.database.entity.Product;

/**
 * @Author arturczopek
 * @Date 10/10/16
 */

@Data
public class ProductPreview {

    private long productId;

    private String productName;

    private String screenUrl;

    public static ProductPreview buildFromProduct(Product product) {
        ProductPreview productPreview = new ProductPreview();
        productPreview.setProductId(product.getProductId());
        productPreview.setProductName(product.getProductName());
        productPreview.setScreenUrl(product.getScreenUrl());

        return productPreview;
    }
}
