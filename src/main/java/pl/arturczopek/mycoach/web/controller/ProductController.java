package pl.arturczopek.mycoach.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.arturczopek.mycoach.model.database.Product;
import pl.arturczopek.mycoach.service.ProductService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author Artur Czopek
 * @Date 10-10-2016
 */

@Slf4j
@RestController
@RequestMapping("/product")
public class ProductController {

    @Value("${myCoach.default-product-image}")
    private String noImageUrl;

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

//    @GetMapping("/previews")
//    public List<ProductPreview> getPreviews() {
//        return productService.getProductPreviews();
//    }

    @GetMapping("/image/{productId}")
    public void getProductPhoto(@PathVariable("productId") long productId, HttpServletResponse response) throws IOException {
        byte productPhoto[] = productService.getProductPhoto(productId);

        if (productPhoto == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(productPhoto);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    @PostMapping("/image/upload")
    public ResponseEntity<Long> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("productId") long productId) throws IOException {

        Long updatedProductId = productService.uploadPhoto(file, productId);

        return ResponseEntity.ok(updatedProductId);
    }
    
    @PostMapping("/add")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Dodano produkt")
    public void addProduct(@RequestBody Product productToAdd) {
        productService.addProduct(productToAdd);
    }

//    @PutMapping("/update")
//    @ResponseStatus(value = HttpStatus.OK, reason = "Zaaktualizowano produkt")
//    public void updateProduct(@RequestBody ProductToUpdate productToUpdate) {
//        productService.updateProduct(productToUpdate);
//    }

}
