package fr.sle.csv.read;

import fr.sle.converter.Converter;
import fr.sle.csv.descriptor.CsvDescriptor;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author slemoine
 */
public class CsvReader<T, E extends Enum<E> & CsvDescriptor> implements Runnable {

    private AtomicBoolean inputFlag = new AtomicBoolean(true);

    private final CsvFile file;

    private final Class<E> descriptorClass;

    private final BlockingQueue<T> output;

    private final Converter<T, E> converter;

    public CsvReader(CsvFile file, Class<E> descriptorClass, BlockingQueue<T> output, Converter<T, E> converter) {
        this.file = file;
        this.descriptorClass = descriptorClass;
        this.output = output;
        this.converter = converter;
    }

    public AtomicBoolean getInputFlag() {
        return inputFlag;
    }


    public void read() throws IOException {

        Objects.requireNonNull(file);
        Objects.requireNonNull(descriptorClass);
        Objects.requireNonNull(output);

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

            inputFlag.set(false);
        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public void run() {
        try {
            read();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
