package pl.crystalek.crcapi.message.impl;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.configuration.ConfigurationSection;
import pl.crystalek.crcapi.message.Message;
import pl.crystalek.crcapi.message.util.MessageUtil;

import java.util.Map;

@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
@Getter
public final class ActionBarMessage implements Message {
    Component component;

    public static ActionBarMessage loadActionBar(final ConfigurationSection actionBarMessageSection) {
        final String actionBar = actionBarMessageSection.getString("actionbar");
        final Component component = MessageUtil.replaceOldColorToComponent(actionBar);

        return new ActionBarMessage(component);
    }

    @Override
    public void sendMessage(final Audience sender, final Map<String, Object> replacements) {
        sender.sendActionBar(MessageUtil.replace(component, replacements));
    }

    @Override
    public void sendMessageComponent(final Audience sender, final Map<String, Component> replacements) {
        sender.sendActionBar(MessageUtil.replaceComponent(component, replacements));
    }
}
