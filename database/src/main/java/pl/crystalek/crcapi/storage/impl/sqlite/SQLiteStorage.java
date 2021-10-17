package pl.crystalek.crcapi.storage.impl.sqlite;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.crystalek.crcapi.storage.impl.Database;
import pl.crystalek.crcapi.util.LogUtil;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class SQLiteStorage implements Database {
    final File databaseLocation;
    @Getter
    HikariDataSource database;

    @Override
    public boolean connect() {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (final ClassNotFoundException exception) {
            LogUtil.error("Wystąpił błąd podczas próby inicjalizacji SQLite");
            exception.printStackTrace();
            return false;
        }

        database = new HikariDataSource();
        database.addDataSourceProperty("cachePrepStmts", true);
        database.addDataSourceProperty("prepStmtCacheSize", 250);
        database.addDataSourceProperty("prepStmtCacheSqlLimit", 2048);
        database.addDataSourceProperty("useServerPrepStmts", true);
        database.setConnectionTestQuery("SELECT * FROM sqlite_master");

        return true;
    }

    @Override
    public boolean init() {
        if (!databaseLocation.exists()) {
            try {
                final File parentFile = databaseLocation.getParentFile();
                if (!parentFile.exists()) {
                    if (!parentFile.mkdir()) {
                        return false;
                    }
                }

                if (!databaseLocation.createNewFile()) {
                    return false;
                }
            } catch (final IOException exception) {
                LogUtil.error("Wystąpił błąd podczas próby stworzenia pliku bazy SQLite");
                exception.printStackTrace();
                return false;
            }
        }

        database.setJdbcUrl(String.format("jdbc:sqlite:%s", databaseLocation.getAbsolutePath()));

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
