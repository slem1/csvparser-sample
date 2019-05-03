package fr.sle.writer;

import fr.sle.model.Comic;

import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author slemoine
 */
public class DataWriter implements Runnable {

    private final BlockingQueue<Comic> comics;

    private final DataSource ds;

    private AtomicBoolean inputFlag;

    public DataWriter(BlockingQueue<Comic> comics, DataSource ds, AtomicBoolean inputFlag) {
        this.comics = comics;
        this.ds = ds;
        this.inputFlag = inputFlag;
    }

    @Override
    public void run() {

        while (inputFlag.get() || pollAndWrite()) {

        }

    }

    private boolean pollAndWrite() {

        try {
            Comic c = comics.poll(1, TimeUnit.MILLISECONDS);

            if (c != null){
               ds.write(c.toString() + " by " + Thread.currentThread());
                return true;
            }

            return false;

        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return false;
    }
}
