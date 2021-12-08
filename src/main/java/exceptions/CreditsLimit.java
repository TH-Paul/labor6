package exceptions;

public class CreditsLimit extends RuntimeException{
    public CreditsLimit(String message) {
        super(message);
    }
}
