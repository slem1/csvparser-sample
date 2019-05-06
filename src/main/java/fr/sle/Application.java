package fr.sle;

import fr.sle.converter.ComicConverter;
import fr.sle.csv.descriptor.ComicCsvDescriptor;
import fr.sle.csv.read.CsvFile;
import fr.sle.csv.read.CsvReader;
import fr.sle.dao.ComicDao;
import fr.sle.model.Comic;
import fr.sle.write.DataWriter;

import java.io.IOException;
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

    public static void main(String... args) throws IOException {

        Pattern linePattern = ComicCsvDescriptor.getPattern();

        Path path = Paths.get("data.csv");

        CsvFile csvFile = new CsvFile(path, linePattern);

        ComicConverter comicConverter = new ComicConverter();

        BlockingQueue<Comic> bq = new LinkedBlockingQueue<>();

        CsvReader<Comic, ComicCsvDescriptor> reader = new CsvReader<>(csvFile, ComicCsvDescriptor.class, bq, comicConverter);

        AtomicBoolean inputFlag = reader.getInputFlag();

        long start = System.nanoTime();

        DataWriter<Comic> dw = new DataWriter<>(bq, inputFlag, new ComicDao(DataSourceSingleton.getInstance()));

        Thread th0 = new Thread(reader);
        Thread th1 = new Thread(dw);
        Thread th2 = new Thread(dw);
        Thread th3 = new Thread(dw);

        th1.start();
        th2.start();
        th3.start();
        th0.start();


        try {
            th0.join();
            th1.join();
            th2.join();
            th3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long end = System.nanoTime();

        long result = end - start;

        System.out.println("Elasped time : " + result);


    }

}
