package com.neoloxal.simple_rituals.datagen;

import com.neoloxal.simple_rituals.SimpleRituals;
import com.neoloxal.simple_rituals.advancment.ModAdvancementTriggers;
import com.neoloxal.simple_rituals.advancment.criterion.FirstRitualTriggerInstance;
import com.neoloxal.simple_rituals.advancment.criterion.MaxTierRitualTriggerInstance;
import com.neoloxal.simple_rituals.blocks.ModBlocks;
import net.minecraft.advancements.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ModAdvancementProvider extends AdvancementProvider {
    public ModAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries, ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new ModAdvancementGenerator()));
    }

    private static final class ModAdvancementGenerator implements AdvancementGenerator {

        @Override
        public void generate(HolderLookup.Provider provider, Consumer<AdvancementHolder> saver, ExistingFileHelper existingFileHelper) {
            Advancement.Builder builder = Advancement.Builder.advancement();

            builder.parent(AdvancementSubProvider.createPlaceholder("minecraft:story/enchant_item"));

            builder.display(
                    ModBlocks.PEDESTAL.toStack(),
                    Component.translatable("advancements.simple_rituals.first_ritual.title"),
                    Component.translatable("advancements.simple_rituals.first_ritual.description"),
                    null,
                    AdvancementType.TASK,
                    true,
                    true,
                    false
            );

            builder.addCriterion("is_first_ritual", new Criterion<>(ModAdvancementTriggers.FIRST_RITUAL_TRIGGER.get(), new FirstRitualTriggerInstance(Optional.empty())));

            builder.requirements(AdvancementRequirements.allOf(List.of("is_first_ritual")));

            builder.save(saver, ResourceLocation.fromNamespaceAndPath(SimpleRituals.MODID, "first_ritual"), existingFileHelper);


            builder = Advancement.Builder.advancement();

            builder.parent(AdvancementSubProvider.createPlaceholder("simple_rituals:first_ritual"));

            builder.display(
                    ModBlocks.CENTRAL_PEDESTAL.toStack(),
                    Component.translatable("advancements.simple_rituals.max_tier_ritual.title"),
                    Component.translatable("advancements.simple_rituals.max_tier_ritual.description"),
                    null,
                    AdvancementType.GOAL,
                    true,
                    true,
                    false
            );

            builder.addCriterion("is_max_tier_ritual", new Criterion<>(ModAdvancementTriggers.MAX_TIER_RITUAL_TRIGGER.get(), new MaxTierRitualTriggerInstance(Optional.empty())));

            builder.requirements(AdvancementRequirements.allOf(List.of("is_max_tier_ritual")));

            builder.save(saver, ResourceLocation.fromNamespaceAndPath(SimpleRituals.MODID, "max_tier_ritual"), existingFileHelper);
        }
    }
}
