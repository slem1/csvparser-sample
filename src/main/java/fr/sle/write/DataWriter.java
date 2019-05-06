package fr.sle.write;

import fr.sle.dao.EntityDao;

import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author slemoine
 */
public class DataWriter<T> implements Runnable {

    private final BlockingQueue<T> data;

    private final AtomicBoolean inputFlag;

    private final EntityDao<T> dao;

    public DataWriter(BlockingQueue<T> data, AtomicBoolean inputFlag, EntityDao<T> dao) {
        this.data = data;
        this.inputFlag = inputFlag;
        this.dao = dao;
    }

    @Override
    public void run() {

        while (inputFlag.get() || pollAndWrite()) {

        }

    }

    private boolean pollAndWrite() {

        try {
            T c = data.poll(1, TimeUnit.MILLISECONDS);

            if (c != null) {
                dao.insert(c);
                return true;
            }

            return false;

        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
