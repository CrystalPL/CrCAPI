package pl.crystalek.crcapi.message.impl.mesage;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.message.api.MessageAPI;
import pl.crystalek.crcapi.message.api.MessageAPIProvider;
import pl.crystalek.crcapi.message.api.locale.LocaleService;
import pl.crystalek.crcapi.message.impl.config.Config;
import pl.crystalek.crcapi.message.impl.locale.LocaleCache;
import pl.crystalek.crcapi.message.impl.locale.LocaleServiceImpl;
import pl.crystalek.crcapi.message.impl.storage.Provider;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class MessageAPIProviderImpl implements MessageAPIProvider {
    LocaleCache localeCache;
    Provider provider;
    Config config;
    BukkitAudiences bukkitAudiences;

    @Override
    public MessageAPI getMessage(final JavaPlugin plugin) {
        return new MessageAPIImpl(plugin, localeCache, config, bukkitAudiences);
    }

    @Override
    public LocaleService getLocaleService(final JavaPlugin plugin) {
        return new LocaleServiceImpl(provider, plugin, localeCache);
    }
}
