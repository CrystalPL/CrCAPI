package pl.crystalek.crcapi.message.impl.manager;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.message.api.message.Message;
import pl.crystalek.crcapi.message.impl.CrCAPIMessage;
import pl.crystalek.crcapi.message.impl.loader.SingleMessageLoader;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class SingleMessageAPI extends MessageAPIImpl {
    final SingleMessageLoader messageLoader;
    Map<String, List<Message>> messageMap;

    public SingleMessageAPI(final JavaPlugin plugin) {
        this.messageLoader = new SingleMessageLoader(plugin);
    }

    @Override
    void sendMessage(final String messagePath, final Audience audience, final Map<String, Object> replacements) {
        sendMessage(messageMap, messagePath, audience, replacements);
    }

    @Override
    void sendMessageComponent(final String messagePath, final Audience audience, final Map<String, Component> replacements) {
        sendMessageComponent(messageMap, messagePath, audience, replacements);
    }

    @Override
    <T> Optional<T> getMessage(final String messagePath, final Audience audience, final Class<T> messageClass) {
        return getComponent(messageMap, messagePath, messageClass);
    }

    @Override
    public boolean init() {
        final boolean init = messageLoader.init();
        messageMap = messageLoader.getMessageMap();

        return init;
    }

    @Override
    public void broadcast(final String messagePath, final Map<String, Object> replacements) {
        sendMessage(messagePath, CrCAPIMessage.getInstance().getBukkitAudiences().players(), replacements);
    }

    @Override
    public void broadcast(final String messagePath) {
        sendMessage(messagePath, CrCAPIMessage.getInstance().getBukkitAudiences().players(), ImmutableMap.of());
    }

    @Override
    public void setLocale(final Player player, final Locale locale) {
        throw new UnsupportedOperationException("you muse use LocalizedMessage to set player locale");
    }

    @Override
    public Locale getLocale(final Player player) {
        throw new UnsupportedOperationException("you muse use LocalizedMessage to get player locale");
    }

    @Override
    public List<Locale> getSupportedLanguages() {
        throw new UnsupportedOperationException("you muse use LocalizedMessage to get supported languages");
    }
}
