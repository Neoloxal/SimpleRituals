package com.neoloxal.simple_rituals.advancment.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

import java.util.function.Predicate;

public class FirstRitualTrigger extends SimpleCriterionTrigger<FirstRitualTriggerInstance> {
    @Override
    public Codec<FirstRitualTriggerInstance> codec() {
        return RecordCodecBuilder.create((triggerInstance) -> triggerInstance.group(EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(FirstRitualTriggerInstance::player)).apply(triggerInstance, FirstRitualTriggerInstance::new));
    }

    public void trigger(ServerPlayer player) {
        super.trigger(player, triggerInstance -> true);
    }
}
