package pl.crystalek.crcapi.message.impl.mesage.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import org.bukkit.configuration.ConfigurationSection;
import pl.crystalek.crcapi.message.api.message.IChatMessage;
import pl.crystalek.crcapi.message.api.message.Message;
import pl.crystalek.crcapi.message.api.replacement.Replacement;
import pl.crystalek.crcapi.message.api.util.MessageUtil;
import pl.crystalek.crcapi.message.impl.exception.MessageLoadException;

import java.util.function.Predicate;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class ChatMessage implements Message, IChatMessage {
    static Predicate<String> CHAT_FORMAT = key -> key.equalsIgnoreCase("hover") || key.equalsIgnoreCase("action") || key.equalsIgnoreCase("message");
    Component chatComponent;

    public static ChatMessage loadChatMessage(final ConfigurationSection messageConfiguration) throws MessageLoadException {
        if (!messageConfiguration.isConfigurationSection("chat")) {
            final Component singleChatMessage = MessageUtil.convertTextAsComponent(MessageUtil.formatStringMessage(messageConfiguration.get("chat")));
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
        Component chatMessage = MessageUtil.convertTextAsComponent(MessageUtil.formatStringMessage(messageSectionConfiguration.get("message")));

        //check if the message has hover event
        if (messageSectionConfiguration.contains("hover")) {
            chatMessage = chatMessage.hoverEvent(HoverEvent.showText(MessageUtil.convertTextAsComponent(MessageUtil.formatStringMessage(messageSectionConfiguration.get("hover")))));
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
    public void sendMessage(final Audience sender, final Replacement... replacements) {
        sender.sendMessage(MessageUtil.replace(chatComponent, replacements));
    }
}
