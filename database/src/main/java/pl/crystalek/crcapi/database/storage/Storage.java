package pl.crystalek.crcapi.database.storage;

import com.mongodb.client.MongoDatabase;
import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.database.config.DatabaseConfig;
import pl.crystalek.crcapi.database.provider.BaseProvider;
import pl.crystalek.crcapi.database.storage.impl.mongo.MongoStorage;
import pl.crystalek.crcapi.database.storage.impl.mysql.MYSQLStorage;
import pl.crystalek.crcapi.database.storage.impl.sqlite.SQLiteStorage;
import pl.crystalek.crcapi.database.storage.model.Database;

import java.lang.reflect.InvocationTargetException;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class Storage<T extends BaseProvider> {
    final DatabaseConfig databaseConfig;
    final JavaPlugin plugin;
    @Getter
    Database database;
    @Getter
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

    public boolean initProvider(final Class<? extends T> mysqlProviderClass, final Class<? extends T> sqliteProviderClass, final Class<? extends T> mongoProviderClass) {
        try {
            switch (databaseConfig.getStorageType()) {
                case MYSQL:
                    provider = mysqlProviderClass.getDeclaredConstructor(DatabaseConfig.class, HikariDataSource.class).newInstance(databaseConfig, ((MYSQLStorage) database).getDatabase());
                    break;
                case SQLITE:
                    provider = sqliteProviderClass.getDeclaredConstructor(DatabaseConfig.class, HikariDataSource.class).newInstance(databaseConfig, ((SQLiteStorage) database).getDatabase());
                    break;
                case MONGODB:
                    provider = mongoProviderClass.getDeclaredConstructor(DatabaseConfig.class, MongoDatabase.class).newInstance(databaseConfig, ((MongoStorage) database).getDatabase());
                    break;
            }
        } catch (final NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException exception) {
            exception.printStackTrace();
            plugin.getLogger().severe("Wystąpił problem podczas próby inicjacji bazy danych.");
            return false;
        }

        provider.createTable();
        return true;
    }

    public void close() {
        if (database != null) {
            database.close();
        }
    }
}
