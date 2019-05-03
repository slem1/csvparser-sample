package fr.sle;

import fr.sle.converter.ComicConverter;
import fr.sle.csv.descriptor.ComicCsvDescriptor;
import fr.sle.csv.read.CsvFile;
import fr.sle.csv.read.CsvReader;
import fr.sle.model.Comic;
import fr.sle.writer.DataSource;
import fr.sle.writer.DataWriter;

import java.io.IOException;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

/**
 * @author slemoine
 */
public class Application {

    private static List<Comic> comics = new ArrayList<>();

    public static void main(String... args) throws IOException {

        Pattern linePattern = ComicCsvDescriptor.getPattern();

        Path path = Paths.get("data.csv");

        CsvFile csvFile = new CsvFile(path, linePattern);

        ComicConverter comicConverter = new ComicConverter();

        CsvReader<Comic, ComicCsvDescriptor> reader = new CsvReader<>(comicConverter);

        AtomicBoolean inputFlag = reader.getInputFlag();

        List<Comic> comics = reader.read(csvFile, ComicCsvDescriptor.class);

        BlockingQueue<Comic> bq = new LinkedBlockingQueue<>(comics);

        DataWriter dw = new DataWriter(bq, new DataSource(), inputFlag);

        Thread th1 = new Thread(dw);
        Thread th2 = new Thread(dw);
        Thread th3 = new Thread(dw);

        th1.start();
        th2.start();
        th3.start();

        try {
            th1.join();
            th2.join();
            th3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
