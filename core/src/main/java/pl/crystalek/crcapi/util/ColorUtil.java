package pl.crystalek.crcapi.util;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class ColorUtil {

    public String color(final String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public List<String> color(final List<String> list) {
        return list.stream().map(ColorUtil::color).collect(Collectors.toList());
    }
}