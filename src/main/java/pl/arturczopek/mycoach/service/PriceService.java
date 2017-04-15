package pl.arturczopek.mycoach.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.arturczopek.mycoach.model.add.NewPrice;
import pl.arturczopek.mycoach.model.add.ShoppingList;
import pl.arturczopek.mycoach.model.database.Price;
import pl.arturczopek.mycoach.repository.PriceRepository;
import pl.arturczopek.mycoach.repository.ProductRepository;

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

    public void addPrice(NewPrice newPrice) {

        Price price = new Price();
        price.setProductId(newPrice.getProductId());
        price.setValue(newPrice.getValue());
        price.setPlace(newPrice.getPlace());

        if (newPrice.getPriceDate() == null) {
            price.setPriceDate(dateService.getCurrentDate());
        }

        priceRepository.save(price);
    }

    public void addShoppingList(ShoppingList shoppingList) {

        for (NewPrice shoppingPrice : shoppingList.getPrices()) {
            shoppingPrice.setPriceDate(shoppingList.getShoppingDate());
            shoppingPrice.setPlace(shoppingList.getPlace());
            addPrice(shoppingPrice);
        }
    }

//    public void updatePrice(PriceToUpdate priceToUpdate) {
//        Price price = priceRepository.findOne(priceToUpdate.getPriceId());
//
//        if (priceToUpdate.getPriceDate() != null) {
//            price.setPriceDate(priceToUpdate.getPriceDate());
//        }
//
//        if (priceToUpdate.getValue() != null) {
//            price.setValue(priceToUpdate.getValue());
//        }
//
//        if (!StringUtils.isEmpty(priceToUpdate.getPlace())) {
//            price.setPlace(priceToUpdate.getPlace());
//        }
//
//        if (priceToUpdate.getQuantity() != null) {
//            price.setQuantity(priceToUpdate.getQuantity());
//        }
//
//        priceRepository.save(price);
//    }
}
