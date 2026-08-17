package com.neoloxal.simple_rituals.advancment.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;

public class MaxTierRitualTrigger extends SimpleCriterionTrigger<MaxTierRitualTriggerInstance> {
    @Override
    public Codec<MaxTierRitualTriggerInstance> codec() {
        return RecordCodecBuilder.create((triggerInstance) -> triggerInstance.group(EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(MaxTierRitualTriggerInstance::player)).apply(triggerInstance, MaxTierRitualTriggerInstance::new));
    }

    public void trigger(ServerPlayer player) {
        super.trigger(player, triggerInstance -> true);
    }
}
