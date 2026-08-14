package com.neoloxal.simple_rituals.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;

import java.util.List;

public record RitualRecipeInput(List<ItemStack> ingredients) implements RecipeInput {
    @Override
    public ItemStack getItem(int i) {
        return ingredients.get(i);
    }

    @Override
    public int size() {
        return ingredients.size();
    }
}
