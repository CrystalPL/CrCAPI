package pl.crystalek.crcapi.message.impl.storage.sql;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.LocaleUtils;
import org.bukkit.entity.Player;
import pl.crystalek.crcapi.database.config.DatabaseConfig;
import pl.crystalek.crcapi.database.provider.sql.BaseSQLProvider;
import pl.crystalek.crcapi.database.provider.sql.model.SQLFunction;
import pl.crystalek.crcapi.lib.hikari.HikariDataSource;
import pl.crystalek.crcapi.message.impl.storage.Provider;

import java.sql.ResultSet;
import java.util.Collection;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SQLProvider extends BaseSQLProvider implements Provider {
    String getPlayerLocale;
    String getLocalesMap;
    String getLocalesMapByPlayers;
    String createPlayer;
    String setPlayerLocale;

    public SQLProvider(final DatabaseConfig databaseConfig, final HikariDataSource database) {
        super(databaseConfig, database);

        final String prefix = databaseConfig.getPrefix();
        this.getPlayerLocale = String.format("SELECT language_tag FROM %slocaleMap WHERE uuid = ? LIMIT 1;", prefix);
        this.getLocalesMap = String.format("SELECT * FROM %slocaleMap", prefix);
        this.getLocalesMapByPlayers = String.format("SELECT * FROM %slocaleMap WHERE uuid in (?);", prefix);
        this.createPlayer = String.format("INSERT INTO %slocaleMap(uuid, language_tag) VALUES (?, ?);", prefix);
        this.setPlayerLocale = String.format("UPDATE %slocaleMap SET language_tag = ? WHERE uuid = ?;", prefix);
    }

    @Override
    public void createPlayer(final UUID playerUUID, final Locale locale) {
        executeUpdateAndOpenConnection(createPlayer, playerUUID.toString(), locale.toString());
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
    public boolean setPlayerLocale(final UUID playerUUID, final Locale locale) {
        return executeUpdateAndOpenConnection(setPlayerLocale, locale.toString(), playerUUID.toString());
    }

    @Override
    public Map<UUID, Locale> getPlayersLocaleMap(final Collection<? extends Player> players) {
        final String uuidList = players.stream()
                .map(Player::getUniqueId)
                .map(UUID::toString)
                .collect(Collectors.joining("', '", "'", "'"));

        final SQLFunction<ResultSet, Map<UUID, Locale>> function = resultSet -> {
            if (resultSet == null || !resultSet.next()) {
                return new HashMap<>();
            }

            final Map<UUID, Locale> userMap = new ConcurrentHashMap<>();
            do {
                final UUID uuid = UUID.fromString(resultSet.getString("uuid"));
                final Locale locale = LocaleUtils.toLocale(resultSet.getString("language_tag"));

                userMap.put(uuid, locale);
            } while (resultSet.next());

            return userMap;
        };

        return executeQueryAndOpenConnection(getLocalesMapByPlayers.replace("?", uuidList), function);
    }

    @Override
    public Map<UUID, Locale> getPlayersLocaleMap() {
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

        return executeQueryAndOpenConnection(getLocalesMap, function);
    }
}
