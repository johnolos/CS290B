package api;

/**
 * Interface for DAC Job API.
 * @param <S> Solution type
 */
public interface Job<S> {

    Task runJob();

    S value();

    /**
     * Abstract method setValue
     * @param value
     */
    void setValue(S value);

}
