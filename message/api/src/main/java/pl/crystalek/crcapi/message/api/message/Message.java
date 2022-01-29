package pl.crystalek.crcapi.message.api.message;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;

import java.util.Map;

public interface Message {

    void sendMessage(final Audience sender, final Map<String, Object> replacements);

    void sendMessageComponent(final Audience sender, final Map<String, Component> replacements);
}
