package pl.crystalek.crcapi.message.api.util;

import com.google.common.collect.ImmutableMap;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextReplacementConfig;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.apache.commons.lang.StringUtils;
import pl.crystalek.crcapi.message.api.replacement.Replacement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@UtilityClass
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class MessageUtil {
    Map<String, String> COLORS_MAPPER = new ImmutableMap.Builder<String, String>()
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
            .put("/&k", "</obf>")
            .put("/&l", "</b>")
            .put("/&m", "</st>")
            .put("/&n", "</underlined>")
            .put("/&o", "</i>")
            .put("/&r", "</r>")
            .put("&k", "<obf>")
            .put("&l", "<b>")
            .put("&m", "<st>")
            .put("&n", "<underlined>")
            .put("&o", "<i>")
            .put("&r", "<r>")
            .build();

    /**
     * Formats the given text object as a single string.
     *
     * @param textObject the text to be formatted
     * @return the text as a single string
     */
    public String formatStringMessage(final Object textObject) {
        String chatMessageString;
        if (textObject instanceof String) {
            chatMessageString = (String) textObject;
        } else {
            final List<String> chatMessageListString = (ArrayList<String>) textObject;
            chatMessageString = StringUtils.join(chatMessageListString, "\n");
        }

        return chatMessageString;
    }

    /**
     * Converts the given oldText string to a {@link Component}. It replaces the old color codes with the corresponding Adventure color codes
     *
     * @param oldText the message to be converted
     * @return the converted message as a {@link Component}
     */
    public Component convertTextAsComponent(final String oldText) {
        String replaceText = oldText;
        for (final Map.Entry<String, String> entry : COLORS_MAPPER.entrySet()) {
            replaceText = replaceText.replace(entry.getKey(), entry.getValue());
        }

        final MiniMessage miniMessage = MiniMessage.miniMessage();
        return miniMessage.deserialize(replaceText);
    }

    /**
     * Replaces the text in the provided {@link Component} with the given {@link Replacement} objects.
     *
     * @param component    the {@link Component} that contains the text to be replaced
     * @param replacements an array of {@link Replacement} objects that specify the text to be replaced and its replacement
     * @return the updated {@link Component} with the replaced text
     */
    public Component replace(Component component, final Replacement... replacements) {
        for (final Replacement replacement : replacements) {
            component = component.replaceText(TextReplacementConfig.builder()
                    .matchLiteral(replacement.getFrom())
                    .replacement(replacement.getTo())
                    .build());
        }

        return component;
    }
}
