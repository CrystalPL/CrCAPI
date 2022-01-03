package pl.crystalek.crcapi.message.impl.manager.provider;

import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.message.api.MessageAPI;
import pl.crystalek.crcapi.message.api.MessageAPIProvider;
import pl.crystalek.crcapi.message.impl.manager.LocalizedMessageAPI;
import pl.crystalek.crcapi.message.impl.manager.SingleMessageAPI;

public final class MessageAPIProviderImpl implements MessageAPIProvider {

    @Override
    public MessageAPI getSingleMessage(final JavaPlugin plugin) {
        return new SingleMessageAPI(plugin);
    }

    @Override
    public MessageAPI getLocalizedMessage(final JavaPlugin plugin) {
        return new LocalizedMessageAPI(plugin);
    }
}
