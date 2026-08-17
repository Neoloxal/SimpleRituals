package com.neoloxal.simple_rituals.datagen;

import com.neoloxal.simple_rituals.SimpleRituals;
import com.neoloxal.simple_rituals.blocks.ModBlocks;
import com.neoloxal.simple_rituals.items.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, SimpleRituals.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleBlockItem(ModBlocks.CENTRAL_PEDESTAL.get());
        simpleBlockItem(ModBlocks.PEDESTAL.get());

        basicItem(ModItems.EMPTY_SPAWN_EGG.get());
        basicItem(ModItems.UNKNOWN.get());
    }
}
