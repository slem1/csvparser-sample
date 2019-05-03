package fr.sle.csv.descriptor;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 *
 * Describe the CSV columns. The order of enum instance is important.
 *
 * @author slemoine
 */
public enum ComicCsvDescriptor implements CsvDescriptor{

    TITLE,

    PRICE;

    public static Pattern getPattern() {

        List<String> tokens = new ArrayList<>();

        for (ComicCsvDescriptor v : values()) {
            tokens.add(String.format("(?<%s>\\w*)", v.name()));
        }

        String regex = String.join(",", tokens);

        return Pattern.compile(regex);

    }

}
