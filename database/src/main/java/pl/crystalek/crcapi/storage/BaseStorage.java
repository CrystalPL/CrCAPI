package pl.crystalek.crcapi.storage;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import pl.crystalek.crcapi.storage.config.DatabaseConfig;
import pl.crystalek.crcapi.storage.impl.Database;
import pl.crystalek.crcapi.storage.impl.mongo.MongoStorage;
import pl.crystalek.crcapi.storage.impl.mysql.MYSQLStorage;
import pl.crystalek.crcapi.storage.impl.sqlite.SQLiteStorage;
import pl.crystalek.crcapi.util.LogUtil;

import java.io.File;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public final class BaseStorage<T extends BaseProvider> {
    final StorageType storageType;
    final DatabaseConfig databaseConfig;
    final File sqliteDatabaseLocation;
    @Getter
    Database database;
    @Getter
    T provider;

    public boolean initDatabase() {
        switch (storageType) {
            case MYSQL:
                database = new MYSQLStorage(databaseConfig);
                break;
            case SQLITE:
                database = new SQLiteStorage(sqliteDatabaseLocation);
                break;
            case MONGODB:
                database = new MongoStorage(databaseConfig);
                break;
        }

        if (!database.connect() || !database.init()) {
            LogUtil.error("Wystąpił problem podczas próby łączenia z bazą danych");
            return false;
        } else {
            LogUtil.info("Pomyślnie połączono z bazą danych!");
            return true;
        }
    }

    public void initProvider(final T mysqlProvider, T sqliteProvider, T mongoProvider) {
        switch (storageType) {
            case MYSQL:
                provider = mysqlProvider;
                break;
            case SQLITE:
                provider = sqliteProvider;
                break;
            case MONGODB:
                provider = mongoProvider;
                break;
        }

        provider.createTable();
    }
}
