package pl.crystalek.crcapi.message.impl.loader.message;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.configuration.ConfigurationSection;
import pl.crystalek.crcapi.core.util.NumberUtil;
import pl.crystalek.crcapi.message.api.message.ITitleMessage;
import pl.crystalek.crcapi.message.api.message.Message;
import pl.crystalek.crcapi.message.api.replacement.Replacement;
import pl.crystalek.crcapi.message.api.util.MessageUtil;
import pl.crystalek.crcapi.message.impl.exception.MessageLoadException;

import java.time.Duration;
import java.util.Optional;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class TitleMessage implements Message, ITitleMessage {
    Component titleComponent;
    Component subTitleComponent;
    Title.Times times;

    public static TitleMessage loadTitleMessage(final ConfigurationSection titleConfiguration) throws MessageLoadException {
        final ConfigurationSection titleSection = titleConfiguration.getConfigurationSection("title");

        final boolean containsTitle = titleSection.contains("title");
        final boolean containsSubtitle = titleSection.contains("subtitle");
        if (!containsTitle && !containsSubtitle) {
            throw new MessageLoadException("Nie wykryto pola title i subtitle!");
        }

        Duration fadeIn = Duration.ofMillis(1000L);
        Duration stay = Duration.ofMillis(1000L);
        Duration fadeOut = Duration.ofMillis(1000L);

        if (titleSection.contains("fadeIn")) {
            fadeIn = getTitleTime(titleSection, "fadeIn");
        }

        if (titleSection.contains("stay")) {
            stay = getTitleTime(titleSection, "stay");
        }

        if (titleSection.contains("fadeOut")) {
            fadeOut = getTitleTime(titleSection, "fadeOut");
        }

        final Component titleComponent = MessageUtil.convertTextAsComponent(titleSection.getString("title"));
        final Component subTitleComponent = MessageUtil.convertTextAsComponent(titleSection.getString("subtitle"));

        return new TitleMessage(titleComponent, subTitleComponent, Title.Times.of(fadeIn, stay, fadeOut));
    }

    private static Duration getTitleTime(final ConfigurationSection timeConfiguration, final String field) throws MessageLoadException {
        final Optional<Integer> fadeInOptional = NumberUtil.getInt(timeConfiguration.get(field));
        if (!fadeInOptional.isPresent() || fadeInOptional.get() < 0) {
            throw new MessageLoadException("Wartość pola: " + field + " nie jest liczbą z zakresu: <1, 2_147_483_647>!");
        }

        return Duration.ofMillis(fadeInOptional.get());
    }

    @Override
    public void sendMessage(final Audience sender, final Replacement... replacements) {
        final Title title = Title.title(MessageUtil.replace(this.titleComponent, replacements), MessageUtil.replace(subTitleComponent, replacements), times);
        sender.showTitle(title);
    }
}
