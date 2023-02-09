package pl.crystalek.crcapi.message.impl.manager.provider;

import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.message.api.MessageAPI;
import pl.crystalek.crcapi.message.api.MessageAPIProvider;
import pl.crystalek.crcapi.message.impl.manager.MessageAPIImpl;

public final class MessageAPIProviderImpl implements MessageAPIProvider {

    @Override
    public MessageAPI getMessage(final JavaPlugin plugin) {
        return new MessageAPIImpl(plugin);
    }
}
