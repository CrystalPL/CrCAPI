package pl.crystalek.crcapi.database.storage.impl.mysql;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.crystalek.crcapi.database.config.DatabaseConfig;
import pl.crystalek.crcapi.database.storage.model.Database;

import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MYSQLStorage implements Database {
    final DatabaseConfig databaseConfig;
    @Getter
    HikariDataSource database;

    @Override
    public boolean connect() {
        database = new HikariDataSource();
        database.setJdbcUrl("jdbc:mysql://" + databaseConfig.getHostname() + ":" + databaseConfig.getPort() + "/" + databaseConfig.getDatabase() + "?allowPublicKeyRetrieval=true" + "&useSSL=" + databaseConfig.isUseSSL());
        database.setDriverClassName("com.mysql.cj.jdbc.Driver");
        database.setUsername(databaseConfig.getUsername());
        database.setPassword(databaseConfig.getPassword());
        database.setMaximumPoolSize(databaseConfig.getPoolSize());
        database.setConnectionTimeout(databaseConfig.getConnectionTimeout());

        database.addDataSourceProperty("cachePrepStmts", true);
        database.addDataSourceProperty("prepStmtCacheSize", 250);
        database.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        database.addDataSourceProperty("useServerPrepStmts", true);

        return true;
    }

    @Override
    public boolean init() {
        try (
                final Connection connection = database.getConnection()
        ) {
            return true;
        } catch (final SQLException exception) {
            exception.printStackTrace();
            return false;
        }
    }

    @Override
    public void close() {
        if (database != null) {
            database.close();
        }
    }
}
