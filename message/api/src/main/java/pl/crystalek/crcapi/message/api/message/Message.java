package pl.crystalek.crcapi.message.api.message;

import net.kyori.adventure.audience.Audience;
import pl.crystalek.crcapi.message.api.replacement.Replacement;

public interface Message {

    /**
     * Sends a message to the specified audience, with optional text replacements.
     *
     * @param sender       The audience to which the message will be sent
     * @param replacements The replacements to be made in the message text, as a varargs array of {@link Replacement} objects.
     */
    void sendMessage(final Audience sender, final Replacement... replacements);
}
