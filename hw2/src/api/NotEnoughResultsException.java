package api;

/**
 * NotEnoughResultsException
 * Thrown when not enough results are presented to give an solution.
 */
public class NotEnoughResultsException extends Exception {

    /**
     * Constructor for NotEnoughResultException
     * @param message String exception message
     */
    public NotEnoughResultsException(String message) {
        super(message);
    }
}
