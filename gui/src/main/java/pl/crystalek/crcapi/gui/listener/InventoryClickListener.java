package pl.crystalek.crcapi.gui.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import pl.crystalek.crcapi.gui.item.GUIItem;
import pl.crystalek.crcapi.gui.type.BaseGUI;

import java.util.Optional;

public final class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(final InventoryClickEvent event) {
        final InventoryHolder inventoryHolder = event.getInventory().getHolder();
        if (!(inventoryHolder instanceof BaseGUI)) {
            return;
        }

        final Inventory clickedInventory = event.getClickedInventory();
        if (clickedInventory == null) {
            return;
        }

        final BaseGUI baseGUI = (BaseGUI) inventoryHolder;
        baseGUI.getActionManager().getClickActionManager().execute(event);
        final Optional<GUIItem> slotOptional = baseGUI.getItem(event.getSlot() + 1);
        if (!slotOptional.isPresent()) {
            return;
        }

        final GUIItem guiItem = slotOptional.get();
        guiItem.getClickActionManager().execute(event);
    }
}
