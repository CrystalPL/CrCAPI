package pl.crystalek.crcapi.gui.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.InventoryHolder;
import pl.crystalek.crcapi.gui.action.Action;
import pl.crystalek.crcapi.gui.type.BaseGUI;

import java.util.Optional;

public class InventoryOpenListener implements Listener {

    @EventHandler
    public void onOpen(final InventoryOpenEvent event) {
        final InventoryHolder inventoryHolder = event.getInventory().getHolder();
        if (!(inventoryHolder instanceof BaseGUI)) {
            return;
        }


        final BaseGUI baseGUI = (BaseGUI) inventoryHolder;
        final Optional<Action<InventoryOpenEvent>> openAction = baseGUI.getActionManager().getAction(InventoryOpenEvent.class);
        if (!openAction.isPresent()) {
            return;
        }

        openAction.get().execute(event);
    }
}
