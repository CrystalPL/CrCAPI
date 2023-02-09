package pl.crystalek.crcapi.message.api;

import org.bukkit.plugin.java.JavaPlugin;

public interface MessageAPIProvider {

    MessageAPI getMessage(final JavaPlugin plugin);
}
