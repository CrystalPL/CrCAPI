package pl.crystalek.crcapi.gui.item;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.inventory.ItemStack;
import pl.crystalek.crcapi.gui.action.ClickActionManager;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class GUIItem {
    ItemStack item;
    ClickActionManager clickActionManager;

    public GUIItem(final ItemStack item) {
        this(item, new ClickActionManager());
    }

    public GUIItem(final ItemStack item, final ClickActionManager clickActionManager) {
        this.item = item;
        this.clickActionManager = clickActionManager;
    }

    /**
     * Gets the item from this GUIItem.
     *
     * @return the item from this GUIItem
     */
    public ItemStack getItem() {
        return item;
    }

    /**
     * Gets the ClickActionManager for this GUIItem.
     *
     * @return the ClickActionManager for this GUIItem
     */
    public ClickActionManager getClickActionManager() {
        return clickActionManager;
    }
}
