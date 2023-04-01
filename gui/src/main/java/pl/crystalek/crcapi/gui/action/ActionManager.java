package pl.crystalek.crcapi.gui.action;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ActionManager {
    Map<Class<? extends InventoryEvent>, Action<?>> actionMap = new HashMap<>();

    public ActionManager() {
        actionMap.put(InventoryClickEvent.class, new ClickActionManager());
    }

    /**
     * Retrieves the action associated with the specified inventory event class.
     *
     * @param inventoryEventClass the class of the inventory event for which to retrieve the associated action
     * @param <T>                 the type of inventory event for which to retrieve the action
     * @return an optional containing the action associated with the specified inventory event class, or an empty optional if no action is associated
     */
    public <T extends InventoryEvent> Optional<Action<T>> getAction(final Class<T> inventoryEventClass) {
        return Optional.ofNullable((Action<T>) actionMap.get(inventoryEventClass));
    }

    /**
     * Sets the action to be associated with the specified inventory event class.
     *
     * @param inventoryEventClass the class of the inventory event for which to set the associated action
     * @param action              the action to be associated with the specified inventory event class
     * @param <T>                 the type of inventory event for which to set the action
     * @throws IllegalArgumentException if the specified inventory event class is {@link  InventoryClickEvent}, since this class has its own dedicated action manager
     */
    public <T extends InventoryEvent> void setAction(final Class<T> inventoryEventClass, final Action<T> action) throws IllegalArgumentException {
        if (inventoryEventClass.isAssignableFrom(InventoryClickEvent.class)) {
            throw new IllegalArgumentException("cannot set action to InventoryClickEvent, use ClickActionManager");
        }

        actionMap.put(inventoryEventClass, action);
    }

    /**
     * Retrieves the {@link ClickActionManager} to manage the click actions.
     *
     * @return the {@link ClickActionManager}
     */
    public ClickActionManager getClickActionManager() {
        return (ClickActionManager) actionMap.get(InventoryClickEvent.class);
    }

    /**
     * Disables all actions associated with the InventoryClickEvent.
     *
     * @param disableInPlayerInventory whether to disable actions in player inventories as well
     */
    public void disableAllActions(final boolean disableInPlayerInventory) {
        getClickActionManager().disableAllActions(disableInPlayerInventory);
    }
}
