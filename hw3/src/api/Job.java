package api;

/**
 * Interface for DAC Job API.
 * @param <S> Solution type
 * @param <T> Task type
 */
public interface Job<S, T> {

    Task<T> runJob();

    S value();



}
