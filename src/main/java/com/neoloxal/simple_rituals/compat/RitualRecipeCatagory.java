package com.neoloxal.simple_rituals.compat;

import com.neoloxal.simple_rituals.SimpleRituals;
import com.neoloxal.simple_rituals.blocks.ModBlocks;
import com.neoloxal.simple_rituals.items.ModItems;
import com.neoloxal.simple_rituals.recipe.RitualRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public class RitualRecipeCatagory implements IRecipeCategory<RitualRecipe> {
    public static final ResourceLocation UID = ResourceLocation.fromNamespaceAndPath(SimpleRituals.MODID, "ritual");

    public static final ResourceLocation TEXTURE_SMALL = ResourceLocation.fromNamespaceAndPath(SimpleRituals.MODID, "textures/gui/ritual/small_ritual_gui.png");
    public static final ResourceLocation TEXTURE_MEDIUM = ResourceLocation.fromNamespaceAndPath(SimpleRituals.MODID, "textures/gui/ritual/medium_ritual_gui.png");
    public static final ResourceLocation TEXTURE_LARGE = ResourceLocation.fromNamespaceAndPath(SimpleRituals.MODID, "textures/gui/ritual/large_ritual_gui.png");

    public static final ResourceLocation TEXTURE_DANGER = ResourceLocation.fromNamespaceAndPath(SimpleRituals.MODID, "textures/gui/ritual/dangerous_ritual_gui.png");

    public static final int TEXTURE_WIDTH = 80;
    public static final int TEXTURE_HEIGHT = 80;

    public static final RecipeType<RitualRecipe> RITUAL_RECIPE_TYPE = new RecipeType<>(UID, RitualRecipe.class);

    private final IDrawable background_small;
    private final IDrawable background_medium;
    private final IDrawable background_large;

    private final IDrawable background_danger;

    private final IDrawable icon;

    public RitualRecipeCatagory(IGuiHelper helper) {
        this.background_small = helper.drawableBuilder(TEXTURE_SMALL, 0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT).setTextureSize(TEXTURE_WIDTH, TEXTURE_HEIGHT).build();
        this.background_medium = helper.drawableBuilder(TEXTURE_MEDIUM, 0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT).setTextureSize(TEXTURE_WIDTH, TEXTURE_HEIGHT).build();
        this.background_large = helper.drawableBuilder(TEXTURE_LARGE, 0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT).setTextureSize(TEXTURE_WIDTH, TEXTURE_HEIGHT).build();

        this.background_danger = helper.drawableBuilder(TEXTURE_DANGER, 0, 0, TEXTURE_WIDTH, TEXTURE_HEIGHT).setTextureSize(TEXTURE_WIDTH, TEXTURE_HEIGHT).build();

        this.icon  = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(ModBlocks.CENTRAL_PEDESTAL));
    }

    @Override
    public RecipeType<RitualRecipe> getRecipeType() {
        return RITUAL_RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("recipe.simple_rituals.ritual");
    }

    @Override
    public @Nullable IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, RitualRecipe recipe, IFocusGroup focuses) {
        if (recipe.getSize() == 1) {
            builder.addSlot(RecipeIngredientRole.INPUT, 6, 32).addIngredients(recipe.getRitualIngredients().get(0));
            builder.addSlot(RecipeIngredientRole.INPUT, 58, 32).addIngredients(recipe.getRitualIngredients().get(1));
        } else if (recipe.getSize() == 2) {
            builder.addSlot(RecipeIngredientRole.INPUT, 6, 32).addIngredients(recipe.getRitualIngredients().get(0));
            builder.addSlot(RecipeIngredientRole.INPUT, 32, 6).addIngredients(recipe.getRitualIngredients().get(1));
            if (recipe.getRitualIngredients().size() >= 3) builder.addSlot(RecipeIngredientRole.INPUT, 58, 32).addIngredients(recipe.getRitualIngredients().get(2));
            if (recipe.getRitualIngredients().size() >= 4) builder.addSlot(RecipeIngredientRole.INPUT, 32, 58).addIngredients(recipe.getRitualIngredients().get(3));
        } else if (recipe.getSize() == 3) {
            builder.addSlot(RecipeIngredientRole.INPUT, 6, 32).addIngredients(recipe.getRitualIngredients().get(0));
            builder.addSlot(RecipeIngredientRole.INPUT, 10, 10).addIngredients(recipe.getRitualIngredients().get(1));
            if (recipe.getRitualIngredients().size() >= 3) builder.addSlot(RecipeIngredientRole.INPUT, 32, 6).addIngredients(recipe.getRitualIngredients().get(2));
            if (recipe.getRitualIngredients().size() >= 4) builder.addSlot(RecipeIngredientRole.INPUT, 54, 10).addIngredients(recipe.getRitualIngredients().get(3));
            if (recipe.getRitualIngredients().size() >= 5) builder.addSlot(RecipeIngredientRole.INPUT, 58, 32).addIngredients(recipe.getRitualIngredients().get(4));
            if (recipe.getRitualIngredients().size() >= 6) builder.addSlot(RecipeIngredientRole.INPUT, 54, 54).addIngredients(recipe.getRitualIngredients().get(5));
            if (recipe.getRitualIngredients().size() >= 7) builder.addSlot(RecipeIngredientRole.INPUT, 32, 58).addIngredients(recipe.getRitualIngredients().get(6));
            if (recipe.getRitualIngredients().size() == 8) builder.addSlot(RecipeIngredientRole.INPUT, 10, 54).addIngredients(recipe.getRitualIngredients().get(7));
        }

        if (!recipe.getHideOutput()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 32, 32).addItemStack(recipe.getOutput());
        } else {
            builder.addSlot(RecipeIngredientRole.RENDER_ONLY, 32, 32).addItemStack(ModItems.UNKNOWN.toStack());
        }
    }

    @Override
    public void draw(RitualRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
        if (recipe.getSize() == 1) {
            background_small.draw(guiGraphics);
        } else if (recipe.getSize() == 2) {
            background_medium.draw(guiGraphics);
        } else if (recipe.getSize() ==  3) {
            background_large.draw(guiGraphics);
        }

        if (!recipe.getSpecialEffect().equals("none")) {
            background_danger.draw(guiGraphics);
        }

        if (recipe.getHideOutput()) {
            //guiGraphics.renderFakeItem(ModItems.UNKNOWN.toStack(), 32, 32);
        }
    }

    @Override
    public int getWidth() {
        return TEXTURE_WIDTH;
    }

    @Override
    public int getHeight() {
        return TEXTURE_HEIGHT;
    }
}
