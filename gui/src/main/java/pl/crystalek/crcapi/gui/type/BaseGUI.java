package pl.crystalek.crcapi.gui.type;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pl.crystalek.crcapi.gui.action.ActionManager;
import pl.crystalek.crcapi.gui.item.GUIItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PACKAGE)
public abstract class BaseGUI implements InventoryHolder {
    final int rows;
    final int slotCount;
    final ActionManager actionManager = new ActionManager();

    Inventory inventory;
    GUIItem filler;

    public BaseGUI(final String title, final int rows) {
        this.rows = rows;
        this.inventory = Bukkit.createInventory(this, rows * 9, title);
        this.slotCount = rows * 9;
    }

    /**
     * Returns the number of rows in the GUI.
     *
     * @return the number of rows in the GUI.
     */
    public int getRows() {
        return rows;
    }

    /**
     * Returns the total number of slots in the GUI.
     *
     * @return the total number of slots in the GUI.
     */
    public int getSlotCount() {
        return slotCount;
    }

    /**
     * Returns the ActionManager instance associated with this GUI.
     *
     * @return the ActionManager instance associated with this GUI.
     */
    public ActionManager getActionManager() {
        return actionManager;
    }

    /**
     * Returns the GUIItem instance used as filler in the GUI.
     *
     * @return the GUIItem instance used as filler in the GUI.
     */
    public GUIItem getFiller() {
        return filler;
    }

    /**
     * Sets the GUIItem to be used as filler in the GUI.
     *
     * @param filler the GUIItem to be used as filler in the GUI.
     */
    public void setFiller(final GUIItem filler) {
        this.filler = filler;
    }

    /**
     * Close the inventory for the given HumanEntity.
     *
     * @param player the HumanEntity for whom the inventory is to be closed.
     */
    public void close(final HumanEntity player) {
        player.closeInventory();
    }

    /**
     * Open the inventory for the given HumanEntity.
     *
     * @param player the HumanEntity for whom the inventory is to be opened.
     */
    public void open(final HumanEntity player) {
        player.openInventory(inventory);
    }

    /**
     * Update the title of the GUI inventory.
     *
     * @param title the new title for the GUI inventory.
     */
    public void updateTitle(final String title) {
        final List<HumanEntity> viewers = new ArrayList<>(inventory.getViewers());
        this.inventory = Bukkit.createInventory(this, inventory.getSize(), title);
        build();

        viewers.forEach(this::open);
    }

    /**
     * Add a collection of GUIItem instances to the inventory.
     *
     * @param guiItems the collection of GUIItem instances to be added.
     */
    public void addItems(final Collection<GUIItem> guiItems) {
        guiItems.forEach(this::addItem);
    }

    /**
     * Rebuild the gui.
     */
    public void update() {
        inventory.clear();
        build();
    }

    /**
     * Adds an ItemStack to the inventory as a GUIItem instance.
     *
     * @param item The ItemStack to be added.
     */
    public void addItem(final ItemStack item) {
        addItem(new GUIItem(item));
    }

    /**
     * Sets an ItemStack in the inventory at the specified slot as a GUIItem instance.
     *
     * @param slot The slot at which to set the ItemStack.
     * @param item The ItemStack to be set.
     */
    public void setItem(final int slot, final ItemStack item) {
        setItem(slot, new GUIItem(item));
    }

    /**
     * Removes the specified GUIItem instance from the inventory.
     *
     * @param guiItem The GUIItem instance to be removed.
     */
    public abstract void removeItem(final GUIItem guiItem);

    /**
     * Removes the GUIItem instance at the specified slot from the inventory.
     *
     * @param slot The slot from which to remove the GUIItem instance.
     */
    public abstract void removeItem(final int slot);

    /**
     * Fills the inventory with a filler GUIItem instance.
     */
    public abstract void fill();

    /**
     * Sets a GUIItem instance in the inventory at the specified slot.
     *
     * @param slot    The slot at which to set the GUIItem instance.
     * @param guiItem The GUIItem instance to be set.
     */
    public abstract void setItem(final int slot, final GUIItem guiItem);

    /**
     * Returns an Optional containing the GUIItem instance at the specified slot, if present.
     *
     * @param slot The slot from which to get the GUIItem instance.
     * @return An Optional containing the GUIItem instance at the specified slot, if present.
     */
    public abstract Optional<GUIItem> getItem(final int slot);

    /**
     * Returns an Optional containing the slot of the specified GUIItem instance, if present.
     *
     * @param guiItem The GUIItem instance for which to get the slot.
     * @return An Optional containing the slot of the specified GUIItem instance, if present.
     */
    public abstract Optional<Integer> getSlot(final GUIItem guiItem);

    /**
     * Updates the GUIItem instance at the specified slot in the inventory.
     *
     * @param slot    The slot at which to update the GUIItem instance.
     * @param guiItem The new GUIItem instance to be set.
     */
    public abstract void update(final int slot, final GUIItem guiItem);

    /**
     * Builds the inventory with its GUIItem instances.
     */
    public abstract void build();

    /**
     * Adds a GUIItem instance to the inventory.
     *
     * @param guiItem The GUIItem instance to be added.
     */
    public abstract void addItem(final GUIItem guiItem);

    @Override
    public Inventory getInventory() {
        return inventory;
    }
}
