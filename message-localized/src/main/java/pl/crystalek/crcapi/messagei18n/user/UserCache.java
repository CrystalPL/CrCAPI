package pl.crystalek.crcapi.messagei18n.user;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.audience.Audience;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@UtilityClass
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserCache {
    Map<Audience, Locale> userLocaleMap = new HashMap<>();

    public void setLocale(final Audience audience, final Locale locale) {
        userLocaleMap.put(audience, locale);
    }

    public void removeUser(final Audience audience) {
        userLocaleMap.remove(audience);
    }

    public Locale getLocale(final Audience audience) {
        return userLocaleMap.get(audience);
    }
}
