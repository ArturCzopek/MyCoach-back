package pl.arturczopek.mycoach.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.arturczopek.mycoach.database.entity.Product;
import pl.arturczopek.mycoach.dto.preview.ProductPreview;
import pl.arturczopek.mycoach.dto.update.ProductToUpdate;
import pl.arturczopek.mycoach.service.ProductService;

import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 10-10-2016
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

    @GetMapping("/previews")
    public List<ProductPreview> getPreviews() {
        return productService.getProductPreviews();
    }

    @GetMapping("/{id}")
    public Product getProductDetails(@PathVariable long id) {
        return productService.getProductById(id);
    }

    @PostMapping("/add")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Dodano produkt")
    public void addWeight(@RequestBody Product productToAdd) {
        productService.addProduct(productToAdd);
    }

    @PutMapping("/update")
    @ResponseStatus(value = HttpStatus.OK, reason = "Zaaktualizowano produkt")
    public void updateProduct(@RequestBody ProductToUpdate productToUpdate) {
        productService.updateProduct(productToUpdate);
    }

}
