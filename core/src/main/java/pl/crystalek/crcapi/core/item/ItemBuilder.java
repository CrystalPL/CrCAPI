package pl.crystalek.crcapi.core.item;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import pl.crystalek.crcapi.core.util.ColorUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A builder class for creating ItemStack object.
 */

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class ItemBuilder {
    Material material;
    int amount = 1;
    short data;
    String name;
    List<String> lore;
    List<Enchant> enchantList;
    List<ItemFlag> itemFlagList;

    /**
     * Creates a new instance of the ItemBuilder class.
     *
     * @return a new instance of the ItemBuilder class
     */
    public static ItemBuilder builder() {
        return new ItemBuilder();
    }

    /**
     * Sets the material of the ItemStack.
     *
     * @param material the material of the ItemStack
     * @return the {@link ItemBuilder} instance
     */
    public ItemBuilder material(final Material material) {
        this.material = material;
        return this;
    }

    /**
     * Sets the amount of the ItemStack.
     *
     * @param amount the amount of the ItemStack
     * @return the {@link ItemBuilder} instance
     */
    public ItemBuilder amount(final int amount) {
        this.amount = amount;
        return this;
    }

    /**
     * Sets the data/durability of the ItemStack.
     *
     * @param data the data/durability of the ItemStack
     * @return the {@link ItemBuilder} instance
     */
    public ItemBuilder data(final short data) {
        this.data = data;
        return this;
    }

    /**
     * Sets the name of the ItemStack.
     *
     * @param name the name of the ItemStack
     * @return the {@link ItemBuilder} instance
     */
    public ItemBuilder name(final String name) {
        this.name = ColorUtil.color(name);
        return this;
    }

    /**
     * Sets the lore of the ItemStack.
     *
     * @param lore the lore of the ItemStack
     * @return the {@link ItemBuilder} instance
     */
    public ItemBuilder lore(final List<String> lore) {
        this.lore = lore;
        return this;
    }

    /**
     * Adds a line of lore to the ItemStack.
     *
     * @param lore the line of lore to add
     * @return the {@link ItemBuilder} instance
     */
    public ItemBuilder lore(final String lore) {
        if (this.lore == null) {
            this.lore = new ArrayList<>();
        }

        this.lore.add(ColorUtil.color(lore));
        return this;
    }

    /**
     * Sets the lore of the ItemStack.
     *
     * @param lore the lore of the ItemStack
     * @return the {@link ItemBuilder} instance
     */
    public ItemBuilder lore(final String... lore) {
        this.lore = Arrays.asList(lore);
        return this;
    }

    /**
     * Adds an enchantment to the ItemStack.
     *
     * @param enchantList the list of Enchant objects to add
     * @return the {@link ItemBuilder} instance
     */
    public ItemBuilder enchant(final List<Enchant> enchantList) {
        this.enchantList = enchantList;
        return this;
    }

    /**
     * Adds an enchantment to the ItemStack.
     *
     * @param enchant the Enchant object to add
     * @return the {@link ItemBuilder} instance
     */
    public ItemBuilder enchant(final Enchant enchant) {
        if (this.enchantList == null) {
            this.enchantList = new ArrayList<>();
        }

        this.enchantList.add(enchant);
        return this;
    }

    /**
     * Adds one or more Enchants to the ItemStack.
     *
     * @param enchants One or more Enchants to add to the ItemStack
     * @return This {@link ItemBuilder} instance
     */
    public ItemBuilder enchant(final Enchant... enchants) {
        this.enchantList = Arrays.asList(enchants);
        return this;
    }

    /**
     * Adds an Enchant with the given name and level to the ItemStack.
     *
     * @param enchantName Name of the Enchant to add
     * @param level       Level of the Enchant to add
     * @return This {@link ItemBuilder} instance
     */
    public ItemBuilder enchant(final String enchantName, final int level) {
        return this.enchant(Enchant.builder().enchant(enchantName).level(level));
    }

    /**
     * Adds an Enchant with the given Enchantment and level to the ItemStack.
     *
     * @param enchantment Enchantment to add
     * @param level       Level of the Enchantment to add
     * @return This {@link ItemBuilder} instance
     */
    public ItemBuilder enchant(final Enchantment enchantment, final int level) {
        return this.enchant(Enchant.builder().enchant(enchantment).level(level));
    }

    /**
     * Sets the ItemFlags of the ItemStack to the given List of ItemFlags.
     *
     * @param itemFlagList List of ItemFlags to set for the ItemStack
     * @return This {@link ItemBuilder} instance
     */
    public ItemBuilder flag(final List<ItemFlag> itemFlagList) {
        this.itemFlagList = itemFlagList;
        return this;
    }

    /**
     * Hides the attributes of the ItemStack by setting HIDE_ATTRIBUTES and HIDE_UNBREAKABLE flags.
     *
     * @return This {@link ItemBuilder} instance
     */
    public ItemBuilder hideAttributes() {
        this.itemFlagList = Arrays.asList(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_UNBREAKABLE);
        return this;
    }

    /**
     * Sets the ItemFlags of the ItemStack to the given array of ItemFlags.
     *
     * @param flags Array of ItemFlags to set for the ItemStack
     * @return This {@link ItemBuilder} instance
     */
    public ItemBuilder flag(final ItemFlag... flags) {
        this.itemFlagList = Arrays.asList(flags);
        return this;
    }

    /**
     * Adds a single ItemFlag to the list of ItemFlags of the ItemStack.
     *
     * @param itemFlag ItemFlag to add to the list of ItemFlags of the ItemStack
     * @return This {@link ItemBuilder} instance
     */
    public ItemBuilder flag(final ItemFlag itemFlag) {
        if (this.itemFlagList == null) {
            this.itemFlagList = new ArrayList<>();
        }

        this.itemFlagList.add(itemFlag);
        return this;
    }

    /**
     * Sets the durability of the ItemStack.
     *
     * @param durability Durability of the ItemStack
     * @return This {@link ItemBuilder} instance
     */
    public ItemBuilder durability(final short durability) {
        this.data = (short) (material.getMaxDurability() - durability);
        return this;
    }

    /**
     * Builds and returns the ItemStack based on the values set in the builder.
     *
     * @return The built ItemStack.
     */
    public ItemStack build() {
        // Create a new ItemStack with the specified material, amount, and data.
        final ItemStack itemStack = new ItemStack(material, amount, data);

        // Get the item meta of the ItemStack.
        final ItemMeta itemMeta = itemStack.getItemMeta();

        // Set the display name of the item meta if it has been specified.
        if (name != null) {
            itemMeta.setDisplayName(ColorUtil.color(name));
        }

        // Set the lore of the item meta if it has been specified.
        if (lore != null) {
            itemMeta.setLore(ColorUtil.color(lore));
        }

        // Add the specified item flags to the item meta if they have been specified.
        if (itemFlagList != null) {
            itemMeta.addItemFlags(itemFlagList.toArray(new ItemFlag[0]));
        }

        // Set the item meta of the ItemStack.
        itemStack.setItemMeta(itemMeta);

        // Remove any existing enchantments from the item stack and apply the specified enchantments if they have been specified.
        if (enchantList != null) {
            itemMeta.getEnchants().keySet().forEach(itemStack::removeEnchantment);
            enchantList.forEach(enchant -> enchant.apply(itemStack));
        }

        // Return the built ItemStack.
        return itemStack;
    }

}
