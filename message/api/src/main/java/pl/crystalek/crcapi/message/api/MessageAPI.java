package pl.crystalek.crcapi.message.api;

import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

public interface MessageAPI {

    boolean init();

    <T> Optional<T> getMessage(final String messagePath, final CommandSender messageReceiver, final Class<T> messageClass);

    void sendMessage(final String messagePath, final CommandSender messageReceiver);

    void sendMessage(final String messagePath, final CommandSender messageReceiver, final Map<String, Object> replacements);

    void sendMessageComponent(final String messagePath, final CommandSender messageReceiver, final Map<String, Component> replacements);

    void sendMessage(final Component component, final CommandSender messageReceiver, final Map<String, Component> replacements);

    void sendMessageComponent(final Component component, final CommandSender messageReceiver, final Map<String, Object> replacements);

    void broadcastComponent(final String messagePath, final Map<String, Component> replacements);

    void broadcast(final String messagePath, final Map<String, Object> replacements);

    void broadcast(final String messagePath);

    //throw UnsupportedOperationException if you do not use LocalizedMessage
    void setLocale(final Player player, final Locale locale);

    //throw UnsupportedOperationException if you do not use LocalizedMessage
    Locale getLocale(final Player player);

    //throw UnsupportedOperationException if you do not use LocalizedMessage
    List<Locale> getSupportedLanguages();
}
