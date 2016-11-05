package pl.arturczopek.mycoach.dto.add;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 05-11-2016
 */

@Data
public class ShoppingListToAdd implements Serializable {

    private static final long serialVersionUID = -3233704193319319585L;

    @JsonProperty(required = true)
    private String place;

    private Date shoppingDate;

    private List<PriceToAdd> prices;
}
