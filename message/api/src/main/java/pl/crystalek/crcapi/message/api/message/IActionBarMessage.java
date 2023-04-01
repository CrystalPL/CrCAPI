package pl.crystalek.crcapi.message.api.message;

import net.kyori.adventure.text.Component;

public interface IActionBarMessage {

    /**
     * Returns the ActionBar message as a Component object.
     *
     * @return the message as a Component object
     */
    Component getActionBarComponent();
}
