package pl.crystalek.crcapi.config;

import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import pl.crystalek.crcapi.Recipe;
import pl.crystalek.crcapi.config.exception.ConfigLoadException;
import pl.crystalek.crcapi.item.ItemBuilder;
import pl.crystalek.crcapi.util.NumberUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
public class ConfigParserUtil {

    public ItemStack getItem(final ConfigurationSection itemConfiguration) throws ConfigLoadException {
        if (!itemConfiguration.contains("material")) {
            throw new ConfigLoadException("Nie wykryto pola material!");
        }

        final ItemBuilder itemBuilder = ItemBuilder.builder().material(getMaterial(itemConfiguration.getString("material")));
        if (itemConfiguration.contains("amount")) {
            itemBuilder.amount(itemConfiguration.getInt("amount"));
        }

        if (itemConfiguration.contains("data")) {
            final Optional<Short> dataOptional = NumberUtil.getShort(itemConfiguration.getInt("data"));
            if (!dataOptional.isPresent() || dataOptional.get() < 0) {
                throw new ConfigLoadException("Pole data nie jest z zakresu <0, 32_767>!");
            }

            itemBuilder.data(dataOptional.get());
        }

        if (itemConfiguration.contains("durability")) {
            final Optional<Short> durabilityOptional = NumberUtil.getShort(itemConfiguration.getInt("durability"));
            if (!durabilityOptional.isPresent() || durabilityOptional.get() < 1) {
                throw new ConfigLoadException("Pole durability nie jest z zakresu <1, 32_767>!");
            }

            itemBuilder.durability(durabilityOptional.get());
        }

        if (itemConfiguration.contains("name")) {
            itemBuilder.name(itemConfiguration.getString("name"));
        }

        if (itemConfiguration.contains("lore")) {
            final Object lore = itemConfiguration.get("lore");
            if (lore instanceof String) {
                itemBuilder.lore((String) lore);
            } else if (lore instanceof List) {
                itemBuilder.lore((ArrayList<String>) lore);
            } else {
                throw new ConfigLoadException("Nie udało się załadować pola lore!");
            }
        }

        if (itemConfiguration.contains("itemFlag")) {
            final Object itemFlag = itemConfiguration.get("itemFlag");
            if (itemFlag instanceof String) {
                itemBuilder.flag(ItemFlag.valueOf(((String) itemFlag).toUpperCase()));
            } else if (itemFlag instanceof List) {
                final List<ItemFlag> itemFlagList = ((ArrayList<String>) itemFlag).stream().map(String::toUpperCase).map(ItemFlag::valueOf).collect(Collectors.toList());
                itemBuilder.flag(itemFlagList);
            } else {
                throw new ConfigLoadException("Nie udało się załadować pola itemFlag!");
            }
        }

        if (itemConfiguration.contains("enchantments")) {
            final ConfigurationSection enchantmentsSection = itemConfiguration.getConfigurationSection("enchantments");

            for (final String enchant : enchantmentsSection.getKeys(false)) {
                final Optional<Integer> enchantLevelOptional = NumberUtil.getInt(enchantmentsSection.get(enchant));
                if (!enchantLevelOptional.isPresent() || enchantLevelOptional.get() < 1) {
                    throw new ConfigLoadException("Poziom enchantu musi być liczbą z zakresu <1, 2_147_483_647>");
                }

                itemBuilder.enchant(enchant, enchantLevelOptional.get());
            }
        }

        return itemBuilder.build();
    }

    public Recipe.RecipeBuilder getRecipe(final ConfigurationSection recipeConfiguration) throws ConfigLoadException {
        return Recipe.builder()
                .slot1(getMaterial(recipeConfiguration.getString("1")))
                .slot2(getMaterial(recipeConfiguration.getString("2")))
                .slot3(getMaterial(recipeConfiguration.getString("3")))
                .slot4(getMaterial(recipeConfiguration.getString("4")))
                .slot5(getMaterial(recipeConfiguration.getString("5")))
                .slot6(getMaterial(recipeConfiguration.getString("6")))
                .slot7(getMaterial(recipeConfiguration.getString("7")))
                .slot8(getMaterial(recipeConfiguration.getString("8")))
                .slot9(getMaterial(recipeConfiguration.getString("9")));

    }

    private Material getMaterial(final String materialName) throws ConfigLoadException {
        try {
            return Material.valueOf(materialName.toUpperCase());
        } catch (final IllegalArgumentException exception) {
            throw new ConfigLoadException("Nie odnaleziono przedmiotu: " + materialName);
        }
    }
}
