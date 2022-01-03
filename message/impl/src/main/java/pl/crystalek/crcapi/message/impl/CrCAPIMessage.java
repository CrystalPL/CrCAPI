package pl.crystalek.crcapi.message.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.core.config.ConfigHelper;
import pl.crystalek.crcapi.message.api.MessageAPI;
import pl.crystalek.crcapi.message.impl.config.Config;
import pl.crystalek.crcapi.message.impl.listener.PlayerJoinListener;
import pl.crystalek.crcapi.message.impl.listener.PlayerQuitListener;
import pl.crystalek.crcapi.message.impl.manager.LocalizedMessageAPI;
import pl.crystalek.crcapi.message.impl.manager.SingleMessageAPI;
import pl.crystalek.crcapi.message.impl.storage.Provider;
import pl.crystalek.crcapi.message.impl.storage.Storage;
import pl.crystalek.crcapi.message.impl.user.UserCache;
import pl.crystalek.crcapi.storage.BaseStorage;

import java.io.IOException;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class CrCAPIMessage {
    @Getter
    static BukkitAudiences bukkitAudiences;
    final JavaPlugin plugin;
    Storage storage;

    public void load() {
        bukkitAudiences = BukkitAudiences.create(plugin);

        final ConfigHelper configHelper = new ConfigHelper("config.yml", plugin);
        try {
            configHelper.checkExist();
            configHelper.load();
        } catch (final IOException exception) {
            plugin.getLogger().severe("Nie udało się utworzyć pliku konfiguracyjnego..");
            plugin.getLogger().severe("Wyłączanie pluginu");
            exception.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(plugin);
        }

        final Config config = new Config(configHelper.getConfiguration(), plugin);
        if (!config.load()) {
            plugin.getLogger().severe("Wyłączanie pluginu");
            Bukkit.getPluginManager().disablePlugin(plugin);
            return;
        }

        if (config.isLocalizedMessageEnable()) {
            storage = new Storage(new BaseStorage<>(config.getDatabaseConfig(), plugin));
            if (!storage.init()) {
                plugin.getLogger().severe("Wyłączanie pluginu");
                Bukkit.getPluginManager().disablePlugin(plugin);
                return;
            }

            final Provider provider = storage.getStorage().getProvider();

            final PluginManager pluginManager = Bukkit.getPluginManager();
            pluginManager.registerEvents(new PlayerJoinListener(plugin, provider), plugin);
            pluginManager.registerEvents(new PlayerQuitListener(), plugin);

            Bukkit.getOnlinePlayers().forEach(player -> UserCache.loadLocale(player, provider));
        }

        final ServicesManager servicesManager = Bukkit.getServicesManager();
        servicesManager.register(MessageAPI.class, new SingleMessageAPI(plugin), plugin, ServicePriority.Highest);
        servicesManager.register(MessageAPI.class, new LocalizedMessageAPI(plugin), plugin, ServicePriority.Highest);
    }

    public void close() {
        if (storage != null) {
            storage.getStorage().getDatabase().close();
        }
    }
}
