package pl.crystalek.crcapi.storage;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.storage.config.DatabaseConfig;
import pl.crystalek.crcapi.storage.impl.Database;
import pl.crystalek.crcapi.storage.impl.mongo.MongoStorage;
import pl.crystalek.crcapi.storage.impl.mysql.MYSQLStorage;
import pl.crystalek.crcapi.storage.impl.sqlite.SQLiteStorage;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Getter
public final class BaseStorage<T extends BaseProvider> {
    final DatabaseConfig databaseConfig;
    final JavaPlugin plugin;
    Database database;
    @Setter
    T provider;

    public boolean initDatabase() {
        switch (databaseConfig.getStorageType()) {
            case MYSQL:
                database = new MYSQLStorage(databaseConfig);
                break;
            case SQLITE:
                database = new SQLiteStorage(databaseConfig.getSqliteDatabaseLocation(), plugin);
                break;
            case MONGODB:
                database = new MongoStorage(databaseConfig, plugin);
                break;
        }

        if (!database.connect() || !database.init()) {
            plugin.getLogger().severe("Wystąpił problem podczas próby łączenia z bazą danych");
            return false;
        } else {
            plugin.getLogger().info("Pomyślnie połączono z bazą danych!");
            return true;
        }
    }
}
