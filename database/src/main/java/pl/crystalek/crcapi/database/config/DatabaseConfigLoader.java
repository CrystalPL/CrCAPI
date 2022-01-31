package pl.crystalek.crcapi.database.config;

import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.core.config.ConfigParserUtil;
import pl.crystalek.crcapi.core.config.exception.ConfigLoadException;
import pl.crystalek.crcapi.database.storage.type.StorageType;

import java.io.File;

@UtilityClass
public class DatabaseConfigLoader {

    public DatabaseConfig getDatabaseConfig(final ConfigurationSection databaseConfiguration, final JavaPlugin plugin) throws ConfigLoadException {
        ConfigParserUtil.checkFieldExist(databaseConfiguration, "storageType");

        final ConfigurationSection databaseSettings = databaseConfiguration.getConfigurationSection("settings");
        ConfigParserUtil.checkFieldExist(databaseSettings, "hostname");
        ConfigParserUtil.checkFieldExist(databaseSettings, "port");
        ConfigParserUtil.checkFieldExist(databaseSettings, "database");
        ConfigParserUtil.checkFieldExist(databaseSettings, "username");
        ConfigParserUtil.checkFieldExist(databaseSettings, "password");
        ConfigParserUtil.checkFieldExist(databaseSettings, "useSSL");
        ConfigParserUtil.checkFieldExist(databaseSettings, "poolSize");
        ConfigParserUtil.checkFieldExist(databaseSettings, "connectionTimeout");
        ConfigParserUtil.checkFieldExist(databaseSettings, "prefix");

        final ConfigurationSection sqliteConfiguration = databaseConfiguration.getConfigurationSection("sqlite");
        ConfigParserUtil.checkFieldExist(sqliteConfiguration, "fileName");

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
        final File sqliteDatabaseLocation = new File(plugin.getDataFolder(), sqliteFileName);

        return new DatabaseConfig(storageType, hostname, port, database, username, password, useSSL, poolSize, connectionTimeout, prefix, sqliteDatabaseLocation);
    }
}
