package pl.crystalek.crcapi.gui.action;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Map;
import java.util.Optional;

@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ClickActionManager implements Action<InventoryClickEvent> {
    final static EnumSet<InventoryAction> takeItemActions = EnumSet.of(InventoryAction.PICKUP_ONE, InventoryAction.PICKUP_SOME, InventoryAction.PICKUP_HALF, InventoryAction.PICKUP_ALL, InventoryAction.COLLECT_TO_CURSOR, InventoryAction.HOTBAR_SWAP, InventoryAction.MOVE_TO_OTHER_INVENTORY, InventoryAction.DROP_ALL_SLOT, InventoryAction.DROP_ONE_SLOT, InventoryAction.DROP_ALL_CURSOR, InventoryAction.DROP_ONE_CURSOR);
    final static EnumSet<InventoryAction> placeItemActions = EnumSet.of(InventoryAction.PLACE_ONE, InventoryAction.PLACE_SOME, InventoryAction.PLACE_ALL);
    final Map<InventoryAction, ClickAction> clickActionMap = new EnumMap<>(InventoryAction.class);
    boolean placeItem;
    boolean takeItem;
    ClickAction disableAllAction;

    public ClickActionManager(final boolean placeItem, final boolean takeItem) {
        this.placeItem = placeItem;
        this.takeItem = takeItem;
    }

    /**
     * Checks whether all actions are disabled.
     *
     * @return {@code true} if all actions are disabled, {@code false} otherwise.
     */
    public boolean isAllActionsIsDisabled() {
        return disableAllAction != null;
    }

    /**
     * Disables all click actions, preventing any interaction with the inventory. If {@code disableInPlayerInventory} is
     * true, this will also disable all actions in the player's inventory.
     *
     * @param disableInPlayerInventory a boolean indicating whether to disable all actions in the player's inventory as well.
     */
    public void disableAllActions(final boolean disableInPlayerInventory) {
        disableAllAction = new ClickAction(event -> {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        }, disableInPlayerInventory);
    }

    /**
     * Executes a click action based.
     */
    @Override
    public void execute(final InventoryClickEvent event) {
        final InventoryAction action = event.getAction();
        if (clickActionMap.containsKey(action)) {
            final ClickAction clickAction = clickActionMap.get(action);
            if (clickAction.isActionAllowedInPlayerInventory() && event.getClickedInventory().getType() == InventoryType.PLAYER) {
                System.out.println("halo");
                return;
            }

            System.out.println("elo");
            clickAction.getClickAction().execute(event);
        }

        if (takeItem && takeItemActions.contains(action)) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        } else if (placeItem && placeItemActions.contains(action)) {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        } else if (disableAllAction != null) {
            if (disableAllAction.isActionAllowedInPlayerInventory() && event.getClickedInventory().getType() == InventoryType.PLAYER) {
                return;
            }

            disableAllAction.getClickAction().execute(event);
        }
    }

    /**
     * Sets the given action for the specified inventory action.
     *
     * @param inventoryAction the inventory action to set the action for
     * @param action          the action to set
     */
    public void setSlotAction(final InventoryAction inventoryAction, final ClickAction action) {
        clickActionMap.put(inventoryAction, action);
    }

    /**
     * @param inventoryAction the InventoryAction to retrieve the associated ClickAction for.
     * @return an Optional containing the ClickAction associated with the specified InventoryAction.
     */
    public Optional<ClickAction> getSlotAction(final InventoryAction inventoryAction) {
        return Optional.ofNullable(clickActionMap.get(inventoryAction));
    }

    /**
     * Disables the specified InventoryAction by setting a ClickAction that cancels the event for the specified action.
     *
     * @param inventoryAction          the InventoryAction to disable
     * @param disableInPlayerInventory if true, disables the action in the player's inventory only; if false, disables the action only in main inventory
     */
    public void disableAction(final InventoryAction inventoryAction, final boolean disableInPlayerInventory) {
        final Action<InventoryClickEvent> inventoryClickEvent = event -> {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        };

        setSlotAction(inventoryAction, new ClickAction(inventoryClickEvent, disableInPlayerInventory));
    }

    /**
     * Returns whether items can be placed in an inventory.
     *
     * @return A boolean indicating whether items can be placed in an inventory.
     */
    public boolean isPlaceItem() {
        return placeItem;
    }

    /**
     * Sets whether the player is allowed to place items in the inventory.
     *
     * @param placeItem true if the player is allowed to place items, false otherwise.
     */
    public void setPlaceItem(final boolean placeItem) {
        this.placeItem = placeItem;
    }

    /**
     * Checks if the player is allowed to take items from the inventory.
     *
     * @return true if the player is allowed to take items, false otherwise.
     */
    public boolean isTakeItem() {
        return takeItem;
    }

    /**
     * Sets whether the player is allowed to take items from the inventory.
     *
     * @param takeItem true if the player is allowed to take items, false otherwise.
     */
    public void setTakeItem(final boolean takeItem) {
        this.takeItem = takeItem;
    }
}
