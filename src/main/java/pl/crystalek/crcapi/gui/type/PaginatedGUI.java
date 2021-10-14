package pl.crystalek.crcapi.gui.type;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.bukkit.inventory.ItemStack;
import pl.crystalek.crcapi.gui.item.GUIItem;

import java.util.*;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class PaginatedGUI extends BaseGUI {
    final Map<Integer, GUIItem> staticGuiElement = new HashMap<>();
    final Map<Integer, GUIItem> actualPageItemMap = new HashMap<>();
    @Getter
    final List<GUIItem> guiItemList = new LinkedList<>();
    @Getter
    int pageNumber;
    int actualPage = 1;
    int maxItemOnPage;

    public PaginatedGUI(final String title, final int rows) {
        super(title, rows);
    }

    @Override
    public void removeItem(final GUIItem guiItem) {
        removeItem(guiItem, false);
    }

    @Override
    public void removeItem(final int slot) {
        removeItem(slot, false);
    }

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

    @Override
    public void setItem(final int slot, final GUIItem guiItem) {
        staticGuiElement.put(slot, guiItem);
    }

    //zwraca item o podanym slocie z aktualnej strony
    @Override
    public Optional<GUIItem> getItem(final int slot) {
        return Optional.ofNullable(actualPageItemMap.get(slot));
    }

    //zwraca numer slotu, gdzie jest podany przedmiot z aktualnej strony
    @Override
    public Optional<Integer> getSlot(final GUIItem guiItem) {
        for (final Map.Entry<Integer, GUIItem> entry : actualPageItemMap.entrySet()) {
            if (entry.getValue().equals(guiItem)) {
                return Optional.of(entry.getKey());
            }
        }

        return Optional.empty();
    }

    //update item from all gui items list
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

    //update item on actual page
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

    @Override
    public void build() {
        maxItemOnPage = slotCount - staticGuiElement.size();

        pageNumber = (int) Math.ceil((double) guiItemList.size() / maxItemOnPage);

        if (actualPage > pageNumber) {
            actualPage = pageNumber;
        }

        goTo(actualPage);
    }

    @Override
    public void addItem(final GUIItem guiItem) {
        guiItemList.add(guiItem);
    }

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

    public void next() {
        if (actualPage + 1 > pageNumber) {
            return;
        }

        actualPage++;
        goTo(actualPage);
    }

    public void previous() {
        if (actualPage - 1 <= 0) {
            return;
        }

        actualPage--;
        goTo(actualPage);
    }
}
