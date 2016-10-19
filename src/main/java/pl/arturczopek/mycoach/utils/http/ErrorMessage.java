package pl.arturczopek.mycoach.utils.http;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author Artur Czopek
 * @Date 19-10-2016
 */

@Data
@AllArgsConstructor
public class ErrorMessage {

    private String message;
    private String url;
    private String exception;
}
