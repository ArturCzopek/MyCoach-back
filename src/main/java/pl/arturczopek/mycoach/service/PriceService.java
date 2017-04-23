package pl.arturczopek.mycoach.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.arturczopek.mycoach.model.add.NewPrice;
import pl.arturczopek.mycoach.model.add.ShoppingList;
import pl.arturczopek.mycoach.model.database.Price;
import pl.arturczopek.mycoach.repository.PriceRepository;

import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 05-11-2016
 */

@Service
public class PriceService {

    private PriceRepository priceRepository;
    private DateService dateService;

    @Autowired
    public PriceService(PriceRepository priceRepository,  DateService dateService) {
        this.priceRepository = priceRepository;
        this.dateService = dateService;
    }

    public List<Price> getPricesByProductId(long productId) {
        return priceRepository.findByProductIdOrderByPriceDate(productId);
    }

    public void addPrice(NewPrice newPrice) {

        Price price = new Price();
        price.setProductId(newPrice.getProductId());
        price.setValue(newPrice.getValue());
        price.setPlace(newPrice.getPlace());
        price.setQuantity(newPrice.getQuantity());

        if (newPrice.getPriceDate() == null) {
            price.setPriceDate(dateService.getCurrentDate());
        } else {
            price.setPriceDate(newPrice.getPriceDate());
        }

        priceRepository.save(price);
    }

    public void addShoppingList(ShoppingList shoppingList) {

        shoppingList.getPrices().stream()
                .map(position -> new NewPrice(position.getProductId(), position.getValue(), position.getQuantity(),
                        shoppingList.getPlace(), shoppingList.getShoppingDate()))
                .forEach((NewPrice price) -> this.addPrice(price));
    }

    public void deletePrices(List<Price> prices) {
        prices.forEach((Price price) -> priceRepository.delete(price.getPriceId()));
    }

    public void updatePrices(List<Price> prices) {
        prices.forEach((Price price) -> priceRepository.save(price));
    }
}
