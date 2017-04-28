package pl.arturczopek.mycoach.exception;

/**
 * @Author Artur Czopek
 * @Date 28-04-2017
 */
public class InvalidDateException extends InvalidPropsException {

    private static final long serialVersionUID = -4438152382871146501L;

    public InvalidDateException(String message) {
        super(message);
    }
}
