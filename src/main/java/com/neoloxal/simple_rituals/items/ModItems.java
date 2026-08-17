package com.neoloxal.simple_rituals.items;

import com.neoloxal.simple_rituals.SimpleRituals;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SimpleRituals.MODID);

    public static final DeferredItem<Item> EMPTY_SPAWN_EGG = ITEMS.register("empty_spawn_egg", () -> new Item(new Item.Properties()
            .stacksTo(16)
    ));

    public static final DeferredItem<Item> UNKNOWN = ITEMS.register("unknown", () -> new Item(new Item.Properties()) {
        @Override
        public void inventoryTick(ItemStack stack, Level level, Entity entity, int slotId, boolean isSelected) {
            super.inventoryTick(stack, level, entity, slotId, isSelected);
            stack.setCount(0);
        }
    });

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
