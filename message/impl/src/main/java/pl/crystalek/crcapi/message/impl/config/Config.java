package pl.crystalek.crcapi.message.impl.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.LocaleUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.core.config.exception.ConfigLoadException;
import pl.crystalek.crcapi.database.config.DatabaseConfig;
import pl.crystalek.crcapi.database.config.DatabaseConfigLoader;

import java.util.Locale;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class Config {
    @Getter
    static Locale defaultLocale;
    final FileConfiguration config;
    final JavaPlugin plugin;
    DatabaseConfig databaseConfig;
    boolean localizedMessageEnable;

    public boolean load() {
        try {
            this.databaseConfig = DatabaseConfigLoader.getDatabaseConfig(config.getConfigurationSection("database"), plugin);
        } catch (final ConfigLoadException exception) {
            Bukkit.getLogger().severe("Wystąpił błąd podczas próby załadowania bazy danych");
            Bukkit.getLogger().severe(exception.getMessage());
            return false;
        }

        defaultLocale = LocaleUtils.toLocale(config.getString("defaultLanguage"));
        try {
            if (defaultLocale.getCountry().isEmpty() || defaultLocale.getCountry().equals(" ")) {
                throw new IllegalArgumentException();
            }
        } catch (final IllegalArgumentException exception) {
            plugin.getLogger().severe("Nie odnaleziono języka: " + defaultLocale);
            return false;
        }

        this.localizedMessageEnable = config.getBoolean("useLocalizedMessage");

        return true;
    }
}
