package fr.sle.writer;

import fr.sle.DataSourceSingleton;
import fr.sle.model.Comic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author slemoine
 */
public class DataWriter implements Runnable {

    private final BlockingQueue<Comic> comics;

    private AtomicBoolean inputFlag;

    public DataWriter(BlockingQueue<Comic> comics, AtomicBoolean inputFlag) {
        this.comics = comics;
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

            if (c != null) {

                try (Connection connection = DataSourceSingleton.getInstance().getConnection()) {
                    String query = "INSERT INTO public.comics(title, price) VALUES (?,?)";
                    PreparedStatement stmt = connection.prepareStatement(query);
                    stmt.setString(1, c.getTitle());
                    stmt.setInt(2, c.getPrice());
                    stmt.execute();
                    connection.commit();
                }

                return true;
            }

            return false;

        } catch (InterruptedException | SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
