package pl.crystalek.crcapi.messagei18n;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.message.AbstractMessageAPI;
import pl.crystalek.crcapi.message.Message;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class LocalizedMessageAPI extends AbstractMessageAPI {
    Map<UUID, Map<String, List<Message>>>

    public LocalizedMessageAPI(final JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void sendMessage(final String messagePath, final Audience audience, final Map<String, Object> replacements) {

    }

    @Override
    public void sendMessageComponent(final String messagePath, final Audience audience, final Map<String, Component> replacements) {

    }

    @Override
    public Optional<Component> getComponent(final String messagePath, final Class<? extends Message> clazz) {

    }

    public void init() {

    }
}
