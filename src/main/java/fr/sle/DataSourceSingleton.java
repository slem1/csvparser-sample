package fr.sle;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;


/**
 * @author slemoine
 */
public class DataSourceSingleton {

    static {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:postgresql://localhost:5432/parser");
        config.setUsername("parser");
        config.setPassword("parser");
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        config.setAutoCommit(false);

        INSTANCE = new HikariDataSource(config);
    }

    private static DataSource INSTANCE;

    public static DataSource getInstance() {
        return INSTANCE;
    }


}
