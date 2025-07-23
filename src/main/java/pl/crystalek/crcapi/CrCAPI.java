package pl.crystalek.crcapi;

import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.command.api.CommandServiceProvider;
import pl.crystalek.crcapi.command.impl.CommandServiceProviderImpl;
import pl.crystalek.crcapi.gui.listener.InventoryClickListener;
import pl.crystalek.crcapi.gui.listener.InventoryCloseListener;
import pl.crystalek.crcapi.gui.listener.InventoryDragListener;
import pl.crystalek.crcapi.gui.listener.InventoryOpenListener;
import pl.crystalek.crcapi.message.impl.CrCAPIMessage;

public final class CrCAPI extends JavaPlugin {
    private CrCAPIMessage crCAPIMessage;

    @Override
    public void onEnable() {
        //start listener to gui api
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryDragListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryOpenListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryCloseListener(), this);

        //start message api
        crCAPIMessage = new CrCAPIMessage(this);
        crCAPIMessage.load();

        Bukkit.getServicesManager().register(CommandServiceProvider.class, new CommandServiceProviderImpl(), this, ServicePriority.Highest);
    }

    @Override
    public void onDisable() {
        //close api message database connection
        crCAPIMessage.close();
    }
}
