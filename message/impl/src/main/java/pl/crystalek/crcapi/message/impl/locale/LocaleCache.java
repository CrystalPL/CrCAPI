package pl.crystalek.crcapi.message.impl.locale;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.message.impl.config.Config;
import pl.crystalek.crcapi.message.impl.storage.Provider;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class LocaleCache {
    @Getter
    Map<Audience, Locale> audienceLocaleMap;
    BukkitAudiences bukkitAudiences;
    Provider provider;
    JavaPlugin plugin;

    public LocaleCache(final Map<Audience, Locale> audienceLocaleMap, final BukkitAudiences bukkitAudiences, final Provider provider, final JavaPlugin plugin, final Config config) {
        this.audienceLocaleMap = audienceLocaleMap;
        this.bukkitAudiences = bukkitAudiences;
        this.provider = provider;
        this.plugin = plugin;

        audienceLocaleMap.put(bukkitAudiences.console(), config.getConsoleLanguage());
    }

    public void addLocale(final Player player) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            final Optional<Locale> playerLocaleOptional = provider.getPlayerLocale(player.getUniqueId());
            final Locale playerLocale;
            if (!playerLocaleOptional.isPresent()) {
                playerLocale = LocaleUtils.getLocaleFromString(LocaleUtils.getLocale(player));
                provider.createPlayer(player.getUniqueId(), playerLocale);
            } else {
                playerLocale = playerLocaleOptional.get();
            }

            final Audience audience = bukkitAudiences.player(player);
            audienceLocaleMap.put(audience, playerLocale);
        });
    }

    public void removeLocale(final Player player) {
        final Audience audience = bukkitAudiences.player(player);
        audienceLocaleMap.remove(audience);
    }

    public Locale getLocale(final UUID playerUUID) {
        return audienceLocaleMap.get(bukkitAudiences.player(playerUUID));
    }

    public Locale getLocale(final Audience audience) {
        return audienceLocaleMap.get(audience);
    }
}
