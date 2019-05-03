package fr.sle.converter;

import fr.sle.csv.descriptor.ComicCsvDescriptor;
import fr.sle.model.Comic;

import java.util.Map;

/**
 * @author slemoine
 */
public class ComicConverter implements Converter<Comic, ComicCsvDescriptor> {

    public Comic from(Map<ComicCsvDescriptor, String> objectMap) {
        Integer p = Integer.parseInt(objectMap.get(ComicCsvDescriptor.PRICE));
        return new Comic(objectMap.get(ComicCsvDescriptor.TITLE), p);
    }
}
