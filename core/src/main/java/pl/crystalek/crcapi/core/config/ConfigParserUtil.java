package pl.crystalek.crcapi.core.config;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import pl.crystalek.crcapi.core.Recipe;
import pl.crystalek.crcapi.core.config.exception.ConfigLoadException;
import pl.crystalek.crcapi.core.item.ItemBuilder;
import pl.crystalek.crcapi.core.util.NumberUtil;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * A utility class providing methods for retrieving values from configuration and performing validation checks.
 */
@UtilityClass
public class ConfigParserUtil {

    /**
     * Returns an ItemStack object parsed from a ConfigurationSection object.
     *
     * @param itemConfiguration The ConfigurationSection object containing the item data.
     * @return An ItemStack object parsed from the ConfigurationSection.
     * @throws ConfigLoadException If an error occurs while parsing the item data.
     */
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
                itemBuilder.lore((List<String>) lore);
            } else {
                throw new ConfigLoadException("Nie udało się załadować pola lore!");
            }
        }

        if (itemConfiguration.contains("itemFlag")) {
            final Object itemFlag = itemConfiguration.get("itemFlag");
            if (itemFlag instanceof String) {
                itemBuilder.flag(ItemFlag.valueOf(((String) itemFlag).toUpperCase()));
            } else if (itemFlag instanceof List) {
                itemBuilder.flag(((List<String>) itemFlag).stream()
                        .map(line -> ItemFlag.valueOf(line.toUpperCase()))
                        .collect(Collectors.toList())
                );
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

    /**
     * Returns a Location object parsed from a ConfigurationSection object.
     *
     * @param locationConfiguration The ConfigurationSection object containing the location data.
     * @return A Location object parsed from the ConfigurationSection.
     * @throws ConfigLoadException If an error occurs while parsing the location data.
     */
    public Location getLocation(final ConfigurationSection locationConfiguration) throws ConfigLoadException {
        final String worldName = locationConfiguration.getString("world");
        final World world = Bukkit.getWorld(worldName);
        if (world == null) {
            throw new ConfigLoadException("Nie odnaleziono świata: " + worldName);
        }

        final Optional<Double> xOptional = NumberUtil.getDouble(locationConfiguration.get("x"));
        if (!xOptional.isPresent()) {
            throw new ConfigLoadException("Koordynat x nie jest liczbą!");
        }

        final Optional<Double> yOptional = NumberUtil.getDouble(locationConfiguration.get("y"));
        if (!yOptional.isPresent()) {
            throw new ConfigLoadException("Koordynat y nie jest liczbą!");
        }

        final Optional<Double> zOptional = NumberUtil.getDouble(locationConfiguration.get("z"));
        if (!zOptional.isPresent()) {
            throw new ConfigLoadException("Koordynat z nie jest liczbą!");
        }

        final Optional<Float> yawOptional = NumberUtil.getFloat(locationConfiguration.get("yaw"));
        if (!yawOptional.isPresent()) {
            throw new ConfigLoadException("Koordynat yaw nie jest liczbą!");
        }

        final Optional<Float> pitchOptional = NumberUtil.getFloat(locationConfiguration.get("pitch"));
        if (!pitchOptional.isPresent()) {
            throw new ConfigLoadException("Koordynat pitch nie jest liczbą!");
        }

        return new Location(world, xOptional.get(), yOptional.get(), zOptional.get(), yawOptional.get(), pitchOptional.get());
    }

    /**
     * Returns a Recipe object with its item array populated with materials parsed from a ConfigurationSection object.
     *
     * @param recipeConfiguration The ConfigurationSection object containing the recipe data.
     * @param recipe              The Recipe object to be populated with the parsed materials.
     * @return A Recipe object with its item array populated with the parsed materials.
     * @throws ConfigLoadException If an error occurs while parsing the recipe data.
     */
    public Recipe getRecipe(final ConfigurationSection recipeConfiguration, final Recipe recipe) throws ConfigLoadException {
        for (int i = 0; i < 9; i++) {
            recipe.setItem(i, getMaterial(getString(recipeConfiguration, String.valueOf(i + 1))));
        }

        return recipe;
    }

    /**
     * Returns a Material object parsed from a String.
     *
     * @param materialName The name of the material to be parsed.
     * @return A Material object parsed from the provided String.
     * @throws ConfigLoadException If the provided String does not correspond to a valid Material.
     */
    public Material getMaterial(final String materialName) throws ConfigLoadException {
        try {
            return Material.valueOf(materialName.toUpperCase());
        } catch (final IllegalArgumentException exception) {
            throw new ConfigLoadException("Nie odnaleziono przedmiotu: " + materialName);
        }
    }

    /**
     * Retrieves a boolean value from the given ConfigurationSection object with the specified path.
     *
     * @param configurationSection The ConfigurationSection to retrieve the boolean value from.
     * @param booleanPath          The path to the boolean value.
     * @return The boolean value.
     * @throws ConfigLoadException If the specified path does not exist in the ConfigurationSection, or if the value is not "true" or "false".
     */
    public boolean getBoolean(final ConfigurationSection configurationSection, final String booleanPath) throws ConfigLoadException {
        checkFieldExist(configurationSection, booleanPath);

        final String booleanName = configurationSection.getString(booleanPath).toLowerCase();
        if (!booleanName.equals("true") && !booleanName.equals("false")) {
            throw new ConfigLoadException("Pole: " + booleanPath + " nie jest wartością true lub false");
        }

        return Boolean.parseBoolean(booleanName);
    }

    /**
     * Retrieves an integer value from the given ConfigurationSection object with the specified path.
     *
     * @param configurationSection The ConfigurationSection to retrieve the integer value from.
     * @param intPath              The path to the integer value.
     * @return The integer value.
     * @throws ConfigLoadException If the specified path does not exist in the ConfigurationSection, or if the value is not a valid integer.
     */
    public int getInt(final ConfigurationSection configurationSection, final String intPath) throws ConfigLoadException {
        checkFieldExist(configurationSection, intPath);

        final Optional<Integer> integerOptional = NumberUtil.getInt(configurationSection.get(intPath));
        if (!integerOptional.isPresent()) {
            throw new ConfigLoadException("Pole " + intPath + " nie spełnia wymaganych warunków!");
        }

        return integerOptional.get();
    }

    /**
     * Retrieves an integer value from the given ConfigurationSection object with the specified path and applies the specified condition to it.
     *
     * @param configurationSection The ConfigurationSection to retrieve the integer value from.
     * @param intPath              The path to the integer value.
     * @param intCondition         The condition to apply to the integer value.
     * @return The integer value.
     * @throws ConfigLoadException If the specified path does not exist in the ConfigurationSection, or if the value is not a valid integer, or if the condition is not met.
     */
    public int getInt(final ConfigurationSection configurationSection, final String intPath, final Function<Integer, Boolean> intCondition) throws ConfigLoadException {
        final int number = getInt(configurationSection, intPath);

        if (!intCondition.apply(number)) {
            throw new ConfigLoadException("Pole " + intPath + " nie spełnia wymaganych warunków!");
        }

        return number;
    }

    /**
     * Retrieves a string value from the given ConfigurationSection object with the specified path.
     *
     * @param configurationSection The ConfigurationSection to retrieve the string value from.
     * @param stringPath           The path to the string value.
     * @return The string value.
     * @throws ConfigLoadException If the specified path does not exist in the ConfigurationSection.
     */
    public String getString(final ConfigurationSection configurationSection, final String stringPath) throws ConfigLoadException {
        checkFieldExist(configurationSection, stringPath);

        return configurationSection.getString(stringPath);
    }

    /**
     * Retrieves a long value from the given ConfigurationSection object with the specified path.
     *
     * @param configurationSection The ConfigurationSection to retrieve the long value from.
     * @param longPath             The path to the long value.
     * @return The long value.
     * @throws ConfigLoadException If the specified path does not exist in the ConfigurationSection, or if the value is not a valid long.
     */
    public long getLong(final ConfigurationSection configurationSection, final String longPath) throws ConfigLoadException {
        checkFieldExist(configurationSection, longPath);

        final Optional<Long> longOptional = NumberUtil.getLong(configurationSection.get(longPath));
        if (!longOptional.isPresent()) {
            throw new ConfigLoadException("Pole " + longPath + " nie jest liczbą całkowitą!");
        }

        return longOptional.get();
    }

    /**
     * Retrieves a long value from the given ConfigurationSection object with the specified path and applies the specified condition to it.
     *
     * @param configurationSection The ConfigurationSection to retrieve the long value from.
     * @param longPath             The path to the long value.
     * @param longCondition        The condition to apply to the long value.
     * @return The long value.
     * @throws ConfigLoadException If the specified path does not exist in the ConfigurationSection, or if the value is not a valid long, or if the condition is not met.
     */
    public long getLong(final ConfigurationSection configurationSection, final String longPath, final Function<Long, Boolean> longCondition) throws ConfigLoadException {
        final long number = getLong(configurationSection, longPath);

        if (!longCondition.apply(number)) {
            throw new ConfigLoadException("Pole " + longPath + " nie spełnia wymaganych warunków!");
        }

        return number;
    }

    /**
     * Retrieves a double value from the specified ConfigurationSection at the given path.
     *
     * @param configurationSection the ConfigurationSection to retrieve the value from
     * @param doublePath           the path to the double value in the ConfigurationSection
     * @return the double value at the given path
     * @throws ConfigLoadException if the specified path does not exist or the value at the path is not a valid double
     */
    public double getDouble(final ConfigurationSection configurationSection, final String doublePath) throws ConfigLoadException {
        checkFieldExist(configurationSection, doublePath);

        final Optional<Double> doubleOptional = NumberUtil.getDouble(configurationSection.get(doublePath));
        if (!doubleOptional.isPresent()) {
            throw new ConfigLoadException("Pole " + doublePath + " nie jest liczbą zmiennoprzecinkową!");
        }

        return doubleOptional.get();
    }

    /**
     * Retrieves a double value from the specified ConfigurationSection at the given path, and applies a condition to it.
     *
     * @param configurationSection the ConfigurationSection to retrieve the value from
     * @param doublePath           the path to the double value in the ConfigurationSection
     * @param doubleCondition      a function to apply to the retrieved double value
     * @return the double value at the given path if it satisfies the condition
     * @throws ConfigLoadException if the specified path does not exist, the value at the path is not a valid double,
     *                             or the retrieved value does not satisfy the specified condition
     */
    public double getDouble(final ConfigurationSection configurationSection, final String doublePath, final Function<Double, Boolean> doubleCondition) throws ConfigLoadException {
        final double number = getDouble(configurationSection, doublePath);

        if (!doubleCondition.apply(number)) {
            throw new ConfigLoadException("Pole " + doublePath + " nie spełnia wymaganych warunków!");
        }

        return number;
    }

    /**
     * Retrieves a list of strings from the specified ConfigurationSection at the given path.
     *
     * @param configurationSection the ConfigurationSection to retrieve the value from
     * @param stringListPath       the path to the list of strings in the ConfigurationSection
     * @return the list of strings at the given path
     * @throws ConfigLoadException if the specified path does not exist
     */
    public List<String> getStringList(final ConfigurationSection configurationSection, final String stringListPath) throws ConfigLoadException {
        checkFieldExist(configurationSection, stringListPath);

        return configurationSection.getStringList(stringListPath);
    }

    /**
     * Checks if the specified field exists in the given ConfigurationSection.
     *
     * @param configurationSection the ConfigurationSection to check
     * @param field                the field to check for existence
     * @throws ConfigLoadException if the specified field does not exist in the ConfigurationSection
     */
    public void checkFieldExist(final ConfigurationSection configurationSection, final String field) throws ConfigLoadException {
        if (!configurationSection.contains(field)) {
            throw new ConfigLoadException("Nie odnaleziono pola: " + field);
        }
    }
}
