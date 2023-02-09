package pl.crystalek.crcapi.message.api.message;

import net.kyori.adventure.text.Component;

public interface IChatMessage {

    /**
     * Returns the chat message as a Component object.
     *
     * @return the message as a Component object
     */
    Component getChatComponent();
}
