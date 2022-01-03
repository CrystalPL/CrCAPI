package pl.crystalek.crcapi.message.impl.storage.sqlite;

import pl.crystalek.crcapi.message.impl.storage.SQLProvider;
import pl.crystalek.crcapi.storage.config.DatabaseConfig;
import pl.crystalek.crcapi.storage.util.SQLUtil;

import java.util.Locale;
import java.util.UUID;

public final class SQLiteProvider extends SQLProvider {
    private final String setPlayerLocale;

    public SQLiteProvider(final SQLUtil sqlUtil, final DatabaseConfig databaseConfig) {
        super(sqlUtil, databaseConfig);

        this.setPlayerLocale = String.format("INSERT OR REPLACE INTO %slocaleMap(uuid, language_tag) VALUES (?, ?);", databaseConfig.getPrefix());
        this.createTable();
    }

    @Override
    public void setPlayerLocale(final UUID playerUUID, final Locale locale) {
        sqlUtil.executeUpdateAndOpenConnection(setPlayerLocale, playerUUID.toString(), locale.toString());
    }
}
