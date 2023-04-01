package pl.crystalek.crcapi.core.item;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

@FieldDefaults(level = AccessLevel.PRIVATE)
public final class Enchant {
    Enchantment enchantment;
    int level = 1;

    /**
     * Returns a new instance of Enchant builder.
     *
     * @return a new instance of Enchant builder
     */
    public static Enchant builder() {
        return new Enchant();
    }

    /**
     * Sets the enchantment to apply to an item.
     *
     * @param enchantment the enchantment to apply
     * @return the updated Enchant builder instance
     */
    public Enchant enchant(final Enchantment enchantment) {
        this.enchantment = enchantment;
        return this;
    }

    /**
     * Sets the enchantment to apply to an item by its name.
     *
     * @param name the name of the enchantment to apply
     * @return the updated Enchant builder instance
     */
    public Enchant enchant(final String name) {
        this.enchantment = Enchantment.getByName(name);
        return this;
    }

    /**
     * Sets the level of the enchantment to apply to an item.
     *
     * @param level the level of the enchantment to apply
     * @return the updated Enchant builder instance
     */
    public Enchant level(final int level) {
        this.level = level;
        return this;
    }

    /**
     * Applies the enchantment with the specified level to the given ItemStack.
     *
     * @param itemStack the item to apply the enchantment to
     */
    public void apply(final ItemStack itemStack) {
        itemStack.addUnsafeEnchantment(enchantment, level);
    }
}