package pl.arturczopek.mycoach.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.arturczopek.mycoach.model.add.NewPrice;
import pl.arturczopek.mycoach.model.add.ShoppingList;
import pl.arturczopek.mycoach.model.database.Price;
import pl.arturczopek.mycoach.service.PriceService;

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

    @Autowired
    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping("/{id}")
    public List<Price> getPricesForProduct(@PathVariable long id) {
        return priceService.getPricesForProduct(id);
    }

    @PostMapping("/add")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Dodano cenę")
    public void addPrice(@RequestBody NewPrice newPrice) {
        priceService.addPrice(newPrice);
    }

    @PostMapping("/shopping")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Dodano liste zakupow")
    public void addShoppingPrices(@RequestBody ShoppingList shoppingList) {
        priceService.addShoppingList(shoppingList);
    }

//    @PutMapping("/update")
//    @ResponseStatus(value = HttpStatus.OK, reason = "Zaaktualizowano cenę")
//    public void updateProduct(@RequestBody PriceToUpdate priceToUpdate) {
//        priceService.updatePrice(priceToUpdate);
//    }

}
