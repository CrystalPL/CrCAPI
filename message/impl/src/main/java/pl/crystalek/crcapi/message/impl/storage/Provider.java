package pl.crystalek.crcapi.message.impl.storage;

import pl.crystalek.crcapi.database.provider.BaseProvider;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface Provider extends BaseProvider {

    Optional<Locale> getPlayerLocale(final UUID playerUUID);

    void setPlayerLocale(final UUID playerUUID, final Locale locale);

    Map<UUID, Locale> getUserLocaleMap();
}
