package pl.arturczopek.mycoach.web.controller;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.arturczopek.mycoach.exception.InvalidImageExtensionException;
import pl.arturczopek.mycoach.exception.WrongPermissionException;
import pl.arturczopek.mycoach.model.database.User;
import pl.arturczopek.mycoach.service.ProductService;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @Author Artur Czopek
 * @Date 10-10-2016
 */

@Slf4j
@Setter
@RestController
@RequestMapping("/images")
public class ImagesController {

    private ProductService productService;

    @Autowired
    public ImagesController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/product/{productId}", produces = "image/jpeg")
    public void getProductPhoto(@PathVariable("productId") int productId, HttpServletResponse response) throws IOException, WrongPermissionException {
        byte[] productPhoto = productService.getProductPhoto(productId);

        HttpServletResponse configuredResponse = getResponseParams(response);

        ServletOutputStream responseOutputStream = configuredResponse.getOutputStream();
        responseOutputStream.write(productPhoto);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    @PostMapping("/product/upload")
    public ResponseEntity<Long> uploadImage(@RequestParam("file") MultipartFile file, @RequestParam("productId") long productId, User user) throws IOException, InvalidImageExtensionException {
        Long updatedProductId = productService.uploadPhoto(file, productId, user.getUserId());
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
