package pl.crystalek.crcapi.gui.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.Event;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import pl.crystalek.crcapi.gui.action.ActionManager;
import pl.crystalek.crcapi.gui.item.GUIItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@FieldDefaults(level = AccessLevel.PACKAGE)
@Getter
@Setter
public abstract class BaseGUI implements InventoryHolder {
    final int rows;
    Inventory inventory;
    boolean placeItem;
    boolean takeItem;
    boolean swapItem;
    int slotCount;
    ActionManager actionManager = new ActionManager();
    Consumer<InventoryDragEvent> inventoryDragEvent;
    Consumer<InventoryCloseEvent> inventoryCloseEvent;
    Consumer<InventoryOpenEvent> inventoryOpenEvent;
    GUIItem filler;

    public BaseGUI(final String title, final int rows) {
        this.rows = rows;
        this.inventory = Bukkit.createInventory(this, rows * 9, title);
        this.slotCount = rows * 9;
    }

    public void close(final HumanEntity player) {
        player.closeInventory();
    }

    public void open(final HumanEntity player) {
        player.openInventory(inventory);
    }

    public void disableAllAction() {
        actionManager.disableAllAction(false);
        inventoryDragEvent = event -> {
            event.setCancelled(true);
            event.setResult(Event.Result.DENY);
        };
    }

    public void updateTitle(final String title) {
        final List<HumanEntity> viewers = new ArrayList<>(inventory.getViewers());
        this.inventory = Bukkit.createInventory(this, inventory.getSize(), title);
        build();

        viewers.forEach(this::open);
    }

    public void addItems(final Collection<GUIItem> guiItems) {
        guiItems.forEach(this::addItem);
    }

    public void update() {
        inventory.clear();
        build();
    }

    public void addItem(final ItemStack item) {
        addItem(new GUIItem(item));
    }

    public void setItem(final int slot, final ItemStack item) {
        setItem(slot, new GUIItem(item));
    }

    public abstract void removeItem(final GUIItem guiItem);

    public abstract void removeItem(final int slot);

    public abstract void fill();

    public abstract void setItem(final int slot, final GUIItem guiItem);

    public abstract Optional<GUIItem> getItem(final int slot);

    public abstract Optional<Integer> getSlot(final GUIItem guiItem);

    public abstract void update(final int slot, final GUIItem guiItem);

    public abstract void build();

    public abstract void addItem(final GUIItem guiItem);
}
