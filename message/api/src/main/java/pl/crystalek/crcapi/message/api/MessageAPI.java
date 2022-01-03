package pl.crystalek.crcapi.message.api;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import pl.crystalek.crcapi.message.api.type.MessageType;

import java.util.Map;
import java.util.Optional;

public interface MessageAPI {

    boolean init();

    Optional<Component> getComponent(final String messagePath, final CommandSender messageReceiver, final MessageType messageType);

    void sendMessage(final String messagePath, final CommandSender messageReceiver);

    void sendMessage(final String messagePath, final CommandSender messageReceiver, final Map<String, Object> replacements);

    void sendMessageComponent(final String messagePath, final CommandSender messageReceiver, final Map<String, Component> replacements);

    void sendMessage(final Component component, final CommandSender messageReceiver, final Map<String, Component> replacements);

    void sendMessageComponent(final Component component, final CommandSender messageReceiver, final Map<String, Object> replacements);

    void broadcastComponent(final String messagePath, final Map<String, Component> replacements);

    void broadcast(final String messagePath, final Map<String, Object> replacements);

    void broadcast(final String messagePath);
}
