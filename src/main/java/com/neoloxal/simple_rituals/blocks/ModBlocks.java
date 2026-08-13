package com.neoloxal.simple_rituals.blocks;

import com.neoloxal.simple_rituals.SimpleRituals;
import com.neoloxal.simple_rituals.blocks.custom.CentralPedestal;
import com.neoloxal.simple_rituals.blocks.custom.PedestalBlock;
import com.neoloxal.simple_rituals.items.ModItems;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(SimpleRituals.MODID);

    public static final DeferredBlock<Block> CENTRAL_PEDESTAL = registerBlock("central_pedestal", () -> new CentralPedestal(BlockBehaviour.Properties.of()
            .requiresCorrectToolForDrops()
            .noOcclusion()
    ));

    public static final DeferredBlock<Block> PEDESTAL = registerBlock("pedestal", () -> new PedestalBlock(BlockBehaviour.Properties.of()
            .requiresCorrectToolForDrops()
            .noOcclusion()
    ));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
