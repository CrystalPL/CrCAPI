package pl.crystalek.crcapi.messagei18n.storage;

import pl.crystalek.crcapi.storage.BaseProvider;

import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

public abstract class Provider extends BaseProvider {

    public abstract Optional<Locale> getPlayerLocale(final UUID playerUUID);

    public abstract void setPlayerLocale(final UUID playerUUID, final Locale locale);
}
