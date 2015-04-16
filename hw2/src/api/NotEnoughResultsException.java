package api;

/**
 * NotEnoughResultsException
 * Thrown when not enough results are presented to give an solution.
 */
public class NotEnoughResultsException extends Exception {
    public NotEnoughResultsException(String message) {
        super(message);
    }
}
