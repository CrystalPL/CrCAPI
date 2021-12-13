package pl.crystalek.crcapi.messagei18n.storage.mysql;

import pl.crystalek.crcapi.messagei18n.storage.SQLProvider;
import pl.crystalek.crcapi.storage.config.DatabaseConfig;
import pl.crystalek.crcapi.storage.util.SQLUtil;

import java.util.Locale;
import java.util.UUID;

public final class MySQLProvider extends SQLProvider {
    private final String setPlayerLocale;

    public MySQLProvider(final SQLUtil sqlUtil, final DatabaseConfig databaseConfig) {
        super(sqlUtil, databaseConfig);

        this.setPlayerLocale = String.format("INSERT INTO %slocaleMap(uuid, language_tag) VALUES (?, ?) ON DUPLICATE KEY UPDATE language_tag = ?;", databaseConfig.getPrefix());
        this.createTable();
    }

    @Override
    public void setPlayerLocale(final UUID playerUUID, final Locale locale) {
        sqlUtil.executeUpdateAndOpenConnection(setPlayerLocale, playerUUID.toString(), locale.toString(), locale.toString());
    }
}
