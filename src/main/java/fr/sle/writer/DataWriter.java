package fr.sle.writer;

import fr.sle.model.Comic;

import java.util.concurrent.BlockingQueue;

/**
 * @author slemoine
 */
public class DataWriter implements Runnable {

    private final BlockingQueue<Comic> comics;

    private final DataSource ds;

    public DataWriter(BlockingQueue<Comic> comics, DataSource ds) {
        this.comics = comics;
        this.ds = ds;
    }

    @Override
    public void run() {

        while(true) {

            try {
                Comic comic = comics.take();
                ds.write(comic.toString() + " by " + Thread.currentThread());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
