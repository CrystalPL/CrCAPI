package pl.crystalek.crcapi.message.impl.storage;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.LocaleUtils;
import pl.crystalek.crcapi.database.config.DatabaseConfig;
import pl.crystalek.crcapi.database.provider.sql.BaseSQLProvider;
import pl.crystalek.crcapi.database.provider.sql.model.SQLFunction;
import pl.crystalek.crcapi.lib.hikari.HikariDataSource;

import java.sql.ResultSet;
import java.util.*;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public abstract class SQLProvider extends BaseSQLProvider implements Provider {
    String getPlayerLocale;
    String getMapLocales;

    public SQLProvider(final DatabaseConfig databaseConfig, final HikariDataSource database) {
        super(databaseConfig, database);

        this.getPlayerLocale = String.format("SELECT language_tag FROM %slocaleMap WHERE uuid = ? LIMIT 1;", databaseConfig.getPrefix());
        this.getMapLocales = String.format("SELECT * FROM %slocaleMap", databaseConfig.getPrefix());
    }

    @Override
    public void createTable() {
        final String userLocaleTable = String.format("CREATE TABLE IF NOT EXISTS %slocaleMap (\n" +
                "    uuid CHAR(36) NOT NULL UNIQUE PRIMARY KEY NOT NULL,\n" +
                "    language_tag TEXT NOT NULL\n" +
                ");", databaseConfig.getPrefix());

        executeUpdateAndOpenConnection(userLocaleTable);
    }

    @Override
    public Optional<Locale> getPlayerLocale(final UUID playerUUID) {
        final SQLFunction<ResultSet, Optional<Locale>> function = resultSet -> {
            if (resultSet == null || !resultSet.next()) {
                return Optional.empty();
            }

            final String languageTag = resultSet.getString("language_tag");
            final Locale locale = LocaleUtils.toLocale(languageTag);

            return Optional.of(locale);
        };

        return executeQueryAndOpenConnection(getPlayerLocale, function, playerUUID.toString());
    }

    @Override
    public Map<UUID, Locale> getUserLocaleMap() {
        final SQLFunction<ResultSet, Map<UUID, Locale>> function = resultSet -> {
            if (resultSet == null || !resultSet.next()) {
                return new HashMap<>();
            }

            final Map<UUID, Locale> userLocaleMap = new HashMap<>();
            do {
                final String languageTag = resultSet.getString("language_tag");
                final Locale locale = LocaleUtils.toLocale(languageTag);
                final UUID uuid = UUID.fromString(resultSet.getString("uuid"));

                userLocaleMap.put(uuid, locale);
            } while (resultSet.next());

            return userLocaleMap;
        };

        return executeQueryAndOpenConnection(getMapLocales, function);
    }
}
