package com.neoloxal.simple_rituals.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;

import java.util.ArrayList;

public class RitualRecipeSerializer implements RecipeSerializer<RitualRecipe> {
    @Override
    public MapCodec<RitualRecipe> codec() {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
                Codec.intRange(1, 3).fieldOf("size").forGetter(RitualRecipe::getSize),
                Codec.list(Ingredient.CODEC, 2, 8).fieldOf("ingredients").forGetter(RitualRecipe::getRitualIngredients),
                ItemStack.CODEC.fieldOf("output").forGetter(RitualRecipe::getOutput),
                Codec.BOOL.fieldOf("spawn_lightning").forGetter(RitualRecipe::getSpawnLightning)
        ).apply(instance, RitualRecipe::new));
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, RitualRecipe> streamCodec() {
        return StreamCodec.composite(
                ByteBufCodecs.VAR_INT, RitualRecipe::getSize,
                Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()), RitualRecipe::getRitualIngredients,
                ItemStack.STREAM_CODEC, RitualRecipe::getOutput,
                ByteBufCodecs.BOOL, RitualRecipe::getSpawnLightning,
                RitualRecipe::new
        );
    }
}
