package pl.crystalek.crcapi.message.api.replacement;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.text.Component;
import pl.crystalek.crcapi.message.api.util.MessageUtil;

@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class Replacement {
    String from;
    Component to;

    public static Replacement of(final String from, final String to) {
        return new Replacement(from, MessageUtil.convertTextAsComponent(to));
    }

    public static Replacement of(final String from, final Component to) {
        return new Replacement(from, to);
    }
}
