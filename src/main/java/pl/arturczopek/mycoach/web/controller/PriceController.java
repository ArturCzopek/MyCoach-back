package pl.arturczopek.mycoach.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pl.arturczopek.mycoach.database.entity.Price;
import pl.arturczopek.mycoach.dto.add.PriceToAdd;
import pl.arturczopek.mycoach.dto.add.ShoppingListToAdd;
import pl.arturczopek.mycoach.dto.update.PriceToUpdate;
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
    public void addPrice(@RequestBody PriceToAdd priceToAdd) {
        priceService.addPrice(priceToAdd);
    }

    @PostMapping("/shopping")
    @ResponseStatus(value = HttpStatus.CREATED, reason = "Dodano liste zakupow")
    public void addShoppingPrices(@RequestBody ShoppingListToAdd shoppingList) {
        priceService.addShoppingList(shoppingList);
    }

    @PutMapping("/update")
    @ResponseStatus(value = HttpStatus.OK, reason = "Zaaktualizowano cenę")
    public void updateProduct(@RequestBody PriceToUpdate priceToUpdate) {
        priceService.updatePrice(priceToUpdate);
    }

}
