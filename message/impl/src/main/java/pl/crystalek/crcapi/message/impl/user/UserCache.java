package pl.crystalek.crcapi.message.impl.user;

import lombok.Getter;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.apache.commons.lang.LocaleUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import pl.crystalek.crcapi.message.impl.CrCAPIMessage;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@UtilityClass
public class UserCache {
    @Getter
    private final Map<Audience, Locale> audienceLocaleMap = new HashMap<>();
    private final Map<UUID, Locale> userLocaleMap;
    private final BukkitAudiences bukkitAudiences;
    private Function<Player, String> getLocaleFunction;

    static {
        bukkitAudiences = CrCAPIMessage.getInstance().getBukkitAudiences();
        userLocaleMap = CrCAPIMessage.getInstance().getStorage().getProvider().getUserLocaleMap();

        try {
            Player.Spigot.class.getMethod("getLocale");

            getLocaleFunction = player -> player.spigot().getLocale();
        } catch (final NoSuchMethodException exception) {
            getLocaleFunction = Player::getLocale;
        }
    }

    public void setLocale(final Player player, final Locale locale) {
        audienceLocaleMap.put(bukkitAudiences.player(player), locale);
        userLocaleMap.put(player.getUniqueId(), locale);
    }

    public void removeUser(final Player player) {
        audienceLocaleMap.remove(bukkitAudiences.player(player));
    }

    public void loadLocale(final Player player) {
        if (userLocaleMap.containsKey(player.getUniqueId())) {
            setLocale(player, userLocaleMap.get(player.getUniqueId()));
            return;
        }

        final Locale locale = getLocale(player);
        setLocale(player, locale);

        final Runnable runnable = () -> CrCAPIMessage.getInstance().getStorage().getProvider().setPlayerLocale(player.getUniqueId(), locale);
        Bukkit.getScheduler().runTaskAsynchronously(CrCAPIMessage.getInstance().getPlugin(), runnable);
    }

    private Locale getLocale(final Player player) {
        final String locale = getLocaleFunction.apply(player);
        return LocaleUtils.toLocale(locale.substring(0, 3) + locale.substring(3, 5).toUpperCase());
    }

    public Locale getLocale(final Audience audience) {
        return audienceLocaleMap.get(audience);
    }
}
