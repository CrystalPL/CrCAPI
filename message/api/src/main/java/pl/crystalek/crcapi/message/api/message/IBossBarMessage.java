package pl.crystalek.crcapi.message.api.message;

import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;

public interface IBossBarMessage {
    /**
     * Returns the BossBar message as a Component object.
     *
     * @return the message as a Component object
     */
    Component getBossBarComponent();

    /**
     * Returns the current progress of the bossbar.
     *
     * @return the progress of the BossBar as a float between 0 and 1
     */
    float getProgress();

    /**
     * Sets the progress of the bossbar.
     *
     * @param progress the new progress of the BossBar as a float between 0 and 1
     */
    void setProgress(final float progress);

    /**
     * Returns the bossbar color.
     *
     * @return the color of the BossBar
     */
    BossBar.Color getColor();

    /**
     * Returns the bossbar overlay style.
     *
     * @return the overlay style of the BossBar
     */
    BossBar.Overlay getOverlay();

    /**
     * Returns the bossbar stay time.
     *
     * @return the stay time of the BossBar in ticks
     */
    long getStayTime();
}

