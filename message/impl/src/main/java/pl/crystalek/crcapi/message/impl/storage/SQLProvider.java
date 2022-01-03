package pl.crystalek.crcapi.message.impl.storage;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.LocaleUtils;
import pl.crystalek.crcapi.database.config.DatabaseConfig;
import pl.crystalek.crcapi.database.provider.sql.BaseSQLProvider;
import pl.crystalek.crcapi.database.provider.sql.model.SQLFunction;

import java.sql.ResultSet;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public abstract class SQLProvider extends BaseSQLProvider implements Provider {
    String getPlayerLocale;

    public SQLProvider(final DatabaseConfig databaseConfig, final HikariDataSource database) {
        super(databaseConfig, database);

        this.getPlayerLocale = String.format("SELECT language_tag FROM %slocaleMap WHERE uuid = ? LIMIT 1;", databaseConfig.getPrefix());
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
}
