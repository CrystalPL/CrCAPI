package pl.crystalek.crcapi;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

public final class Recipe {

    private final String recipeName;
    private final ItemStack resultItem;
    private final Material[] ingredients;

    public Recipe(final String recipeName, final ItemStack resultItem) {
        this.recipeName = recipeName;
        this.resultItem = resultItem;
        this.ingredients = new Material[9];
    }

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
