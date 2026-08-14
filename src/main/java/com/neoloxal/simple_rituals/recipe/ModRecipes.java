package com.neoloxal.simple_rituals.recipe;

import com.neoloxal.simple_rituals.SimpleRituals;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModRecipes {
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, SimpleRituals.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, SimpleRituals.MODID);

    public static final Supplier<RecipeType<RitualRecipe>> RITUAL = RECIPE_TYPES.register("ritual", () -> new RecipeType<RitualRecipe>() {
        @Override
        public String toString() {
            return "ritual";
        }
    });
    public static final Supplier<RitualRecipeSerializer> RITUAL_SERIALIZER = RECIPE_SERIALIZERS.register("ritual", RitualRecipeSerializer::new);

    public static void register(IEventBus eventBus) {
        RECIPE_TYPES.register(eventBus);
        RECIPE_SERIALIZERS.register(eventBus);
    }
}
