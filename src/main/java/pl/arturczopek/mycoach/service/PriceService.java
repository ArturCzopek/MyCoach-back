package pl.arturczopek.mycoach.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import pl.arturczopek.mycoach.exception.WrongPermissionException;
import pl.arturczopek.mycoach.model.add.NewPrice;
import pl.arturczopek.mycoach.model.add.ShoppingList;
import pl.arturczopek.mycoach.model.database.Price;
import pl.arturczopek.mycoach.model.database.Product;
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
    private DictionaryService dictionaryService;
    private DateService dateService;

    @Autowired
    public PriceService(PriceRepository priceRepository, ProductRepository productRepository,
                        DictionaryService dictionaryService, DateService dateService) {
        this.priceRepository = priceRepository;
        this.productRepository = productRepository;
        this.dictionaryService = dictionaryService;
        this.dateService = dateService;
    }

    @Cacheable(value = "prices", key = "#productId")
    public List<Price> getPricesByProductId(long productId, long userId) throws WrongPermissionException {
        Product product = productRepository.findOne(productId);

        if (product.getUserId() != userId) {
            throw new WrongPermissionException(dictionaryService.translate("global.error.wrongPermission.message", userId).getValue());
        }

        return product.getPrices();
    }

    @CacheEvict(value = "prices", key = "#newPrice.productId")
    public void addPrice(NewPrice newPrice, long userId) throws WrongPermissionException {

        Product product = productRepository.findOne(newPrice.getProductId());

        if (product.getUserId() != userId) {
            throw new WrongPermissionException(dictionaryService.translate("global.error.wrongPermission.message", userId).getValue());
        }

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

    @CacheEvict(value = "prices", allEntries = true)
    public void addShoppingList(ShoppingList shoppingList, long userId) throws WrongPermissionException {

        boolean arePricesValid = !shoppingList.getPrices().stream().anyMatch((NewPrice price) -> {
            Product product = productRepository.findOne(price.getProductId());
            if (product.getUserId() != userId) {
                return true;
            }

            return false;
        });

        if (!arePricesValid) {
            throw new WrongPermissionException(dictionaryService.translate("global.error.wrongPermission.message", userId).getValue());
        }

        for (NewPrice position : shoppingList.getPrices()) {
            NewPrice price = new NewPrice(position.getProductId(), position.getValue(), position.getQuantity(),
                    shoppingList.getPlace(), shoppingList.getShoppingDate());
            this.addPrice(price, userId);
        }
    }

    @CacheEvict(value = "prices", allEntries = true)
    public void deletePrices(List<Price> prices, long userId) throws WrongPermissionException {

        if (!arePricesValid(prices, userId)) {
            throw new WrongPermissionException(dictionaryService.translate("global.error.wrongPermission.message", userId).getValue());
        }

        prices.forEach((Price price) -> priceRepository.delete(price.getPriceId()));
    }

    @CacheEvict(value = "prices", allEntries = true)
    public void updatePrices(List<Price> prices, long userId) throws WrongPermissionException {

        if (!arePricesValid(prices, userId)) {
            throw new WrongPermissionException(dictionaryService.translate("global.error.wrongPermission.message", userId).getValue());
        }

        prices.forEach((Price price) -> priceRepository.save(price));
    }

    private boolean arePricesValid(List<Price> prices, long userId) {
        return !prices.stream().anyMatch((Price price) -> {
            Product product = productRepository.findOne(price.getProductId());
            if (product.getUserId() != userId) {
                return true;
            }

            return false;
        });
    }
}
