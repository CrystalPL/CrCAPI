package pl.crystalek.crcapi.gui.action;

import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

public final class ActionManager {
    final Map<InventoryAction, Action> inventoryActionEventMap = new EnumMap<>(InventoryAction.class);

    public Optional<Action> getAction(final InventoryAction action) {
        return Optional.ofNullable(inventoryActionEventMap.get(action));
    }

    public void addAction(final InventoryAction action, final Action event) {
        inventoryActionEventMap.put(action, event);
    }

    private void disableAction(final Map<InventoryAction, Boolean> actionMap) {
        final Consumer<InventoryClickEvent> inventoryClickEvent = event -> {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        };

        actionMap.forEach((key, value) -> addAction(key, new Action(inventoryClickEvent, value)));
    }

    public void disableAllAction(final boolean disableActionInPlayerInventory) {
        final Map<InventoryAction, Boolean> actionMap = new HashMap<>();
        for (final InventoryAction action : InventoryAction.values()) {
            actionMap.put(action, !disableActionInPlayerInventory);
        }

        actionMap.put(InventoryAction.COLLECT_TO_CURSOR, false);
        actionMap.put(InventoryAction.MOVE_TO_OTHER_INVENTORY, false);
        disableAction(actionMap);
    }

    public void disableTakeItem() {
        final EnumSet<InventoryAction> takeItemActionSet = EnumSet.of(
                InventoryAction.PICKUP_ONE,
                InventoryAction.PICKUP_SOME,
                InventoryAction.PICKUP_HALF,
                InventoryAction.PICKUP_ALL,
                InventoryAction.COLLECT_TO_CURSOR,
                InventoryAction.HOTBAR_SWAP,
                InventoryAction.MOVE_TO_OTHER_INVENTORY,
                InventoryAction.DROP_ALL_SLOT,
                InventoryAction.DROP_ONE_SLOT,
                InventoryAction.DROP_ALL_CURSOR,
                InventoryAction.DROP_ONE_CURSOR);

        final Map<InventoryAction, Boolean> actionMap = new HashMap<>();
        for (final InventoryAction action : takeItemActionSet) {
            actionMap.put(action, true);
        }

        disableAction(actionMap);
    }

    public void disablePlaceItem() {
        final EnumSet<InventoryAction> placeItemActionSet = EnumSet.of(
                InventoryAction.PLACE_ONE,
                InventoryAction.PLACE_SOME,
                InventoryAction.PLACE_ALL);

        final Map<InventoryAction, Boolean> actionMap = new HashMap<>();
        for (final InventoryAction action : placeItemActionSet) {
            actionMap.put(action, true);
        }

        disableAction(actionMap);
    }
}
