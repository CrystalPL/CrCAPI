package pl.crystalek.crcapi.message.api;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import pl.crystalek.crcapi.message.api.type.MessageType;

import java.util.Map;

public interface Message {

    void sendMessage(final Audience sender, final Map<String, Object> replacements);

    void sendMessageComponent(final Audience sender, final Map<String, Component> replacements);

    Component getComponent();

    MessageType getMessageType();
}
