package pl.crystalek.crcapi.gui;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.gui.listener.InventoryClickListener;
import pl.crystalek.crcapi.gui.listener.InventoryCloseListener;
import pl.crystalek.crcapi.gui.listener.InventoryDragListener;
import pl.crystalek.crcapi.gui.listener.InventoryOpenListener;

public final class CrCAPIGUI extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryCloseListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryDragListener(), this);
        Bukkit.getPluginManager().registerEvents(new InventoryOpenListener(), this);
    }

}
