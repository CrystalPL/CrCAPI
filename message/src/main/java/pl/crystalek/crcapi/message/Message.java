package pl.crystalek.crcapi.message;

import net.kyori.adventure.audience.Audience;

import java.util.Map;

public interface Message {
    void sendMessage(final Audience sender, final Map<String, Object> replacements);
}
