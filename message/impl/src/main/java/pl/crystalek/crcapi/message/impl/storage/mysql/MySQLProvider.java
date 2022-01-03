package pl.crystalek.crcapi.message.impl.storage.mysql;

import com.zaxxer.hikari.HikariDataSource;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import pl.crystalek.crcapi.database.config.DatabaseConfig;
import pl.crystalek.crcapi.message.impl.storage.SQLProvider;

import java.util.Locale;
import java.util.UUID;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class MySQLProvider extends SQLProvider {
    String setPlayerLocale;

    public MySQLProvider(final DatabaseConfig databaseConfig, final HikariDataSource database) {
        super(databaseConfig, database);

        this.setPlayerLocale = String.format("INSERT INTO %slocaleMap(uuid, language_tag) VALUES (?, ?) ON DUPLICATE KEY UPDATE language_tag = ?;", databaseConfig.getPrefix());
    }

    @Override
    public void setPlayerLocale(final UUID playerUUID, final Locale locale) {
        executeUpdateAndOpenConnection(setPlayerLocale, playerUUID.toString(), locale.toString(), locale.toString());
    }
}
