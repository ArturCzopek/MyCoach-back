package pl.arturczopek.mycoach.exception;

/**
 * @Author Artur Czopek
 * @Date 28-04-2017
 */
public abstract class InvalidPropsException extends Exception {

    private static final long serialVersionUID = -4466930211254750546L;

    InvalidPropsException(String message) {
        super(message);
    }
}
