package pl.arturczopek.mycoach.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import pl.arturczopek.mycoach.database.entity.Product;
import pl.arturczopek.mycoach.database.entity.dto.ProductPreview;
import pl.arturczopek.mycoach.web.service.ProductService;

import java.util.List;

/**
 * @Author arturczopek
 * @Date 10/10/16
 */

@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {


    private ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @RequestMapping(value = "/previews", method = RequestMethod.GET)
    public List<ProductPreview> getPreviews() {
        return productService.getProductPreviews();
    }


    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Product getProductDetails(@PathVariable long id) {
        return productService.getProductById(id);
    }
}
