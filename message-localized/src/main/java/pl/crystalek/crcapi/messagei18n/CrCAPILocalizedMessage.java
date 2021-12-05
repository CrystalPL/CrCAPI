package pl.crystalek.crcapi.messagei18n;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.messagei18n.listener.PlayerJoinListener;

public final class CrCAPILocalizedMessage extends JavaPlugin {

    @Override
    public void onEnable() {
        final PluginManager pluginManager = Bukkit.getPluginManager();
        pluginManager.registerEvents(new PlayerJoinListener(this), this);
        pluginManager.registerEvents(new PlayerJoinListener(this), this);
    }
}
