package pl.crystalek.crcapi.singlemessage;

import lombok.Getter;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.bukkit.plugin.java.JavaPlugin;

public final class CrCAPISingleMessage extends JavaPlugin {
    @Getter
    private static BukkitAudiences bukkitAudiences;

    @Override
    public void onEnable() {
        bukkitAudiences = BukkitAudiences.create(this);
    }
}
