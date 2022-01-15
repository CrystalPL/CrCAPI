package pl.crystalek.crcapi.core.config;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class FileHelper extends ConfigHelper {

    public FileHelper(final JavaPlugin plugin, final String fileName) {
        super(plugin, fileName);
    }

    public void save() throws IOException {
        this.configuration.save(this.file);
    }

}
