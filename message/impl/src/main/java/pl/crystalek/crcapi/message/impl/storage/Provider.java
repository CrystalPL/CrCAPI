package pl.crystalek.crcapi.message.impl.storage;

import org.bukkit.entity.Player;
import pl.crystalek.crcapi.database.provider.BaseProvider;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface Provider extends BaseProvider {

    /**
     * Insert a new player to the database
     *
     * @param playerUUID the UUID of the player
     * @param locale     the locale of the player
     */
    void createPlayer(final UUID playerUUID, final Locale locale);

    /**
     * Retrieves the locale for a specific player.
     *
     * @param playerUUID the UUID of the player to retrieve the locale for
     * @return an {@link Optional} containing the locale for the player, or an empty Optional if the player does not have a locale set
     */
    Optional<Locale> getPlayerLocale(final UUID playerUUID);

    /**
     * Sets the locale for a specific player.
     *
     * @param playerUUID the UUID of the player to set the locale for
     * @param locale     the locale to set for the player
     */
    boolean setPlayerLocale(final UUID playerUUID, final Locale locale);

    /**
     * Retrieves a map of all player UUIDs and their corresponding locales.
     *
     * @return a map of player UUIDs to locales
     */
    Map<UUID, Locale> getPlayersLocaleMap();

    /**
     * Retrieves the locales of a collection of players.
     *
     * @param players The collection of players whose locales are to be retrieved.
     * @return A map of UUIDs to locales representing the locales of the given players.
     */
    Map<UUID, Locale> getPlayersLocaleMap(final Collection<? extends Player> players);
}
