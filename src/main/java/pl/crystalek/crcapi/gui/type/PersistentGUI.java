package pl.crystalek.crcapi.gui.type;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import pl.crystalek.crcapi.gui.item.GUIItem;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class PersistentGUI extends BaseGUI {
    Map<Integer, GUIItem> slotListMap = new LinkedHashMap<>();

    public PersistentGUI(final String title, final int rows) {
        super(title, rows);
    }

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

    @Override
    public void removeItem(final int slot) {
        final GUIItem removedGuiItem = slotListMap.remove(slot);
        if (removedGuiItem != null) {
            inventory.remove(removedGuiItem.getItem());
        }
    }

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

    @Override
    public void setItem(final int slot, final GUIItem guiItem) {
        slotListMap.put(slot, guiItem);
    }

    @Override
    public Optional<GUIItem> getItem(final int slot) {
        return Optional.ofNullable(slotListMap.get(slot));
    }

    @Override
    public Optional<Integer> getSlot(final GUIItem guiItem) {
        for (final Map.Entry<Integer, GUIItem> entry : slotListMap.entrySet()) {
            if (entry.getValue().equals(guiItem)) {
                return Optional.of(entry.getKey());
            }
        }

        return Optional.empty();
    }

    @Override
    public void update(final int slot, final GUIItem guiItem) {
        inventory.setItem(slot - 1, guiItem.getItem());
        slotListMap.put(slot, guiItem);
    }

    @Override
    public void build() {
        for (final Map.Entry<Integer, GUIItem> entry : slotListMap.entrySet()) {
            inventory.setItem(entry.getKey() - 1, entry.getValue().getItem());
        }
    }

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
