package pl.arturczopek.mycoach.model.add;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @Author Artur Czopek
 * @Date 14-04-2017
 */

@Data
public class NewProduct {

    private long productId;

    @JsonProperty(required = true)
    private String productName;

    private byte[] image;
}
