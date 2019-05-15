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
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Pattern;

/**
 * @author slemoine
 */
public class Application {

    public static void main(String... args) throws IOException, InterruptedException {

        Pattern linePattern = ComicCsvDescriptor.getPattern();

        Path path = Paths.get("data.csv");

        CsvFile csvFile = new CsvFile(path, linePattern);

        ComicConverter comicConverter = new ComicConverter();

        BlockingQueue<Comic> bq = new LinkedBlockingQueue<>();

        CsvReader<Comic, ComicCsvDescriptor> reader = new CsvReader<>(csvFile, ComicCsvDescriptor.class, bq, comicConverter);

        AtomicBoolean inputFlag = reader.getInputFlag();

        List<Callable<Object>> tasks = new ArrayList<>();

        for (int i = 0; i < 8; i++) {
            tasks.add(Executors.callable(new DataWriter<>(bq, inputFlag, new ComicDao(DataSourceSingleton.getInstance()))));
        }

        ExecutorService executorService = Executors.newFixedThreadPool(8);

        long start = System.nanoTime();

        reader.read();

        List<Future<Object>> futures = executorService.invokeAll(tasks);

        for(Future<?> fs : futures){
            try {
                fs.get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        long end = System.nanoTime();

        long result = end - start;

        System.out.println("Elasped time : " + result);

        executorService.shutdown();

    }

}
