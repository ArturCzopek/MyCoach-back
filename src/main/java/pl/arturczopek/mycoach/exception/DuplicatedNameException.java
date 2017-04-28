package pl.arturczopek.mycoach.exception;



public class DuplicatedNameException extends InvalidPropsException {

    private static final long serialVersionUID = -3495183549061541674L;

    public DuplicatedNameException(String message) {
        super(message);
    }
}
