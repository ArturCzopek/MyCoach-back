package pl.arturczopek.mycoach.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
@RequestMapping("/images")
public class ImagesController {

    @Value("${myCoach.default-product-image}")
    private String noImageUrl;

    private ProductService productService;

    public ImagesController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/product/{productId}", produces = "image/jpeg")
    public void getProductPhoto(@PathVariable("productId") long productId, HttpServletResponse response) throws IOException {
        byte[] productPhoto = productService.getProductPhoto(productId);

        response = getResponseParams(response);

        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(productPhoto);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    @PostMapping("/product/upload")
    public ResponseEntity<Long> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("productId") long productId) throws IOException {
        Long updatedProductId = productService.uploadPhoto(file, productId);
        return ResponseEntity.ok(updatedProductId);
    }

    private HttpServletResponse getResponseParams(HttpServletResponse response) {
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        response.setContentType("image/jpeg");

        return response;
    }
}
