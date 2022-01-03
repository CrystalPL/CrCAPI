package pl.crystalek.crcapi.message.impl.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import pl.crystalek.crcapi.message.impl.user.UserCache;

public final class PlayerQuitListener implements Listener {

    @EventHandler
    public void onQuit(final PlayerQuitEvent event) {
        UserCache.removeUser(event.getPlayer());
    }
}
