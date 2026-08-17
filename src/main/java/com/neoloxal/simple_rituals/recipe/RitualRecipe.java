package com.neoloxal.simple_rituals.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class RitualRecipe implements Recipe<RitualRecipeInput> {
    private final int size;
    private final List<Ingredient> ingredients;
    private final ItemStack output;
    private final String specialEffect;
    private final Boolean hideOutput;

    public RitualRecipe(int size, List<Ingredient> ingredients, ItemStack output, String specialEffect, Boolean hideOutput) {
        this.size = size;
        this.ingredients = ingredients;
        this.output = output;
        this.specialEffect = specialEffect;
        this.hideOutput = hideOutput;
    }

    @Override
    public boolean matches(RitualRecipeInput input, Level level) {
        List<Ingredient> leftOverIngredients = new ArrayList<>(ingredients);
        if (input.size() != ingredients.size()) {
            return false;
        }
        for (int i = 0; i < input.size(); i++) {
            Iterator<Ingredient> iter = leftOverIngredients.iterator();
            boolean succeeded = false;
            while (iter.hasNext()) {
                Ingredient ingredient = iter.next();
                if (ingredient.test(input.getItem(i))) {
                    iter.remove();
                    succeeded = true;
                    break;
                }
            }
            if (!succeeded) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack assemble(RitualRecipeInput ritualRecipeInput, HolderLookup.Provider provider) {
        return output.copy();
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return true;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return output;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.RITUAL_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return ModRecipes.RITUAL.get();
    }

    public int getSize() {
        return size;
    }

    public List<Ingredient> getRitualIngredients() {
        return ingredients;
    }

    public ItemStack getOutput() {
        return output;
    }

    public String getSpecialEffect() {
        return specialEffect;
    }

    public Boolean getHideOutput() {
        return hideOutput;
    }
}
