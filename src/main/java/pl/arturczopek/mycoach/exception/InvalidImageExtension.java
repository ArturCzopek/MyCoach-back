package pl.arturczopek.mycoach.exception;

/**
 * @Author Artur Czopek
 * @Date 28-04-2017
 */
public class InvalidImageExtension extends InvalidPropsException {

    private static final long serialVersionUID = 8496238814065529614L;

    public InvalidImageExtension(String message) {
        super(message);
    }
}
