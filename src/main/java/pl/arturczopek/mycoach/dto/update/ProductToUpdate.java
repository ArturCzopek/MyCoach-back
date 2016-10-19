package pl.arturczopek.mycoach.dto.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Artur Czopek
 * @Date 16-10-2016
 */

@Data
public class ProductToUpdate implements Serializable {

    private static final long serialVersionUID = 7633582948684858402L;

    @JsonProperty(required = true)
    private long productId;

    private String productName;

    private String screenUrl;
}
