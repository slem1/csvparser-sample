package fr.sle.dao;

import fr.sle.model.Comic;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @author slemoine
 */
public class ComicDao implements EntityDao<Comic> {

    private final DataSource dataSource;

    public ComicDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void insert(Comic c) throws SQLException {

        try (Connection connection = dataSource.getConnection()) {
            String query = "INSERT INTO public.comics(title, price) VALUES (?,?)";
            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, c.getTitle());
            stmt.setInt(2, c.getPrice());
            stmt.execute();
            connection.commit();
        }

    }
}
