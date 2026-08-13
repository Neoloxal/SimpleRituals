package com.neoloxal.simple_rituals.blocks.entity;

import com.neoloxal.simple_rituals.SimpleRituals;
import com.neoloxal.simple_rituals.blocks.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, SimpleRituals.MODID);

    public static final Supplier<BlockEntityType<PedestalBlockEntity>> PEDESTAL_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("pedestal_block_entity", () -> BlockEntityType.Builder.of(
                    PedestalBlockEntity::new, ModBlocks.PEDESTAL.get(), ModBlocks.CENTRAL_PEDESTAL.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
