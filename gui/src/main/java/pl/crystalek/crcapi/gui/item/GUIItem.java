package pl.crystalek.crcapi.gui.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.inventory.ItemStack;
import pl.crystalek.crcapi.gui.action.ActionManager;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Getter
public final class GUIItem {
    ItemStack item;
    ActionManager actionManager = new ActionManager();
}
