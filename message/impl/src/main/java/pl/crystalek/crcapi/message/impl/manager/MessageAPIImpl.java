package pl.crystalek.crcapi.message.impl.manager;

import com.google.common.collect.ImmutableMap;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import pl.crystalek.crcapi.message.api.MessageAPI;
import pl.crystalek.crcapi.message.api.message.Message;
import pl.crystalek.crcapi.message.api.util.MessageUtil;
import pl.crystalek.crcapi.message.impl.CrCAPIMessage;
import pl.crystalek.crcapi.message.impl.user.UserCache;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

abstract class MessageAPIImpl implements MessageAPI {
    abstract void sendMessage(final String messagePath, final Audience audience, final Map<String, Object> replacements);

    abstract void sendMessageComponent(final String messagePath, final Audience audience, final Map<String, Component> replacements);

    abstract <T> Optional<T> getMessage(final String messagePath, final Audience audience, final Class<T> messageClass);

    @Override
    public <T> Optional<T> getMessage(final String messagePath, final CommandSender messageReceiver, final Class<T> messageClass) {
        return getMessage(messagePath, CrCAPIMessage.getBukkitAudiences().sender(messageReceiver), messageClass);
    }

    @Override
    public abstract boolean init();

    @Override
    public void sendMessage(final String messagePath, final CommandSender messageReceiver) {
        sendMessage(messagePath, CrCAPIMessage.getBukkitAudiences().sender(messageReceiver), ImmutableMap.of());
    }

    @Override
    public void sendMessage(final String messagePath, final CommandSender messageReceiver, final Map<String, Object> replacements) {
        sendMessage(messagePath, CrCAPIMessage.getBukkitAudiences().sender(messageReceiver), replacements);
    }

    @Override
    public void sendMessageComponent(final String messagePath, final CommandSender messageReceiver, final Map<String, Component> replacements) {
        sendMessageComponent(messagePath, CrCAPIMessage.getBukkitAudiences().sender(messageReceiver), replacements);
    }

    @Override
    public void sendMessage(final Component component, final CommandSender messageReceiver, final Map<String, Component> replacements) {
        CrCAPIMessage.getBukkitAudiences().sender(messageReceiver).sendMessage(MessageUtil.replaceComponent(component, replacements));
    }

    @Override
    public void sendMessageComponent(final Component component, final CommandSender messageReceiver, final Map<String, Object> replacements) {
        CrCAPIMessage.getBukkitAudiences().sender(messageReceiver).sendMessage(MessageUtil.replace(component, replacements));
    }

    @Override
    public void broadcastComponent(final String messagePath, final Map<String, Component> replacements) {
        sendMessageComponent(messagePath, CrCAPIMessage.getBukkitAudiences().players(), replacements);
    }

    @Override
    public void setLocale(final Player player, final Locale locale) {
        UserCache.setLocale(player, locale);
    }

    @Override
    public Locale getLocale(final Player player) {
        return UserCache.getLocale(CrCAPIMessage.getBukkitAudiences().player(player));
    }

    void sendMessage(final Map<String, List<Message>> messageMap, final String messagePath, final Audience audience, final Map<String, Object> replacements) {
        final List<Message> messageList = messageMap.get(messagePath);
        if (messageList == null) {
            audience.sendMessage(Component.text("Nie odnaleziono wiadomości: " + messagePath + ". Zgłoś błąd administratorowi."));
            return;
        }

        messageList.forEach(message -> message.sendMessage(audience, replacements));
    }

    void sendMessageComponent(final Map<String, List<Message>> messageMap, final String messagePath, final Audience audience, final Map<String, Component> replacements) {
        final List<Message> messageList = messageMap.get(messagePath);
        if (messageList == null) {
            audience.sendMessage(Component.text("Nie odnaleziono wiadomości: " + messagePath + ". Zgłoś błąd administratorowi."));
            return;
        }

        messageList.forEach(message -> message.sendMessageComponent(audience, replacements));
    }

    <T> Optional<T> getComponent(final Map<String, List<Message>> messageMap, final String messagePath, final Class<T> messageClass) {
        for (final Message message : messageMap.get(messagePath)) {
            if (messageClass.isAssignableFrom(message.getClass())) {
                return Optional.of(messageClass.cast(message));
            }
        }

        return Optional.empty();
    }
}
