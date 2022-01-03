package pl.crystalek.crcapi.database.storage.impl.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoException;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.database.config.DatabaseConfig;
import pl.crystalek.crcapi.database.storage.model.Database;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class MongoStorage implements Database {
    final DatabaseConfig databaseConfig;
    final JavaPlugin plugin;
    MongoClient mongoClient;
    MongoClientSettings clientSettings;
    @Getter
    MongoDatabase database;

    @Override
    public boolean connect() {
        try {
            clientSettings = MongoClientSettings.builder()
                    .applyToSslSettings(builder -> builder.enabled(databaseConfig.isUseSSL()))
                    .applyToSocketSettings(builder -> builder.connectTimeout(databaseConfig.getConnectionTimeout(), TimeUnit.MILLISECONDS))
                    .applyToConnectionPoolSettings(builder -> builder.maxSize(databaseConfig.getPoolSize()))
                    .applyConnectionString(new ConnectionString("mongodb://" + databaseConfig.getUsername() + ":" + databaseConfig.getPassword() + "@" + databaseConfig.getHostname() + ":" + databaseConfig.getPort() + "/" + databaseConfig.getDatabase()))
                    .build();

            return true;
        } catch (final MongoException exception) {
            plugin.getLogger().severe("Wystąpił błąd podczas próby konfiguracji połączenia bazy mongodb");
            return false;
        }
    }

    @Override
    public boolean init() {
        try {
            mongoClient = MongoClients.create(clientSettings);
            database = mongoClient.getDatabase(databaseConfig.getDatabase());
            return true;
        } catch (final MongoException exception) {
            plugin.getLogger().severe("Wystąpił błąd podczas próby połączenia z bazą mongodb");
            return false;
        }
    }

    @Override
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
        }
    }
}
