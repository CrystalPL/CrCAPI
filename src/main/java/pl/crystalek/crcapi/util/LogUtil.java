package pl.crystalek.crcapi.util;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

@UtilityClass
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class LogUtil {
    Logger logger;

    static {
        logger = JavaPlugin.getProvidingPlugin(LogUtil.class).getLogger();
    }

    public void info(final String info) {
        logger.info(info);
    }

    public void warn(final String warn) {
        logger.warning(warn);
    }

    public void error(final String error) {
        logger.severe(error);
    }
}
