package pl.crystalek.crcapi.gui.action;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.event.inventory.InventoryClickEvent;

@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ClickAction {
    final Action<InventoryClickEvent> clickAction;
    /**
     * A boolean flag indicating whether the action can be performed in the player's inventory.
     */
    boolean actionAllowedInPlayerInventory = true;

    /**
     * Returns the action to be executed when the player clicks on the inventory slot.
     *
     * @return the action to be executed when the player clicks on the inventory slot
     */
    public Action<InventoryClickEvent> getClickAction() {
        return clickAction;
    }

    /**
     * Returns a boolean flag indicating whether the action can be performed in the player's inventory.
     *
     * @return a boolean flag indicating whether the action can be performed in the player's inventory
     */
    public boolean isActionAllowedInPlayerInventory() {
        return actionAllowedInPlayerInventory;
    }

    /**
     * Sets the boolean flag indicating whether the action can be performed in the player's inventory.
     *
     * @param actionAllowedInPlayerInventory a boolean flag indicating whether the action can be performed in the player's inventory
     */
    public void setActionAllowedInPlayerInventory(final boolean actionAllowedInPlayerInventory) {
        this.actionAllowedInPlayerInventory = actionAllowedInPlayerInventory;
    }
}