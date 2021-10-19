package pl.crystalek.crcapi.storage.config;

import lombok.experimental.UtilityClass;
import org.bukkit.configuration.ConfigurationSection;
import pl.crystalek.crcapi.config.exception.ConfigLoadException;

@UtilityClass
public class DatabaseConfigLoader {

    public DatabaseConfig getDatabaseConfig(final ConfigurationSection databaseConfiguration) throws ConfigLoadException {
        checkFieldExist(databaseConfiguration, "hostname");
        checkFieldExist(databaseConfiguration, "port");
        checkFieldExist(databaseConfiguration, "database");
        checkFieldExist(databaseConfiguration, "username");
        checkFieldExist(databaseConfiguration, "password");
        checkFieldExist(databaseConfiguration, "useSSL");
        checkFieldExist(databaseConfiguration, "poolSize");
        checkFieldExist(databaseConfiguration, "connectionTimeout");

        final String hostname = databaseConfiguration.getString("hostname");
        final String port = databaseConfiguration.getString("port");
        final String database = databaseConfiguration.getString("database");
        final String username = databaseConfiguration.getString("username");
        final String password = databaseConfiguration.getString("password");
        final boolean useSSL = databaseConfiguration.getBoolean("useSSL");
        final int poolSize = databaseConfiguration.getInt("poolSize");
        final int connectionTimeout = databaseConfiguration.getInt("connectionTimeout");

        return new DatabaseConfig(hostname, port, database, username, password, useSSL, poolSize, connectionTimeout);
    }

    private void checkFieldExist(final ConfigurationSection databaseConfiguration, final String field) throws ConfigLoadException {
        if (!databaseConfiguration.contains(field)) {
            throw new ConfigLoadException("Nie wykryto pola: " + field);
        }
    }
}
