package com.neoloxal.simple_rituals.items;

import com.neoloxal.simple_rituals.SimpleRituals;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(SimpleRituals.MODID);

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
