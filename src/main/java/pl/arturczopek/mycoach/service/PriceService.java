package pl.arturczopek.mycoach.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import pl.arturczopek.mycoach.database.entity.Price;
import pl.arturczopek.mycoach.database.entity.Product;
import pl.arturczopek.mycoach.database.repository.PriceRepository;
import pl.arturczopek.mycoach.database.repository.ProductRepository;
import pl.arturczopek.mycoach.dto.add.PriceToAdd;
import pl.arturczopek.mycoach.dto.add.ShoppingListToAdd;
import pl.arturczopek.mycoach.dto.update.PriceToUpdate;

import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 05-11-2016
 */

@Service
public class PriceService {

    private PriceRepository priceRepository;
    private ProductRepository productRepository;
    private DateService dateService;

    @Autowired
    public PriceService(PriceRepository priceRepository, ProductRepository productRepository, DateService dateService) {
        this.priceRepository = priceRepository;
        this.productRepository = productRepository;
        this.dateService = dateService;
    }

    public List<Price> getPricesForProduct(long productId) {
        return productRepository.findOne(productId).getPrices();
    }

    public void addPrice(PriceToAdd priceToAdd) {
        Product product = productRepository.findOne(priceToAdd.getProductId());

        Price price = new Price();
        price.setProduct(product);
        price.setPrice(priceToAdd.getPrice());
        price.setPlace(priceToAdd.getPlace());

        if (priceToAdd.getPriceDate() == null) {
            price.setPriceDate(dateService.getCurrentDate());
        }

        if (priceToAdd.getQuantity() == null) {
            price.setQuantity(1);
        } else {
            price.setQuantity(priceToAdd.getQuantity());
        }

        priceRepository.save(price);
    }

    public void addShoppingList(ShoppingListToAdd shoppingList) {

        for (PriceToAdd shoppingPrice : shoppingList.getPrices()) {
            shoppingPrice.setPriceDate(shoppingList.getShoppingDate());
            shoppingPrice.setPlace(shoppingList.getPlace());
            addPrice(shoppingPrice);
        }
    }

    public void updatePrice(PriceToUpdate priceToUpdate) {
        Price price = priceRepository.findOne(priceToUpdate.getPriceId());

        if (priceToUpdate.getPriceDate() != null) {
            price.setPriceDate(priceToUpdate.getPriceDate());
        }

        if (priceToUpdate.getPrice() != null) {
            price.setPrice(priceToUpdate.getPrice());
        }

        if (!StringUtils.isEmpty(priceToUpdate.getPlace())) {
            price.setPlace(priceToUpdate.getPlace());
        }

        if (priceToUpdate.getQuantity() != null) {
            price.setQuantity(priceToUpdate.getQuantity());
        }

        priceRepository.save(price);
    }
}
