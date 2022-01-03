package pl.crystalek.crcapi.message.impl.loader.message;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.configuration.ConfigurationSection;
import pl.crystalek.crcapi.message.api.Message;
import pl.crystalek.crcapi.message.impl.exception.MessageLoadException;
import pl.crystalek.crcapi.message.impl.util.MessageUtil;

import java.util.Map;
import java.util.function.Predicate;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Getter
public final class ChatMessage implements Message {
    static Predicate<String> CHAT_FORMAT = key -> key.equalsIgnoreCase("hover") || key.equalsIgnoreCase("action") || key.equalsIgnoreCase("message");
    Component component;

    public static ChatMessage loadChatMessage(final ConfigurationSection messageConfiguration) throws MessageLoadException {
        if (!messageConfiguration.isConfigurationSection("chat")) {
            final Component singleChatMessage = MessageUtil.replaceOldColorToComponent(MessageUtil.getStringMessage(messageConfiguration.get("chat")));
            return new ChatMessage(singleChatMessage);
        }

        final ConfigurationSection chatMessageConfiguration = messageConfiguration.getConfigurationSection("chat");
        if (chatMessageConfiguration.getKeys(false).stream().anyMatch(CHAT_FORMAT)) {
            return new ChatMessage(getMessageComponent(chatMessageConfiguration));
        }

        Component chatMessageComponent = Component.text("");
        for (final String messageSectionName : chatMessageConfiguration.getKeys(false)) {
            final ConfigurationSection messageSectionConfiguration = chatMessageConfiguration.getConfigurationSection(messageSectionName);
            final Component messageComponent = getMessageComponent(messageSectionConfiguration);

            chatMessageComponent = chatMessageComponent.append(messageComponent);
        }

        return new ChatMessage(chatMessageComponent);
    }

    private static Component getMessageComponent(final ConfigurationSection messageSectionConfiguration) throws MessageLoadException {
        Component chatMessage = MessageUtil.replaceOldColorToComponent(MessageUtil.getStringMessage(messageSectionConfiguration.get("message")));

        //check if the message has hover event
        if (messageSectionConfiguration.contains("hover")) {
            chatMessage = chatMessage.hoverEvent(HoverEvent.showText(MessageUtil.replaceOldColorToComponent(MessageUtil.getStringMessage(messageSectionConfiguration.get("hover")))));
        }

        //check if the message has click event
        if (messageSectionConfiguration.contains("action")) {
            final ClickEvent.Action clickEventType;
            try {
                clickEventType = ClickEvent.Action.valueOf(messageSectionConfiguration.getString("action.type").toUpperCase());
            } catch (final IllegalArgumentException exception) {
                throw new MessageLoadException("Nie odnaleziono akcji typu: " + messageSectionConfiguration.getString("action.type"));
            }

            if (!messageSectionConfiguration.contains("action.action")) {
                throw new MessageLoadException("Nie podano jaka akcja ma być wykonywana!");
            }

            final String action = messageSectionConfiguration.getString("action.action");
            chatMessage = chatMessage.clickEvent(ClickEvent.clickEvent(clickEventType, action));
        }

        return chatMessage;
    }

    @Override
    public void sendMessage(final Audience sender, final Map<String, Object> replacements) {
        sender.sendMessage(MessageUtil.replace(component, replacements));
    }

    @Override
    public void sendMessageComponent(final Audience sender, final Map<String, Component> replacements) {
        sender.sendMessage(MessageUtil.replaceComponent(component, replacements));
    }
}
