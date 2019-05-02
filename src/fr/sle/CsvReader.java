package fr.sle;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author slemoine
 */
public class CsvReader<T> {

    private final Converter<T> linesConverter;

    private List<T> output = new ArrayList<>();

    public CsvReader(Converter<T> lineConverter) {
        linesConverter = lineConverter;
    }

    public void read(CsvFile file) throws IOException {

        boolean hasHeader = true;

        try (BufferedReader reader = Files.newBufferedReader(file.getFilePath())) {
            Pattern pattern = file.getPattern();
            int count = 0;
            String line;
            while ((line = reader.readLine()) != null) {
                if (hasHeader) {
                    hasHeader = false;
                } else {
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        output.add(linesConverter.from(matcher.group(1), matcher.group(2)));
                    } else {
                        System.out.println(String.format("skip line %d, value: %s", count, line));
                    }
                }

                count++;
            }
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }

        System.out.println(output);
    }
}
