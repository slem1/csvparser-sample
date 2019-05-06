package fr.sle.dao;

import java.sql.SQLException;

/**
 * @author slemoine
 */
public interface EntityDao<T> {

    void insert(T entity) throws SQLException;

}
