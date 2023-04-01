package pl.crystalek.crcapi.gui.action;

import org.bukkit.event.inventory.InventoryEvent;

@FunctionalInterface
public interface Action<T extends InventoryEvent> {

    void execute(final T event);
}
