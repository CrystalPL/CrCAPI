package pl.crystalek.crcapi.message.api;

import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.message.api.locale.LocaleService;

public interface MessageAPIProvider {

    /**
     * Returns an instance of the {@link MessageAPI} interface for the given plugin instance.
     *
     * @param plugin the {@link JavaPlugin} instance for which the MessageAPI instance is created
     * @return the {@link MessageAPI} instance for the given plugin instance.
     */
    MessageAPI getMessage(final JavaPlugin plugin);

    /**
     * Returns an instance of the {@link LocaleService} interface for the given plugin instance.
     *
     * @param plugin the {@link JavaPlugin} instance for which the LocaleService instance is created
     * @return the {@link LocaleService} instance for the given plugin instance.
     */
    LocaleService getLocaleService(final JavaPlugin plugin);
}