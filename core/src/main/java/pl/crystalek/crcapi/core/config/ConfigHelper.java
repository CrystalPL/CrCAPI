package pl.crystalek.crcapi.core.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

@RequiredArgsConstructor
public class ConfigHelper {
    protected final JavaPlugin plugin;
    private final String fileName;
    protected File file;
    @Getter
    protected FileConfiguration configuration;

    public void checkExist() throws IOException {
        final File dataFolder = this.plugin.getDataFolder();
        if (!dataFolder.exists() && !dataFolder.mkdirs()) {
            throw new IOException();
        }

        this.file = new File(dataFolder, this.fileName);
        if (!this.file.exists()) {
            this.plugin.saveResource(this.fileName, true);
        }
    }

    public void load() {
        this.configuration = YamlConfiguration.loadConfiguration(this.file);
    }

}
