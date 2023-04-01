package pl.crystalek.crcapi.message.impl.locale;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.function.Function;

@UtilityClass
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class LocaleUtils {
    Function<Player, String> localeFunction;

    static {
        try {
            Player.Spigot.class.getMethod("getLocale");

            localeFunction = player -> player.spigot().getLocale();
        } catch (final NoSuchMethodException exception) {
            localeFunction = Player::getLocale;
        }
    }

    public String getLocale(final Player player) {
        return localeFunction.apply(player);
    }

    public Locale getLocaleFromString(final String locale) {
        return org.apache.commons.lang.LocaleUtils.toLocale(locale.substring(0, 3) + locale.substring(3, 5).toUpperCase());
    }
}
