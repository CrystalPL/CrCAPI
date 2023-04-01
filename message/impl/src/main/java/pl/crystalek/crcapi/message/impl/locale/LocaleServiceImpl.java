package pl.crystalek.crcapi.message.impl.locale;

import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.message.api.locale.LocaleService;
import pl.crystalek.crcapi.message.impl.storage.Provider;

import java.util.Collection;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class LocaleServiceImpl implements LocaleService {
    Provider provider;
    JavaPlugin plugin;
    LocaleCache localeCache;

    @Override
    public CompletableFuture<Optional<Locale>> getPlayerLocale(final @NonNull UUID playerUUID) {
        final CompletableFuture<Optional<Locale>> completableFuture = new CompletableFuture<>();
        final Locale locale = localeCache.getLocale(playerUUID);
        if (locale != null) {
            completableFuture.complete(Optional.of(locale));
        } else {
            Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> completableFuture.complete(provider.getPlayerLocale(playerUUID)));
        }

        return completableFuture;
    }

    @Override
    public CompletableFuture<Boolean> setPlayerLocale(final @NonNull UUID playerUUID, final @NonNull Locale locale) {
        final CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> completableFuture.complete(provider.setPlayerLocale(playerUUID, locale)));

        return completableFuture;
    }

    @Override
    public CompletableFuture<Map<UUID, Locale>> getPlayersLocaleMap() {
        final CompletableFuture<Map<UUID, Locale>> completableFuture = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> completableFuture.complete(provider.getPlayersLocaleMap()));

        return completableFuture;
    }

    @Override
    public CompletableFuture<Map<UUID, Locale>> getPlayersLocaleMap(final @NonNull Collection<Player> players) {
        final CompletableFuture<Map<UUID, Locale>> completableFuture = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> completableFuture.complete(provider.getPlayersLocaleMap(players)));

        return completableFuture;
    }

}
