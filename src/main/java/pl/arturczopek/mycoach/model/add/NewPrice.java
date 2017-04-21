package pl.arturczopek.mycoach.model.add;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

/**
 * @Author Artur Czopek
 * @Date 05-11-2016
 */

@Data
@AllArgsConstructor
public class NewPrice implements Serializable {

    private static final long serialVersionUID = -2267814709244502404L;

    @JsonProperty(required = true)
    private long productId;

    @JsonProperty(required = true)
    private float value;

    @JsonProperty(required = true)
    private float quantity;

    private String place;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date priceDate;
}
