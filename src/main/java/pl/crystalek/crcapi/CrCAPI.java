package pl.crystalek.crcapi;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.gui.listener.InventoryClickListener;
import pl.crystalek.crcapi.gui.listener.InventoryDragListener;
import pl.crystalek.crcapi.message.impl.CrCAPIMessage;

public final class CrCAPI extends JavaPlugin {
    private CrCAPIMessage crCAPIMessage;

    @Override
    public void onEnable() {
        //start listener to gui api
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryDragListener(), this);

        //start message api
        crCAPIMessage = new CrCAPIMessage(this);
        crCAPIMessage.load();
    }

    @Override
    public void onDisable() {
        //close api message database connection
        crCAPIMessage.close();
    }
}
