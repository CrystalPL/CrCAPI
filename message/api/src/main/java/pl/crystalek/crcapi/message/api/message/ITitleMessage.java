package pl.crystalek.crcapi.message.api.message;

import net.kyori.adventure.text.Component;

public interface ITitleMessage {

    /**
     * Returns the title message as a Component object.
     *
     * @return the title message as a Component object
     */
    Component getTitleComponent();

    /**
     * Returns the subtitle message as a Component object.
     *
     * @return the subtitle message as a Component object
     */
    Component getSubTitleComponent();
}
