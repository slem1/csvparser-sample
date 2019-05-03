package fr.sle.csv.read;

import fr.sle.converter.Converter;
import fr.sle.csv.descriptor.CsvDescriptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author slemoine
 */
public class CsvReader<T, E extends Enum<E> & CsvDescriptor> {

    private final Converter<T, E> converter;

    private List<T> output = new ArrayList<>();

    public CsvReader(Converter<T, E> converter) {
        this.converter = converter;
    }

    public void read(CsvFile file, Class<E> descriptorClass) throws IOException {

        Objects.requireNonNull(file);
        Objects.requireNonNull(descriptorClass);

        boolean hasHeader = true;

        try (BufferedReader reader = Files.newBufferedReader(file.getFilePath())) {
            Pattern pattern = file.getPattern();
            int count = 0;
            String line;

            while ((line = reader.readLine()) != null) {
                Map<E, String> objectMap = new HashMap<>();
                if (hasHeader) {
                    hasHeader = false;
                } else {
                    Matcher matcher = pattern.matcher(line);
                    if (matcher.find()) {
                        for (E e : descriptorClass.getEnumConstants()) {
                            objectMap.put(e, matcher.group(e.name()));
                        }

                        output.add(converter.from(objectMap));
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
