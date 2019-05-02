package fr.sle;

/**
 * @author slemoine
 */
public interface Converter<T> {

    T from(String... params);
}
