package pl.crystalek.crcapi.message.impl.listener;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.crystalek.crcapi.message.impl.user.UserCache;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent event) {
        UserCache.loadLocale(event.getPlayer());
    }
}
