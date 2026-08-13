package com.neoloxal.simple_rituals.blocks.custom;
import com.mojang.serialization.MapCodec;
import com.neoloxal.simple_rituals.SimpleRituals;
import com.neoloxal.simple_rituals.blocks.entity.PedestalBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

public class PedestalBlock extends BaseEntityBlock {
    public static final MapCodec<PedestalBlock> CODEC = simpleCodec(PedestalBlock::new);

    public PedestalBlock(Properties properties) {
		super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new PedestalBlockEntity(blockPos, blockState);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof PedestalBlockEntity pedestalBlockEntity) {
                if (!level.isClientSide()) {
                    pedestalBlockEntity.drops();
                    level.updateNeighbourForOutputSignal(pos, this);
                }
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof PedestalBlockEntity pedestalBlockEntity) {
            if (!level.isClientSide()) {
                ItemStackHandler inv = pedestalBlockEntity.inventory;
                ItemStack pedStack = inv.getStackInSlot(0).copy();

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
                toInsert.setCount(1);
                ItemStack remainder = inv.insertItem(0, toInsert, true);

                if (!remainder.isEmpty()) {
                    ItemStack newPedStack = stack.copy();
                    newPedStack.setCount(1);
                    inv.setStackInSlot(0, newPedStack);
                } else {
                    inv.insertItem(0, toInsert, false);
                }
                stack.shrink(1);

                returnStackToPlayer(player, hand, pedStack, inv);
                player.playNotifySound(SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
            }
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.FAIL;
    }

    protected void returnStackToPlayer(Player player, InteractionHand hand, ItemStack pedStack, ItemStackHandler inv) {
        if (!pedStack.isEmpty()) {
            if (player.getItemInHand(hand).isEmpty()) {
                player.setItemInHand(hand, pedStack);
                return;
            } else if (ItemStack.isSameItemSameComponents(player.getItemInHand(hand), pedStack)) {
                int itemsLeftInSlot = player.getItemInHand(hand).getMaxStackSize() - player.getItemInHand(hand).getCount();
                int overflow = pedStack.getCount() - itemsLeftInSlot;
                inv.setStackInSlot(0, ItemStack.EMPTY);
                if (overflow >= 0) {
                    player.getItemInHand(hand).grow(itemsLeftInSlot);
                    ItemStack updatedPedStack = pedStack.copy();
                    updatedPedStack.setCount(overflow);
                    if (overflow > 0) {
                        if (!player.getInventory().add(updatedPedStack)) {
                            player.drop(updatedPedStack, false);
                        }
                    }
                    return;
                }
                player.getItemInHand(hand).grow(pedStack.getCount());
            }
            if (!player.getInventory().add(pedStack)) {
                player.drop(pedStack, false);
            }
        }
    }
}
