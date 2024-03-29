package pl.crystalek.crcapi.message.impl.mesage.impl;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;
import pl.crystalek.crcapi.core.util.NumberUtil;
import pl.crystalek.crcapi.message.api.message.IBossBarMessage;
import pl.crystalek.crcapi.message.api.message.Message;
import pl.crystalek.crcapi.message.api.replacement.Replacement;
import pl.crystalek.crcapi.message.api.util.MessageUtil;
import pl.crystalek.crcapi.message.impl.exception.MessageLoadException;

import java.util.Optional;

@Getter
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public final class BossBarMessage implements Message, IBossBarMessage {
    final Component bossBarComponent;
    final BossBar.Color color;
    final BossBar.Overlay overlay;
    @Setter
    float progress;
    long stayTime;
    JavaPlugin plugin;

    public BossBarMessage(final Component bossBarComponent, final BossBar.Color color, final BossBar.Overlay overlay, final float progress) {
        this.bossBarComponent = bossBarComponent;
        this.color = color;
        this.overlay = overlay;
        this.progress = progress;
    }

    public static BossBarMessage loadBossBar(final ConfigurationSection bossBarMessageSection, final JavaPlugin plugin) throws MessageLoadException {
        final ConfigurationSection bossBarSection = bossBarMessageSection.getConfigurationSection("bossbar");
        final float progress;
        final BossBar.Color color;
        final BossBar.Overlay overlay;
        final long stayTime;

        checkFieldExist(bossBarSection, "title");
        checkFieldExist(bossBarSection, "progress");
        checkFieldExist(bossBarSection, "color");
        checkFieldExist(bossBarSection, "overlay");

        final Component title = MessageUtil.convertTextAsComponent(bossBarSection.getString("title"));

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
            return new BossBarMessage(title, color, overlay, progress, stayTime, plugin);
        }

        return new BossBarMessage(title, color, overlay, progress);
    }

    private static void checkFieldExist(final ConfigurationSection titleConfiguration, final String field) throws MessageLoadException {
        if (!titleConfiguration.contains("title")) {
            throw new MessageLoadException("Nie wykryto pola: " + field);
        }
    }

    @Override
    public void sendMessage(final Audience sender, final Replacement... replacements) {
        final BossBar bossBar = BossBar.bossBar(MessageUtil.replace(bossBarComponent, replacements), progress, color, overlay);

        sender.showBossBar(bossBar);
        if (plugin != null) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, () -> sender.hideBossBar(bossBar), stayTime);
        }
    }
}
