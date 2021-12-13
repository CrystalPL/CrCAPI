package pl.crystalek.crcapi.messagei18n.storage;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.LocaleUtils;
import pl.crystalek.crcapi.storage.config.DatabaseConfig;
import pl.crystalek.crcapi.storage.util.SQLFunction;
import pl.crystalek.crcapi.storage.util.SQLUtil;

import java.sql.ResultSet;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
public abstract class SQLProvider extends Provider {
    protected final SQLUtil sqlUtil;
    private final DatabaseConfig databaseConfig;
    private String getPlayerLocale;

    @Override
    public void createTable() {
        final String prefix = databaseConfig.getPrefix();
        getPlayerLocale = String.format("SELECT language_tag FROM %slocaleMap WHERE uuid = ? LIMIT 1;", prefix);

        final String userLocaleTable = String.format("CREATE TABLE IF NOT EXISTS %slocaleMap (\n" +
                "    uuid CHAR(36) NOT NULL UNIQUE PRIMARY KEY NOT NULL,\n" +
                "    language_tag TEXT NOT NULL\n" +
                ");", prefix);

        sqlUtil.executeUpdateAndOpenConnection(userLocaleTable);
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

        return sqlUtil.executeQueryAndOpenConnection(getPlayerLocale, function, playerUUID.toString());
    }
}
