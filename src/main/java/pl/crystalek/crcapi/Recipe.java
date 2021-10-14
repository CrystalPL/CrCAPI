package pl.crystalek.crcapi;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.experimental.FieldDefaults;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class Recipe {
    final JavaPlugin plugin = JavaPlugin.getProvidingPlugin(Recipe.class);
    Material slot1;
    Material slot2;
    Material slot3;
    Material slot4;
    Material slot5;
    Material slot6;
    Material slot7;
    Material slot8;
    Material slot9;
    ItemStack resultItem;
    String recipeName;

    public void registerRecipe() {
        ShapedRecipe recipe;
        try {
            Class.forName("org.bukkit.NamespacedKey");

            final NamespacedKey bedrock_breaker = new NamespacedKey(plugin, recipeName);
            recipe = new ShapedRecipe(bedrock_breaker, resultItem);
        } catch (final ClassNotFoundException exception) {
            recipe = new ShapedRecipe(resultItem);
        }
        recipe.shape("abc", "def", "ghi");

        setItem(recipe, slot1, 'a');
        setItem(recipe, slot2, 'b');
        setItem(recipe, slot3, 'c');
        setItem(recipe, slot4, 'd');
        setItem(recipe, slot5, 'e');
        setItem(recipe, slot6, 'f');
        setItem(recipe, slot7, 'g');
        setItem(recipe, slot8, 'h');
        setItem(recipe, slot9, 'i');

        Bukkit.addRecipe(recipe);
    }

    private void setItem(final ShapedRecipe recipe, final Material material, final char character) {
        if (material != Material.AIR) {
            recipe.setIngredient(character, material);
        }
    }
}
