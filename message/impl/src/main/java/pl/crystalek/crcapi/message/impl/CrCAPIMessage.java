package pl.crystalek.crcapi.message.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.core.config.exception.ConfigLoadException;
import pl.crystalek.crcapi.database.storage.Storage;
import pl.crystalek.crcapi.message.api.MessageAPIProvider;
import pl.crystalek.crcapi.message.impl.config.Config;
import pl.crystalek.crcapi.message.impl.listener.PlayerJoinListener;
import pl.crystalek.crcapi.message.impl.listener.PlayerQuitListener;
import pl.crystalek.crcapi.message.impl.locale.LocaleCache;
import pl.crystalek.crcapi.message.impl.mesage.MessageAPIProviderImpl;
import pl.crystalek.crcapi.message.impl.storage.Provider;
import pl.crystalek.crcapi.message.impl.storage.mongo.MongoProvider;
import pl.crystalek.crcapi.message.impl.storage.sql.SQLProvider;

import java.io.IOException;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class CrCAPIMessage {
    final JavaPlugin apiPlugin;
    Storage<Provider> storage;

    public void load() {
        final BukkitAudiences bukkitAudiences = BukkitAudiences.create(apiPlugin);

        final Config config = new Config(apiPlugin, "config.yml");
        try {
            config.checkExist();
            config.load();
        } catch (final IOException exception) {
            apiPlugin.getLogger().severe("Nie udało się utworzyć pliku konfiguracyjnego..");
            apiPlugin.getLogger().severe("Wyłączanie pluginu..");
            Bukkit.getPluginManager().disablePlugin(apiPlugin);
            exception.printStackTrace();
            return;
        }

        try {
            config.loadConfig();
        } catch (final ConfigLoadException exception) {
            apiPlugin.getLogger().severe(exception.getMessage());
            apiPlugin.getLogger().severe("Wyłączanie pluginu..");
            Bukkit.getPluginManager().disablePlugin(apiPlugin);
            return;
        }

        storage = new Storage<>(config.getDatabaseConfig(), apiPlugin);
        if (!storage.initDatabase() || !storage.initProvider(SQLProvider.class, SQLProvider.class, MongoProvider.class)) {
            apiPlugin.getLogger().severe("Wyłączanie pluginu");
            Bukkit.getPluginManager().disablePlugin(apiPlugin);
            return;
        }

        final Provider provider = storage.getProvider();

        final Map<UUID, Locale> usersLocaleMap = provider.getPlayersLocaleMap(Bukkit.getOnlinePlayers());
        final Map<Audience, Locale> audienceLocaleMap = usersLocaleMap.entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> bukkitAudiences.player(entry.getKey()),
                        Map.Entry::getValue
                ));

        final LocaleCache localeCache = new LocaleCache(audienceLocaleMap, bukkitAudiences, provider, apiPlugin, config);

        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(localeCache), apiPlugin);
        pluginManager.registerEvents(new PlayerQuitListener(localeCache), apiPlugin);

        Bukkit.getServicesManager().register(MessageAPIProvider.class, new MessageAPIProviderImpl(localeCache, provider, config, bukkitAudiences), apiPlugin, ServicePriority.Highest);
    }

    public void close() {
        if (storage != null) {
            storage.close();
        }
    }
}
