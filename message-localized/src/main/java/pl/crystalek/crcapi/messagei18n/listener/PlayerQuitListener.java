package pl.crystalek.crcapi.messagei18n.listener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.messagei18n.user.UserCache;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class PlayerQuitListener implements Listener {
    JavaPlugin plugin;

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        UserCache.removeUser();
    }
}
