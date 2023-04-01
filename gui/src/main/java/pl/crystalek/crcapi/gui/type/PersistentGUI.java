package pl.crystalek.crcapi.gui.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import pl.crystalek.crcapi.gui.item.GUIItem;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Getter
public final class PersistentGUI extends BaseGUI {
    Map<Integer, GUIItem> slotListMap = new LinkedHashMap<>();

    public PersistentGUI(final String title, final int rows) {
        super(title, rows);
    }

    /**
     * Removes the specified {@link GUIItem} from the gui
     *
     * @param guiItem the GUI item to be removed
     */
    @Override
    public void removeItem(final GUIItem guiItem) {
        final Iterator<Map.Entry<Integer, GUIItem>> iterator = slotListMap.entrySet().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getValue().equals(guiItem)) {
                iterator.remove();
                inventory.remove(guiItem.getItem());
                break;
            }
        }
    }

    /**
     * Removes the {@link GUIItem} from the specified slot in the inventory.
     *
     * @param slot the slot number of the GUI item to be removed
     */
    @Override
    public void removeItem(final int slot) {
        final GUIItem removedGuiItem = slotListMap.remove(slot);
        if (removedGuiItem != null) {
            inventory.remove(removedGuiItem.getItem());
        }
    }


    /**
     * Fills the GUI with a filler item if there are empty slots.
     */
    @Override
    public void fill() {
        if (slotCount == slotListMap.size()) {
            return;
        }

        for (int slot = 1; slot <= slotCount; slot++) {
            if (slotListMap.get(slot) != null) {
                continue;
            }

            slotListMap.put(slot, filler);
        }
    }

    /**
     * Sets the specified GUI item to the specified slot in the inventory.
     *
     * @param slot    the slot number to set the GUI item
     * @param guiItem the GUI item to be set
     */
    @Override
    public void setItem(final int slot, final GUIItem guiItem) {
        slotListMap.put(slot, guiItem);
    }

    /**
     * Returns the Optional of the GUI item at the specified slot in the inventory.
     *
     * @param slot the slot number of the GUI item to be returned
     * @return the Optional of the GUI item at the specified slot in the inventory
     */
    @Override
    public Optional<GUIItem> getItem(final int slot) {
        return Optional.ofNullable(slotListMap.get(slot));
    }

    /**
     * Returns the Optional of the slot number where the specified GUI item is located in the inventory.
     *
     * @param guiItem the GUI item to find the slot number
     * @return the Optional of the slot number where the specified GUI item is located in the inventory
     */
    @Override
    public Optional<Integer> getSlot(final GUIItem guiItem) {
        for (final Map.Entry<Integer, GUIItem> entry : slotListMap.entrySet()) {
            if (entry.getValue().equals(guiItem)) {
                return Optional.of(entry.getKey());
            }
        }

        return Optional.empty();
    }

    /**
     * Updates the persistent GUI item in the specified slot.
     *
     * @param slot    the slot to update
     * @param guiItem the new GUI item to set in the slot
     */
    @Override
    public void update(final int slot, final GUIItem guiItem) {
        inventory.setItem(slot - 1, guiItem.getItem());
        slotListMap.put(slot, guiItem);
    }

    /**
     * Builds the persistent GUI by setting the GUI items in their respective slots in the inventory.
     */
    @Override
    public void build() {
        for (final Map.Entry<Integer, GUIItem> entry : slotListMap.entrySet()) {
            inventory.setItem(entry.getKey() - 1, entry.getValue().getItem());
        }
    }

    /**
     * Adds a GUI item to the persistent GUI by finding the first empty slot in the slot list map.
     *
     * @param guiItem the GUI item to add
     */
    @Override
    public void addItem(final GUIItem guiItem) {
        for (int slot = 1; slot <= slotCount; slot++) {
            if (slotListMap.get(slot) != null) {
                continue;
            }

            slotListMap.put(slot, guiItem);
            break;
        }
    }
}
