package pl.crystalek.crcapi.gui;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.gui.listener.InventoryClickListener;
import pl.crystalek.crcapi.gui.listener.InventoryCloseListener;
import pl.crystalek.crcapi.gui.listener.InventoryDragListener;
import pl.crystalek.crcapi.gui.listener.InventoryOpenListener;

public final class GuiAPI {

    public GuiAPI(final JavaPlugin plugin) {
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new InventoryCloseListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new InventoryDragListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new InventoryOpenListener(), plugin);
    }
}
