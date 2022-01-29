package pl.crystalek.crcapi.message.api.message;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;

public interface IBossBarMessage {

    Component getBossBarComponent();

    float getProgress();

    BossBar.Color getColor();

    BossBar.Overlay getOverlay();

    long getStayTime();

    void setProgress(final float progress);
}
