package pl.crystalek.crcapi.message.api.locale;

import lombok.NonNull;
import org.bukkit.entity.Player;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * The LocaleService interface defines methods for managing player locales.
 * <p>
 * The implementation of this interface is intended to be asynchronous, returning {@link CompletableFuture}.
 */
public interface LocaleService {

    /**
     * Retrieves the locale of a player, specified by their UUID.
     *
     * @param playerUUID The UUID of the player whose locale is to be retrieved.
     * @return A {@link CompletableFuture} that contains an Optional holding the player's locale.
     * The Optional will be empty if the player cannot be found.
     * @throws NullPointerException if the playerUUID is null
     */
    CompletableFuture<Optional<Locale>> getPlayerLocale(@NonNull final UUID playerUUID);

    /**
     * Sets the locale of a player, specified by their UUID.
     *
     * @param playerUUID The UUID of the player whose locale is to be set.
     * @param locale     The new locale for the player.
     * @return A {@link CompletableFuture} that contains a boolean indicating whether the player's locale was successfully set.
     * False will be returned if the player cannot be found.
     * @throws NullPointerException if the playerUUID or locale is null
     */
    CompletableFuture<Boolean> setPlayerLocale(@NonNull final UUID playerUUID, @NonNull final Locale locale);

    /**
     * Retrieves a map of all players and their locales.
     *
     * @return A {@link CompletableFuture} that contains a map of UUIDs to locales, representing all players and their locales.
     */
    CompletableFuture<Map<UUID, Locale>> getPlayersLocaleMap();

    /**
     * Retrieves the locales of a collection of players.
     *
     * @param players The collection of players whose locales are to be retrieved.
     * @return A {@link CompletableFuture} that contains a map of UUIDs to locales representing the locales of the given players.
     * @throws NullPointerException if the players collection is null
     */
    CompletableFuture<Map<UUID, Locale>> getPlayersLocaleMap(@NonNull final Collection<Player> players);
}