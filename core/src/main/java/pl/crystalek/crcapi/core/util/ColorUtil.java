package pl.crystalek.crcapi.core.util;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@UtilityClass
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ColorUtil {
    static final Pattern HEX_COLOR_PATTERN = Pattern.compile("&(#[a-fA-F0-9]{6})");

    public String color(final String text) {
        String newText = text;

        final Matcher hexMatcher = HEX_COLOR_PATTERN.matcher(newText);
        while (hexMatcher.find()) {
            String hex = hexMatcher.group(1);
            newText = newText.replace(hexMatcher.group(), ChatColor.of(hex).toString());
        }

        return ChatColor.translateAlternateColorCodes('&', newText);
    }

    public List<String> color(final List<String> list) {
        return list.stream().map(ColorUtil::color).collect(Collectors.toList());
    }
}