package pl.arturczopek.mycoach.model.add;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;
import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 05-11-2016
 */

@Data
public class ShoppingList implements Serializable {

    private static final long serialVersionUID = -3233704193319319585L;

    @JsonProperty(required = true)
    private String place;

    @JsonProperty(required = true)
    private List<NewPrice> prices;

    private Date shoppingDate;
}
