package fr.sle.converter;

import java.util.Map;

/**
 * @author slemoine
 */
public interface Converter<T, E extends Enum<E>> {

    T from(Map<E, String> params);
}
