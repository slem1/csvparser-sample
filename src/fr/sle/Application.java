package fr.sle;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author slemoine
 */
public class Application {

    private static List<Comic> comics = new ArrayList<>();

    public static void main(String... args) throws IOException {

        Pattern linePattern = Pattern.compile("(\\w+),(\\w+)");

        Path path = Paths.get("data.csv");

        CsvFile csvFile = new CsvFile(path, linePattern);

        ComicConverter comicConverter = new ComicConverter();

        CsvReader<Comic> comicCsvReader = new CsvReader<>(comicConverter);

        comicCsvReader.read(csvFile);

    }
}
