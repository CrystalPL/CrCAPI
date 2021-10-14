package pl.crystalek.crcapi.gui;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.gui.listener.InventoryClickListener;
import pl.crystalek.crcapi.gui.listener.InventoryCloseListener;
import pl.crystalek.crcapi.gui.listener.InventoryDragListener;
import pl.crystalek.crcapi.gui.listener.InventoryOpenListener;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class GuiAPI {

    public GuiAPI() {
        final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(GuiAPI.class);

        Bukkit.getPluginManager().registerEvents(new InventoryClickListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new InventoryCloseListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new InventoryDragListener(), plugin);
        Bukkit.getPluginManager().registerEvents(new InventoryOpenListener(), plugin);
    }
}
