package pl.crystalek.crcapi.messagei18n;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.config.ConfigHelper;
import pl.crystalek.crcapi.messagei18n.config.Config;
import pl.crystalek.crcapi.messagei18n.listener.PlayerJoinListener;
import pl.crystalek.crcapi.messagei18n.listener.PlayerQuitListener;
import pl.crystalek.crcapi.messagei18n.storage.Provider;
import pl.crystalek.crcapi.messagei18n.storage.Storage;
import pl.crystalek.crcapi.messagei18n.user.UserCache;
import pl.crystalek.crcapi.storage.BaseStorage;

import java.io.IOException;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class CrCAPILocalizedMessage extends JavaPlugin {
    @Getter
    static BukkitAudiences bukkitAudiences;
    Storage storage;

    @Override
    public void onEnable() {
        final ConfigHelper configHelper = new ConfigHelper("config.yml", this);
        try {
            configHelper.checkExist();
            configHelper.load();
        } catch (final IOException exception) {
            getLogger().severe("Nie udało się utworzyć pliku konfiguracyjnego..");
            getLogger().severe("Wyłączanie pluginu");
            exception.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(this);
        }

        final Config config = new Config(configHelper.getConfiguration(), this);
        if (!config.load()) {
            getLogger().severe("Wyłączanie pluginu");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        storage = new Storage(new BaseStorage<>(config.getDatabaseConfig(), this));
        if (!storage.init()) {
            getLogger().severe("Wyłączanie pluginu");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        final Provider provider = storage.getStorage().getProvider();

        bukkitAudiences = BukkitAudiences.create(this);

        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(this, provider), this);
        pluginManager.registerEvents(new PlayerQuitListener(), this);

        Bukkit.getOnlinePlayers().forEach(player -> UserCache.loadLocale(player, provider));
    }

    @Override
    public void onDisable() {
        if (storage != null) {
            storage.getStorage().getDatabase().close();
        }
    }
}
