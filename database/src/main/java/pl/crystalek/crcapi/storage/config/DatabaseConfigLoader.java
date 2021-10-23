package pl.crystalek.crcapi.storage.config;

import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.config.exception.ConfigLoadException;
import pl.crystalek.crcapi.storage.StorageType;

import java.io.File;

@UtilityClass
public class DatabaseConfigLoader {

    public DatabaseConfig getDatabaseConfig(final ConfigurationSection databaseConfiguration) throws ConfigLoadException {
        checkFieldExist(databaseConfiguration, "storageType");

        final ConfigurationSection databaseSettings = databaseConfiguration.getConfigurationSection("settings");
        checkFieldExist(databaseSettings, "hostname");
        checkFieldExist(databaseSettings, "port");
        checkFieldExist(databaseSettings, "database");
        checkFieldExist(databaseSettings, "username");
        checkFieldExist(databaseSettings, "password");
        checkFieldExist(databaseSettings, "useSSL");
        checkFieldExist(databaseSettings, "poolSize");
        checkFieldExist(databaseSettings, "connectionTimeout");
        checkFieldExist(databaseSettings, "prefix");

        final ConfigurationSection sqliteConfiguration = databaseConfiguration.getConfigurationSection("sqlite");
        checkFieldExist(sqliteConfiguration, "fileName");

        final StorageType storageType;
        try {
            storageType = StorageType.valueOf(databaseConfiguration.getString("storageType").toUpperCase());
        } catch (final IllegalArgumentException exception) {
            throw new ConfigLoadException("Nie odnaleziono bazy: " + databaseConfiguration.getString("storageType"));
        }
        final String hostname = databaseSettings.getString("hostname");
        final String port = databaseSettings.getString("port");
        final String database = databaseSettings.getString("database");
        final String username = databaseSettings.getString("username");
        final String password = databaseSettings.getString("password");
        final boolean useSSL = databaseSettings.getBoolean("useSSL");
        final int poolSize = databaseSettings.getInt("poolSize");
        final int connectionTimeout = databaseSettings.getInt("connectionTimeout");
        final String prefix = databaseSettings.getString("prefix");
        final String sqliteFileName = sqliteConfiguration.getString("fileName");
        final File sqliteDatabaseLocation = new File(JavaPlugin.getProvidingPlugin(DatabaseConfigLoader.class).getDataFolder(), sqliteFileName);

        return new DatabaseConfig(storageType, hostname, port, database, username, password, useSSL, poolSize, connectionTimeout, prefix, sqliteDatabaseLocation);
    }

    private void checkFieldExist(final ConfigurationSection databaseConfiguration, final String field) throws ConfigLoadException {
        if (!databaseConfiguration.contains(field)) {
            throw new ConfigLoadException("Nie wykryto pola: " + field);
        }
    }
}
