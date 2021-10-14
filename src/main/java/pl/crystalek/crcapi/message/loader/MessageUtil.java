package pl.crystalek.crcapi.message.loader;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@UtilityClass
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MessageUtil {
    MiniMessage miniMessage = MiniMessage.miniMessage();
    ImmutableSet<Map.Entry<String, String>> colorsMapperEntry = new ImmutableMap.Builder<String, String>()
            .put("&0", "<black>")
            .put("&1", "<dark_blue>")
            .put("&2", "<dark_green>")
            .put("&3", "<dark_aqua>")
            .put("&4", "<dark_red>")
            .put("&5", "<dark_purple>")
            .put("&6", "<gold>")
            .put("&7", "<gray>")
            .put("&8", "<dark_gray>")
            .put("&9", "<blue>")
            .put("&a", "<green>")
            .put("&b", "<aqua>")
            .put("&c", "<red>")
            .put("&d", "<light_purple>")
            .put("&e", "<yellow>")
            .put("&f", "<white>")
            .put("&k", "<obf>")
            .put("&l", "<b>")
            .put("&m", "<st>")
            .put("&n", "<underlined>")
            .put("&o", "<i>")
            .put("&r", "<r>")
            .put("/&k", "</obf>")
            .put("/&l", "</b>")
            .put("/&m", "</st>")
            .put("/&n", "</underlined>")
            .put("/&o", "</i>")
            .put("/&r", "</r>")
            .build()
            .entrySet();

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

    public Component replaceOldColorToComponent(String oldText) {
        for (final Map.Entry<String, String> entry : colorsMapperEntry) {
            oldText = oldText.replace(entry.getKey(), entry.getValue());
        }

        return miniMessage.parse(oldText);
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
