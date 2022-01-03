package pl.crystalek.crcapi.message.impl.user;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.apache.commons.lang.LocaleUtils;
import org.bukkit.entity.Player;
import pl.crystalek.crcapi.message.impl.CrCAPIMessage;
import pl.crystalek.crcapi.message.impl.storage.Provider;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@UtilityClass
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserCache {
    Map<Audience, Locale> userLocaleMap = new HashMap<>();
    BukkitAudiences bukkitAudiences;
    Function<Player, String> getLocaleFunction;

    static {
        bukkitAudiences = CrCAPIMessage.getBukkitAudiences();
        try {
            Player.Spigot.class.getMethod("getLocale");

            getLocaleFunction = player -> player.spigot().getLocale();
        } catch (final NoSuchMethodException exception) {
            getLocaleFunction = Player::getLocale;
        }
    }

    public void setLocale(final Player player, final Locale locale) {
        userLocaleMap.put(bukkitAudiences.player(player), locale);
    }

    public void removeUser(final Player player) {
        userLocaleMap.remove(bukkitAudiences.player(player));
    }

    public void loadLocale(final Player player, final Provider provider) {
        final Optional<Locale> playerLocaleOptional = provider.getPlayerLocale(player.getUniqueId());
        if (playerLocaleOptional.isPresent()) {
            setLocale(player, playerLocaleOptional.get());
            return;
        }

        final Locale locale = getLocale(player);
        setLocale(player, locale);
        provider.setPlayerLocale(player.getUniqueId(), locale);
    }

    private Locale getLocale(final Player player) {
        final String locale = getLocaleFunction.apply(player);
        return LocaleUtils.toLocale(locale.substring(0, 3) + locale.substring(3, 5).toUpperCase());
    }

    public Locale getLocale(final Audience audience) {
        return userLocaleMap.get(audience);
    }
}
