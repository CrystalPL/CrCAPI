package pl.crystalek.crcapi.message.api;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crcapi.message.api.replacement.Replacement;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface MessageAPI {

    /**
     * Initializes the Message API.
     *
     * @return <code>true</code> if the initialization was successful, <code>false</code> otherwise.
     */
    boolean init();

    /**
     * Gets the specified message for the given message receiver.
     *
     * @param messagePath     The path of the message to retrieve.
     * @param messageReceiver The receiver of the message.
     * @param messageClass    The class of the message.
     * @param <T>             The type of the message.
     * @return An {@link Optional} containing the message, or an empty Optional if the message does not exist.
     */
    <T> Optional<T> getMessage(final String messagePath, final CommandSender messageReceiver, final Class<T> messageClass);

    /**
     * Sends the specified message to the given message receiver.
     *
     * @param messagePath     The path of the message to send.
     * @param messageReceiver The receiver of the message.
     * @param replacements    The replacements to apply to the message.
     */
    void sendMessage(final String messagePath, final String messageReceiver, final Replacement... replacements);

    /**
     * Sends the specified message component to the given message receiver.
     *
     * @param component       The message component to send.
     * @param messageReceiver The receiver of the message.
     * @param replacements    The replacements to apply to the message.
     */
    void sendMessage(final Component component, final CommandSender messageReceiver, final Replacement... replacements);

    /**
     * Sends the specified message component to the given message receiver.
     *
     * @param component       The message component to send.
     * @param messageReceiver The receiver of the message.
     * @param replacements    The replacements to apply to the message.
     */
    void sendMessage(final Component component, final String messageReceiver, final Replacement... replacements);

    /**
     * Sends the specified message to the given message receiver.
     *
     * @param messagePath     The path of the message to send.
     * @param messageReceiver The receiver of the message.
     * @param replacements    The replacements to apply to the message.
     */
    void sendMessage(final String messagePath, final CommandSender messageReceiver, final Replacement... replacements);

    /**
     * Broadcasts the specified message to all online players.
     *
     * @param messagePath  The path of the message to broadcast.
     * @param replacements The replacements to apply to the message.
     */
    void broadcast(final String messagePath, final Replacement... replacements);

    /**
     * Broadcasts the specified message component to all online players.
     *
     * @param component    The message component to broadcast.
     * @param replacements The replacements to apply to the message.
     */
    void broadcast(final Component component, final Replacement... replacements);

    /**
     * @param player The {@link Player} whose locale is being set.
     * @param locale The {@link Locale} to be set for the player.
     *               This method sets the locale for the specified player. The locale is used to determine the language of messages to be sent to the player.
     */
    void setLocale(final Player player, final Locale locale);

    /**
     * @param player The {@link Player} whose locale is being retrieved.
     * @return The {@link Locale} of the player.
     * This method retrieves the locale for the specified player. The locale is used to determine the language of messages to be sent to the player.
     */
    Locale getLocale(final Player player);

    /**
     * @return A list of {@link Locale} objects representing the supported languages.
     * This method returns a list of locales representing the supported languages.
     */
    List<Locale> getSupportedLanguages();
}
