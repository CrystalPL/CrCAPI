package pl.crystalek.crcapi.gui.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import pl.crystalek.crcapi.gui.action.Action;
import pl.crystalek.crcapi.gui.type.BaseGUI;

import java.util.Optional;

public class InventoryCloseListener implements Listener {

    @EventHandler
    public void onClose(final InventoryCloseEvent event) {
        final InventoryHolder inventoryHolder = event.getInventory().getHolder();
        if (!(inventoryHolder instanceof BaseGUI)) {
            return;
        }

        final BaseGUI baseGUI = (BaseGUI) inventoryHolder;
        final Optional<Action<InventoryCloseEvent>> closeAction = baseGUI.getActionManager().getAction(InventoryCloseEvent.class);
        if (!closeAction.isPresent()) {
            return;
        }

        closeAction.get().execute(event);
    }
}
