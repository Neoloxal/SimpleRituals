package com.neoloxal.simple_rituals.blocks.custom;

import com.mojang.serialization.MapCodec;
import com.neoloxal.simple_rituals.blocks.entity.PedestalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CentralPedestal extends PedestalBlock {
    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 12.5f, 16);

    public CentralPedestal(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        PedestalBlockEntity pedestalBlockEntity = new PedestalBlockEntity(blockPos, blockState);
        pedestalBlockEntity.central = true;
        pedestalBlockEntity.magic_level = 1;
        return pedestalBlockEntity;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }
}
