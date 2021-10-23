package pl.crystalek.crcapi.storage;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import pl.crystalek.crcapi.storage.config.DatabaseConfig;
import pl.crystalek.crcapi.storage.impl.Database;
import pl.crystalek.crcapi.storage.impl.mongo.MongoStorage;
import pl.crystalek.crcapi.storage.impl.mysql.MYSQLStorage;
import pl.crystalek.crcapi.storage.impl.sqlite.SQLiteStorage;
import pl.crystalek.crcapi.util.LogUtil;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Getter
public final class BaseStorage<T extends BaseProvider> {
    final DatabaseConfig databaseConfig;
    Database database;
    @Setter
    T provider;

    public boolean initDatabase() {
        switch (databaseConfig.getStorageType()) {
            case MYSQL:
                database = new MYSQLStorage(databaseConfig);
                break;
            case SQLITE:
                database = new SQLiteStorage(databaseConfig.getSqliteDatabaseLocation());
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
}
