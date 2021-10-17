package pl.crystalek.crcapi.gui.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;
import pl.crystalek.crcapi.gui.type.BaseGUI;

import java.util.function.Consumer;

public final class InventoryDragListener implements Listener {

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent event) {
        final InventoryHolder inventoryHolder = event.getInventory().getHolder();
        if (!(inventoryHolder instanceof BaseGUI)) {
            return;
        }

        final BaseGUI baseGUI = (BaseGUI) inventoryHolder;
        final Consumer<InventoryDragEvent> inventoryDragEvent = baseGUI.getInventoryDragEvent();
        if (inventoryDragEvent != null) {
            inventoryDragEvent.accept(event);
        }
    }
}
