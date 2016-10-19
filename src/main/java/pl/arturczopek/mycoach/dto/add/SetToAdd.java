package pl.arturczopek.mycoach.dto.add;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author Artur Czopek
 * @Date 16-10-2016
 */

@Data
public class SetToAdd implements Serializable{

    private static final long serialVersionUID = -1614366596244113663L;

    @JsonProperty(required = true)
    private String setName;

    @JsonProperty(required = true)
    private List<String> exercises;
}
