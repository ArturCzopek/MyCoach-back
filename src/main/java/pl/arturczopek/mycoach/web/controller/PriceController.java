package pl.arturczopek.mycoach.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.arturczopek.mycoach.exception.DuplicatedNameException;
import pl.arturczopek.mycoach.exception.WrongPermissionException;
import pl.arturczopek.mycoach.model.add.NewPrice;
import pl.arturczopek.mycoach.model.add.NewProduct;
import pl.arturczopek.mycoach.model.add.ShoppingList;
import pl.arturczopek.mycoach.model.database.Price;
import pl.arturczopek.mycoach.model.database.Product;
import pl.arturczopek.mycoach.model.database.User;
import pl.arturczopek.mycoach.service.PriceService;
import pl.arturczopek.mycoach.service.ProductService;

import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 05-11-2016
 */

@Slf4j
@RestController
@RequestMapping("/price")
public class PriceController {

    private PriceService priceService;
    private ProductService productService;

    @Autowired
    public PriceController(PriceService priceService, ProductService productService) {
        this.priceService = priceService;
        this.productService = productService;
    }

    @GetMapping("/product/previews")
    public List<Product> getProductPreviews(User user) {
        return productService.getProductPreviews(user.getUserId());
    }

    @GetMapping("/{productId}")
    public List<Price> getPricesByProductId(@PathVariable long productId, User user) throws WrongPermissionException {
        return priceService.getPricesByProductId(productId, user.getUserId());
    }

    @PostMapping("product/add")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Added product")
    public void addProduct(@RequestBody NewProduct product, User user) throws DuplicatedNameException {
        productService.addProduct(product, user.getUserId());
    }

    @PostMapping("/add")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Added price")
    public void addPrice(@RequestBody NewPrice newPrice, User user) throws WrongPermissionException {
        priceService.addPrice(newPrice, user.getUserId());
    }

    @PostMapping("/shopping/add")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Added shopping list")
    public void addShoppingPrices(@RequestBody ShoppingList shoppingList, User user) throws WrongPermissionException {
        priceService.addShoppingList(shoppingList, user.getUserId());
    }

    @DeleteMapping("/delete")
    @ResponseStatus(value = HttpStatus.OK, reason = "Removed prices")
    public void deletePrices(@RequestBody List<Price> prices, User user) throws WrongPermissionException {
        priceService.deletePrices(prices, user.getUserId());
    }

    @DeleteMapping("product/delete")
    @ResponseStatus(value = HttpStatus.OK, reason = "Removed product")
    public void deleteProduct(@RequestBody Product product, User user) throws WrongPermissionException {
        productService.deleteProduct(product.getProductId(), user.getUserId());
    }

    @PutMapping("/update")
    @ResponseStatus(value = HttpStatus.OK, reason = "Updated prices")
    public void updatePrices(@RequestBody List<Price> prices, User user) throws WrongPermissionException {
        priceService.updatePrices(prices, user.getUserId());
    }

    @PutMapping("/product/update")
    @ResponseStatus(value = HttpStatus.OK, reason = "Updated product")
    public void updateProduct(@RequestBody Product product, User user) throws DuplicatedNameException, WrongPermissionException {
        productService.updateProduct(product, user.getUserId());
    }
}
