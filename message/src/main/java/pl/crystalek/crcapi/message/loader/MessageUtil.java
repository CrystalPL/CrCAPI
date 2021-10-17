package pl.crystalek.crcapi.message.loader;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@UtilityClass
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MessageUtil {
    public String getStringMessage(final Object objectMessage) {
        String chatMessageString;
        if (objectMessage instanceof String) {
            chatMessageString = (String) objectMessage;
        } else {
            final List<String> chatMessageListString = (ArrayList<String>) objectMessage;
            chatMessageString = StringUtils.join(chatMessageListString, "\n");
        }

        return chatMessageString;
    }

    public Component replaceOldColorToComponent(final String oldText) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(oldText);
    }

    public Component replace(final Component component, final Map<String, Object> replacements) {
        Component newComponent = component;

        for (final Map.Entry<String, Object> entry : replacements.entrySet()) {
            final TextReplacementConfig replacement = TextReplacementConfig.builder().matchLiteral(entry.getKey()).replacement(entry.getValue().toString()).build();
            newComponent = newComponent.replaceText(replacement);
        }

        return newComponent;
    }
}
