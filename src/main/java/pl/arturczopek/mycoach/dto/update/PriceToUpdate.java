package pl.arturczopek.mycoach.dto.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author Artur Czopek
 * @Date 05-11-2016
 */

@Data
public class PriceToUpdate implements Serializable {

    private static final long serialVersionUID = 646998775096674956L;

    @JsonProperty(required = true)
    private long priceId;

    private Float price;

    private String place;

    private Float quantity;

    private Date priceDate;
}
