package pl.crystalek.crcapi.message;

import com.google.common.collect.ImmutableMap;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.message.loader.MessageUtil;

import java.util.Map;
import java.util.Optional;

public abstract class AbstractMessageAPI {
    private final BukkitAudiences bukkitAudiences;

    public AbstractMessageAPI(final JavaPlugin plugin) {
        this.bukkitAudiences = BukkitAudiences.create(plugin);
    }

    public abstract void sendMessage(final String messagePath, final Audience audience, final Map<String, Object> replacements);

    public abstract void sendMessageComponent(final String messagePath, final Audience audience, final Map<String, Component> replacements);

    public abstract Optional<Component> getComponent(final String messagePath, final Class<? extends Message> clazz);

    public void sendMessage(final String messagePath, final CommandSender messageReceiver) {
        sendMessage(messagePath, bukkitAudiences.sender(messageReceiver));
    }

    public void sendMessage(final String messagePath, final CommandSender messageReceiver, final Map<String, Object> replacements) {
        sendMessage(messagePath, bukkitAudiences.sender(messageReceiver), replacements);
    }

    public void sendMessage(final String messagePath, final Audience audience) {
        sendMessage(messagePath, audience, ImmutableMap.of());
    }

    public void sendMessageComponent(final String messagePath, final CommandSender messageReceiver, final Map<String, Component> replacements) {
        sendMessageComponent(messagePath, bukkitAudiences.sender(messageReceiver), replacements);
    }

    public void sendMessage(final Component component, final CommandSender messageReceiver, final Map<String, Component> replacements) {
        bukkitAudiences.sender(messageReceiver).sendMessage(MessageUtil.replaceComponent(component, replacements));
    }

    public void sendMessageComponent(final Component component, final CommandSender messageReceiver, final Map<String, Object> replacements) {
        bukkitAudiences.sender(messageReceiver).sendMessage(MessageUtil.replace(component, replacements));
    }

    public void broadcastComponent(final String messagePath, final Map<String, Component> replacements) {
        sendMessageComponent(messagePath, bukkitAudiences.players(), replacements);
    }

    public void broadcast(final String messagePath, final Map<String, Object> replacements) {
        sendMessage(messagePath, bukkitAudiences.players(), replacements);
    }

    public void broadcast(final String messagePath) {
        sendMessage(messagePath, bukkitAudiences.players());
    }
}
