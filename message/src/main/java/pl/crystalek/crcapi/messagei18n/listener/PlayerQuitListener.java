package pl.crystalek.crcapi.messagei18n.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.crystalek.crcapi.messagei18n.user.UserCache;

public final class PlayerQuitListener implements Listener {

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        UserCache.removeUser(event.getPlayer());
    }
}
