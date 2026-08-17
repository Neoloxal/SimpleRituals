package com.neoloxal.simple_rituals.advancment;

import com.neoloxal.simple_rituals.SimpleRituals;
import com.neoloxal.simple_rituals.advancment.criterion.FirstRitualTrigger;
import com.neoloxal.simple_rituals.advancment.criterion.MaxTierRitualTrigger;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.core.registries.Registries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModAdvancementTriggers {
    public static final DeferredRegister<CriterionTrigger<?>> TRIGGER_TYPES = DeferredRegister.create(Registries.TRIGGER_TYPE, SimpleRituals.MODID);

    public static final Supplier<FirstRitualTrigger> FIRST_RITUAL_TRIGGER = TRIGGER_TYPES.register("first_ritual", FirstRitualTrigger::new);

    public static final Supplier<MaxTierRitualTrigger> MAX_TIER_RITUAL_TRIGGER = TRIGGER_TYPES.register("max_tier_ritual", MaxTierRitualTrigger::new);

    public static void register(IEventBus eventBus) {
        TRIGGER_TYPES.register(eventBus);
    }
}
