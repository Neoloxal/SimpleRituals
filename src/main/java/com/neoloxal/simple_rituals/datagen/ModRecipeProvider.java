package com.neoloxal.simple_rituals.datagen;

import com.neoloxal.simple_rituals.SimpleRituals;
import com.neoloxal.simple_rituals.blocks.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        super.buildRecipes(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, ModBlocks.PEDESTAL.get())
                        .pattern("BBB")
                        .pattern("LAL")
                        .pattern("BBB")
                        .define('B', Items.STONE_BRICKS).define('L', Items.LAPIS_LAZULI).define('A', Items.ANDESITE)
                        .unlockedBy("has_lapis", has(Items.LAPIS_LAZULI)).save(recipeOutput);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.CENTRAL_PEDESTAL.get())
                .pattern("LLL")
                .pattern("LPL")
                .pattern("LLL")
                .define('L', Items.LAPIS_LAZULI).define('P', ModBlocks.PEDESTAL.get())
                .unlockedBy("has_pedestal", has(ModBlocks.PEDESTAL.get())).save(recipeOutput);
    }
}
