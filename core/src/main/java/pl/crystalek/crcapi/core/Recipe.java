package pl.crystalek.crcapi.core;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public final class Recipe {

    String recipeName;
    ItemStack resultItem;
    Material[] ingredients = new Material[9];

    public void setItem(final int slot, final Material material) {
        this.ingredients[slot] = material;
    }

    public void registerRecipe(final Plugin plugin) {
        if (plugin == null) {
            throw new NullPointerException("plugin cannot be null");
        }

        ShapedRecipe recipe;
        try {
            Class.forName("org.bukkit.NamespacedKey");
            recipe = new ShapedRecipe(new NamespacedKey(plugin, this.recipeName), this.resultItem);
        } catch (final ClassNotFoundException exception) {
            recipe = new ShapedRecipe(this.resultItem);
        }

        recipe.shape("abc", "def", "ghi");

        for (int i = 0; i < 9; i++) {
            final Material ingredient = this.ingredients[i];
            if (ingredient == null || ingredient == Material.AIR) {
                continue;
            }

            recipe.setIngredient((char) ('a' + i), ingredient);
        }

        Bukkit.addRecipe(recipe);
    }

}
