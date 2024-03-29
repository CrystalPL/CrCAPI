package pl.crystalek.crcapi.gui.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.InventoryHolder;
import pl.crystalek.crcapi.gui.action.Action;
import pl.crystalek.crcapi.gui.type.BaseGUI;

import java.util.Optional;

public final class InventoryDragListener implements Listener {

    @EventHandler
    public void onInventoryDrag(final InventoryDragEvent event) {
        final InventoryHolder inventoryHolder = event.getInventory().getHolder();
        if (!(inventoryHolder instanceof BaseGUI)) {
            return;
        }

        final BaseGUI baseGUI = (BaseGUI) inventoryHolder;
        final Optional<Action<InventoryDragEvent>> dragAction = baseGUI.getActionManager().getAction(InventoryDragEvent.class);
        if (!dragAction.isPresent()) {
            return;
        }

        dragAction.get().execute(event);
    }
}
