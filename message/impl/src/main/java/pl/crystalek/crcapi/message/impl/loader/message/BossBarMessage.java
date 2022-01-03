package pl.crystalek.crcapi.message.impl.loader.message;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.core.util.NumberUtil;
import pl.crystalek.crcapi.message.api.Message;
import pl.crystalek.crcapi.message.api.MessageType;
import pl.crystalek.crcapi.message.impl.exception.MessageLoadException;
import pl.crystalek.crcapi.message.impl.util.MessageUtil;

import java.util.Map;
import java.util.Optional;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class BossBarMessage implements Message {
    MessageType messageType = MessageType.BOSSBAR;
    Component component;
    float progress;
    BossBar.Color color;
    BossBar.Overlay overlay;
    @NonFinal
    long stayTime;
    @NonFinal
    JavaPlugin plugin;

    public static BossBarMessage loadBossBar(final ConfigurationSection bossBarMessageSection, final JavaPlugin plugin) throws MessageLoadException {
        final ConfigurationSection bossBarSection = bossBarMessageSection.getConfigurationSection("bossBar");
        final float progress;
        final BossBar.Color color;
        final BossBar.Overlay overlay;
        final long stayTime;

        checkFieldExist(bossBarSection, "title");
        checkFieldExist(bossBarSection, "progress");
        checkFieldExist(bossBarSection, "color");
        checkFieldExist(bossBarSection, "overlay");

        final Component title = MessageUtil.replaceOldColorToComponent(bossBarSection.getString("title"));

        final Optional<Float> progressOptional = NumberUtil.getFloat(bossBarSection.get("progress"));
        if (!progressOptional.isPresent()) {
            throw new MessageLoadException("Wartość pola progress musi być liczbą!");
        }

        progress = progressOptional.get();
        if (progress < 0 || progress > 1) {
            throw new MessageLoadException("Wartość pola progress musi być większa od zera i mniejsza od jedynki!");
        }

        try {
            color = BossBar.Color.valueOf(bossBarSection.getString("color").toUpperCase());
        } catch (final IllegalArgumentException exception) {
            throw new MessageLoadException("Nie odnaleziono koloru: " + bossBarSection.getString("color"));
        }

        try {
            overlay = BossBar.Overlay.valueOf(bossBarSection.getString("overlay").toUpperCase());
        } catch (final IllegalArgumentException exception) {
            throw new MessageLoadException("Nie odnaleziono nakładki: " + bossBarSection.getString("overlay"));
        }

        if (bossBarSection.contains("stayTime")) {
            final Optional<Long> stayTimeOptional = NumberUtil.getLong(bossBarSection.get("stayTime"));
            if (!stayTimeOptional.isPresent() || stayTimeOptional.get() < 0) {
                throw new MessageLoadException("Wartość pola stayTime musi być liczbą z zakresu <1, 9,223,372,036,854,775,807>!");
            }

            stayTime = stayTimeOptional.get();
            return new BossBarMessage(title, progress, color, overlay, stayTime, plugin);
        }

        return new BossBarMessage(title, progress, color, overlay);
    }

    private static void checkFieldExist(final ConfigurationSection titleConfiguration, final String field) throws MessageLoadException {
        if (!titleConfiguration.contains("title")) {
            throw new MessageLoadException("Nie wykryto pola: " + field);
        }
    }

    @Override
    public void sendMessage(final Audience sender, final Map<String, Object> replacements) {
        final Component title = MessageUtil.replace(this.component, replacements);
        final BossBar bossBar = BossBar.bossBar(MessageUtil.replace(title, replacements), progress, color, overlay);

        sender.showBossBar(bossBar);
        if (plugin != null) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> sender.hideBossBar(bossBar), stayTime);
        }
    }

    @Override
    public void sendMessageComponent(final Audience sender, final Map<String, Component> replacements) {
        final Component title = MessageUtil.replaceComponent(this.component, replacements);
        final BossBar bossBar = BossBar.bossBar(MessageUtil.replaceComponent(title, replacements), progress, color, overlay);

        sender.showBossBar(bossBar);
        if (plugin != null) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> sender.hideBossBar(bossBar), stayTime);
        }
    }
}
