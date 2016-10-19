package pl.arturczopek.mycoach.dto.update;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author Artur Czopek
 * @Date 16-10-2016
 */

@Data
public class SetToUpdate implements Serializable {

    private static final long serialVersionUID = 8229817696368636432L;

    @JsonProperty(required = true)
    private long setId;

    private String setName;
}
