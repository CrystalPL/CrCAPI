package pl.crystalek.crcapi.gui.action;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.function.Consumer;

@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public final class Action {
    final Consumer<InventoryClickEvent> action;
    boolean actionAllowedInPlayerInventory;
}
