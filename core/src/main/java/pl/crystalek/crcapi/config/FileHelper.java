package pl.crystalek.crcapi.config;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class FileHelper extends ConfigHelper {

    public FileHelper(final String fileName, final JavaPlugin plugin) {
        super(fileName, plugin);
    }

    public void save() throws IOException {
        configuration.save(file);
    }
}
