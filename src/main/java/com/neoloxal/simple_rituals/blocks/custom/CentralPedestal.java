package com.neoloxal.simple_rituals.blocks.custom;

import com.mojang.serialization.MapCodec;
import com.neoloxal.simple_rituals.blocks.entity.PedestalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemStackHandler;
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

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof PedestalBlockEntity pedestalBlockEntity) {
            if (!level.isClientSide()) {
                if (!stack.isEmpty() && !ItemStack.isSameItemSameComponents(player.getItemInHand(hand), pedestalBlockEntity.inventory.getStackInSlot(0)) && !player.isCreative()) {
                    return ItemInteractionResult.FAIL;
                }

                ItemStackHandler inv = pedestalBlockEntity.inventory;
                ItemStack pedStack = inv.getStackInSlot(0).copy();

                if (player.isCreative()) {
                    if (stack.isEmpty()) {
                        if (pedStack.isEmpty()) {
                            return ItemInteractionResult.FAIL;
                        }
                        inv.setStackInSlot(0, ItemStack.EMPTY);
                        player.setItemInHand(hand, pedStack);
                        player.playNotifySound(SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
                        return ItemInteractionResult.SUCCESS;
                    }

                    ItemStack toInsert = stack.copy();
                    ItemStack remainder = inv.insertItem(0, toInsert, true);

                    if (!remainder.isEmpty()) {
                        ItemStack newPedStack = stack.copy();
                        inv.setStackInSlot(0, newPedStack);
                    } else {
                        inv.insertItem(0, toInsert, false);
                    }
                    stack.shrink(toInsert.getCount());
                } else {
                    inv.setStackInSlot(0, ItemStack.EMPTY);
                }

                returnStackToPlayer(player, hand, pedStack, inv);
                player.playNotifySound(SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
            }
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.FAIL;
    }
}
