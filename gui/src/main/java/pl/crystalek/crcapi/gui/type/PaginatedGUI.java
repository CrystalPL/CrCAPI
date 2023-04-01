package pl.crystalek.crcapi.gui.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.inventory.ItemStack;
import pl.crystalek.crcapi.gui.item.GUIItem;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class PaginatedGUI extends BaseGUI {
    final Map<Integer, GUIItem> staticGuiElement = new HashMap<>();
    final Map<Integer, GUIItem> actualPageItemMap = new HashMap<>();
    @Getter final List<GUIItem> guiItemList = new LinkedList<>();
    @Getter
    int pageNumber;
    int actualPage = 1;
    int maxItemOnPage;

    public PaginatedGUI(final String title, final int rows) {
        super(title, rows);
    }

    /**
     * Removes the specified GUIItem from the GUI.
     *
     * @param guiItem the GUIItem to be removed
     */
    @Override
    public void removeItem(final GUIItem guiItem) {
        removeItem(guiItem, false);
    }

    /**
     * Removes the GUIItem at the specified slot from the GUI.
     *
     * @param slot the slot of the GUIItem to be removed
     */
    @Override
    public void removeItem(final int slot) {
        removeItem(slot, false);
    }

    /**
     * Removes the specified GUIItem from the GUI and rebuilds it if specified.
     *
     * @param guiItem the GUIItem to be removed
     * @param reBuild whether to rebuild the GUI after removing the item
     */
    public void removeItem(final GUIItem guiItem, final boolean reBuild) {
        guiItemList.remove(guiItem);
        inventory.remove(guiItem.getItem());

        final Iterator<Map.Entry<Integer, GUIItem>> iterator = actualPageItemMap.entrySet().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getValue().equals(guiItem)) {
                iterator.remove();
                break;
            }
        }

        if (reBuild) {
            build();
        }
    }

    /**
     * Removes the GUIItem at the specified slot from the GUI and rebuilds it if specified.
     *
     * @param slot    the slot of the GUIItem to be removed
     * @param reBuild whether to rebuild the GUI after removing the item
     */
    public void removeItem(final int slot, final boolean reBuild) {
        final GUIItem removedGuiItem = actualPageItemMap.remove(slot);
        if (removedGuiItem != null) {
            guiItemList.remove(removedGuiItem);
            inventory.remove(removedGuiItem.getItem());
        }

        if (reBuild) {
            build();
        }
    }

    /**
     * Fills the empty slots of the GUI with the filler GUIItem.
     */
    @Override
    public void fill() {
        if (slotCount == actualPageItemMap.size()) {
            return;
        }

        for (int slot = 1; slot <= slotCount; slot++) {
            if (actualPageItemMap.get(slot) != null) {
                continue;
            }

            actualPageItemMap.put(slot, filler);
        }
    }

    /**
     * Sets the {@link GUIItem} for the given slot in the staticGuiElement map.
     *
     * @param slot    The slot to set the {@link GUIItem}.
     * @param guiItem The {@link GUIItem} to set.
     */
    @Override
    public void setItem(final int slot, final GUIItem guiItem) {
        staticGuiElement.put(slot, guiItem);
    }

    /**
     * Returns the {@link GUIItem} at the given slot from the current page.
     *
     * @param slot The slot to get the {@link GUIItem}.
     * @return An {@link Optional} containing the {@link GUIItem} if it exists, or an empty {@link Optional} if it does not.
     */
    @Override
    public Optional<GUIItem> getItem(final int slot) {
        return Optional.ofNullable(actualPageItemMap.get(slot));
    }

    /**
     * Returns the slot number where the given {@link GUIItem} is located on the current page.
     *
     * @param guiItem The {@link GUIItem} to search for.
     * @return An {@link Optional} containing the slot number if the {@link GUIItem} is found, or an empty {@link Optional} if it is not found.
     */
    @Override
    public Optional<Integer> getSlot(final GUIItem guiItem) {
        for (final Map.Entry<Integer, GUIItem> entry : actualPageItemMap.entrySet()) {
            if (entry.getValue().equals(guiItem)) {
                return Optional.of(entry.getKey());
            }
        }

        return Optional.empty();
    }

    /**
     * Updates an existing {@link GUIItem} with a new one, both in the {@link #guiItemList} and the {@link #actualPageItemMap}.
     *
     * @param oldItem The old {@link GUIItem} to replace.
     * @param newItem The new {@link GUIItem} to set in place of the old one.
     */
    public void update(final GUIItem oldItem, final GUIItem newItem) {
        for (int i = 0; i < guiItemList.size(); i++) {
            final GUIItem guiItem = guiItemList.get(i);
            if (guiItem.equals(oldItem)) {
                guiItemList.set(i, newItem);
            }
        }

        for (final Map.Entry<Integer, GUIItem> entry : actualPageItemMap.entrySet()) {
            if (entry.getValue().equals(oldItem)) {
                actualPageItemMap.put(entry.getKey(), newItem);
            }
        }
    }

    /**
     * Updates an existing {@link GUIItem} at the given slot.
     *
     * @param slot    the slot of the item to update
     * @param guiItem the {@link GUIItem} to update
     */
    @Override
    public void update(final int slot, final GUIItem guiItem) {
        final ItemStack item = inventory.getItem(slot + 1);
        for (int i = 0; i < guiItemList.size(); i++) {
            if (guiItemList.get(i).getItem().isSimilar(item)) {
                guiItemList.set(i, guiItem);
            }
        }

        inventory.setItem(slot - 1, guiItem.getItem());
        actualPageItemMap.put(slot - 1, guiItem);
    }

    /**
     * Builds the inventory GUI with the current list of {@link GUIItem}s and displays the current page.
     */
    @Override
    public void build() {
        maxItemOnPage = slotCount - staticGuiElement.size();

        pageNumber = (int) Math.ceil((double) guiItemList.size() / maxItemOnPage);

        if (actualPage > pageNumber) {
            actualPage = pageNumber;
        }

        goTo(actualPage);
    }

    /**
     * Adds a new {@link GUIItem} to the inventory GUI.
     *
     * @param guiItem the {@link GUIItem} to add
     */
    @Override
    public void addItem(final GUIItem guiItem) {
        guiItemList.add(guiItem);
    }

    /**
     * Navigates to the specified page in the inventory GUI and displays the corresponding {@link GUIItem}s.
     *
     * @param page the page number to navigate to
     */
    public void goTo(final int page) {
        inventory.clear();
        actualPageItemMap.clear();

        staticGuiElement.forEach((key, value) -> {
            actualPageItemMap.put(key, value);
            inventory.setItem(key - 1, value.getItem());
        });

        final int startIndex = (page - 1) * maxItemOnPage;
        final int lastIndex = Math.min(page * maxItemOnPage, guiItemList.size()) - 1;
        for (int i = 0; i <= lastIndex - startIndex; i++) {
            for (int slot = 1; slot <= slotCount; slot++) {
                if (actualPageItemMap.get(slot) != null) {
                    continue;
                }

                final GUIItem guiItem = guiItemList.get(startIndex + i);
                actualPageItemMap.put(slot, guiItem);
                inventory.setItem(slot - 1, guiItem.getItem());
                break;
            }
        }
    }

    /**
     * Navigates to the next page in the inventory GUI and displays the corresponding {@link GUIItem}s.
     */
    public void next() {
        if (actualPage + 1 > pageNumber) {
            return;
        }

        actualPage++;
        goTo(actualPage);
    }

    /**
     * Navigates to the previous page in the inventory GUI and displays the corresponding {@link GUIItem}s.
     */
    public void previous() {
        if (actualPage - 1 <= 0) {
            return;
        }

        actualPage--;
        goTo(actualPage);
    }
}
