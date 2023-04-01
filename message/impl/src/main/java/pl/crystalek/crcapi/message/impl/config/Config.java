package pl.crystalek.crcapi.message.impl.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang.LocaleUtils;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.core.config.ConfigHelper;
import pl.crystalek.crcapi.core.config.ConfigParserUtil;
import pl.crystalek.crcapi.core.config.exception.ConfigLoadException;
import pl.crystalek.crcapi.database.config.DatabaseConfig;
import pl.crystalek.crcapi.database.config.DatabaseConfigLoader;

import java.util.Locale;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class Config extends ConfigHelper {
    Locale defaultLanguage;
    Locale consoleLanguage;
    DatabaseConfig databaseConfig;

    public Config(final JavaPlugin plugin, final String fileName) {
        super(plugin, fileName);
    }

    public void loadConfig() throws ConfigLoadException {
        this.databaseConfig = DatabaseConfigLoader.getDatabaseConfig(configuration.getConfigurationSection("database"), plugin);

        this.defaultLanguage = LocaleUtils.toLocale(ConfigParserUtil.getString(configuration, "defaultLanguage"));
        if (defaultLanguage.getCountry().isEmpty() || defaultLanguage.getCountry().equals(" ")) {
            throw new ConfigLoadException("Nie odnaleziono języka: " + defaultLanguage);
        }

        this.consoleLanguage = LocaleUtils.toLocale(ConfigParserUtil.getString(configuration, "consoleLanguage"));
        if (consoleLanguage.getCountry().isEmpty() || consoleLanguage.getCountry().equals(" ")) {
            throw new ConfigLoadException("Nie odnaleziono języka: " + consoleLanguage);
        }
    }
}
