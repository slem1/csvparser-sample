package fr.sle;

/**
 * @author slemoine
 */
public class ComicConverter implements Converter<Comic> {

    public Comic from(String... params) {
        Integer p = Integer.parseInt(params[1]);
        return new Comic(params[0], p);
    }
}
