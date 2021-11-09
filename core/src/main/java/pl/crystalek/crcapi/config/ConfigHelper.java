package pl.crystalek.crcapi.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

@FieldDefaults(level = AccessLevel.PACKAGE)
@RequiredArgsConstructor
public class ConfigHelper {
    final String fileName;
    final JavaPlugin plugin;
    File file;
    @Getter
    FileConfiguration configuration;

    public void checkExist() throws IOException {
        final File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists()) {
            final boolean mkdirs = dataFolder.mkdirs();
            if (!mkdirs) {
                throw new IOException();
            }
        }

        file = new File(dataFolder, fileName);
        if (!file.exists()) {
            plugin.saveResource(fileName, true);
        }
    }

    public void load() {
        configuration = YamlConfiguration.loadConfiguration(file);
    }
}
