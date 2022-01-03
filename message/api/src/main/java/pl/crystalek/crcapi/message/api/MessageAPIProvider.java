package pl.crystalek.crcapi.message.api;

import org.bukkit.plugin.java.JavaPlugin;

public interface MessageAPIProvider {

    MessageAPI getSingleMessage(final JavaPlugin plugin);

    MessageAPI getLocalizedMessage(final JavaPlugin plugin);
}
