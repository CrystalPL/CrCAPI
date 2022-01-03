package pl.crystalek.crcapi.message.impl.storage.sqlite;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import pl.crystalek.crcapi.database.config.DatabaseConfig;
import pl.crystalek.crcapi.message.impl.storage.SQLProvider;

import java.util.Locale;
import java.util.UUID;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class SQLiteProvider extends SQLProvider {
    String setPlayerLocale;

    public SQLiteProvider(final DatabaseConfig databaseConfig, final HikariDataSource database) {
        super(databaseConfig, database);

        this.setPlayerLocale = String.format("INSERT OR REPLACE INTO %slocaleMap(uuid, language_tag) VALUES (?, ?);", databaseConfig.getPrefix());
    }

    @Override
    public void setPlayerLocale(final UUID playerUUID, final Locale locale) {
        executeUpdateAndOpenConnection(setPlayerLocale, playerUUID.toString(), locale.toString());
    }
}
