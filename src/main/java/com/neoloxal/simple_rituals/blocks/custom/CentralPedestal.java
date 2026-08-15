package com.neoloxal.simple_rituals.blocks.custom;

import com.neoloxal.simple_rituals.SimpleRituals;
import com.neoloxal.simple_rituals.blocks.entity.ModBlockEntities;
import com.neoloxal.simple_rituals.blocks.entity.PedestalBlockEntity;
import com.neoloxal.simple_rituals.recipe.ModRecipes;
import com.neoloxal.simple_rituals.recipe.RitualRecipe;
import com.neoloxal.simple_rituals.recipe.RitualRecipeInput;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CentralPedestal extends PedestalBlock {
    public static final VoxelShape SHAPE = Block.box(0, 0, 0, 16, 12.5f, 16);

    public CentralPedestal(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        PedestalBlockEntity pedestalBlockEntity = new PedestalBlockEntity(blockPos, blockState);
        pedestalBlockEntity.setCentral(true);
        pedestalBlockEntity.setMagicLevel(1);
        return pedestalBlockEntity;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        SimpleRituals.LOGGER.debug("Interacting with central pedestal with item");
        if (level.getBlockEntity(pos) instanceof PedestalBlockEntity pedestalBlockEntity) {
            if (pedestalBlockEntity.inventory.getStackInSlot(0).isEmpty()) {
                if (player.getItemInHand(hand).isEmpty()) {
                    useWithoutItem(state, level, pos, player, hitResult);
                    return ItemInteractionResult.SUCCESS;
                }
            }
            if (!level.isClientSide()) {
                ItemStackHandler inv = pedestalBlockEntity.inventory;
                ItemStack pedStack = inv.getStackInSlot(0).copy();

                if (!inv.getStackInSlot(0).isEmpty()) {
                    player.playNotifySound(SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 1f, 1f);
                }

                inv.setStackInSlot(0, ItemStack.EMPTY);

                returnStackToPlayer(player, hand, pedStack, inv);
            }
            return ItemInteractionResult.SUCCESS;
        }
        return ItemInteractionResult.FAIL;
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (!level.isClientSide()){
            SimpleRituals.LOGGER.debug("Interacting with central pedestal without item");
            int size = 0;
            List<ItemStack> items = new ArrayList<>();

            BlockPos[] positionsToCheckBasic = {
                    blockPosOffset(pos, 2, 0, 0),
                    blockPosOffset(pos, -2, 0, 0),
                    blockPosOffset(pos, 0, 0, 2),
                    blockPosOffset(pos, 0, 0, -2)
            };

            BlockPos[] positionsToCheckHigher = {
                    blockPosOffset(pos, 3, 0, 0),
                    blockPosOffset(pos, -3, 0, 0),
                    blockPosOffset(pos, 0, 0, 3),
                    blockPosOffset(pos, 0, 0, -3),

                    blockPosOffset(pos, 2, 0, 2),
                    blockPosOffset(pos, 2, 0, -2),
                    blockPosOffset(pos, -2, 0, 2),
                    blockPosOffset(pos, -2, 0, -2)
            };

            List<BlockPos> pedestals = new ArrayList<>();
            for (BlockPos point : positionsToCheckBasic) {
                if (level.getBlockEntity(point) instanceof PedestalBlockEntity) {
                    pedestals.add(point);
                }
            }

            if (pedestals.size() == 2) {
                size = 1;
                for (BlockPos pedestalPosition : pedestals) {
                    items.add(((PedestalBlockEntity) level.getBlockEntity(pedestalPosition)).inventory.getStackInSlot(0));
                }
            } else if (pedestals.size() == 4) {
                size = 2;
                for (BlockPos pedestalPosition : pedestals) {
                    items.add(((PedestalBlockEntity) level.getBlockEntity(pedestalPosition)).inventory.getStackInSlot(0));
                }
            }

            if (size == 0) {
                pedestals.clear();
                for (BlockPos point : positionsToCheckHigher) {
                    if (level.getBlockEntity(point) instanceof PedestalBlockEntity) {
                        pedestals.add(point);
                    }
                }
                if (pedestals.size() == 8) {
                    size = 3;
                    for (BlockPos pedestalPosition : pedestals) {
                        items.add(((PedestalBlockEntity) level.getBlockEntity(pedestalPosition)).inventory.getStackInSlot(0));
                    }
                }
            }

            RitualRecipeInput recipeInput = new RitualRecipeInput(items);
            Optional<RecipeHolder<RitualRecipe>> ritual = level.getRecipeManager().getRecipeFor(ModRecipes.RITUAL.get(), recipeInput, level);
            SimpleRituals.LOGGER.debug("Checking recipe: " + ritual + " From input: " + recipeInput);
            if (ritual.isPresent()) {
                if (ritual.get().value().getSize() == size) {
                    PedestalBlockEntity pedestalBlockEntity = (PedestalBlockEntity) level.getBlockEntity(pos);
                    pedestalBlockEntity.inventory.setStackInSlot(0, ritual.get().value().assemble(recipeInput, level.registryAccess()));
                    ServerLevel serverLevel = level.getServer().getLevel(level.dimension());

                    if (ritual.get().value().getSpawnLightning()) {
                        EntityType.LIGHTNING_BOLT.spawn(serverLevel, pos, MobSpawnType.TRIGGERED);
                    }
                    level.playSound(null, pos, SoundEvents.PLAYER_LEVELUP, SoundSource.BLOCKS, 1f, 1.5f);

                    serverLevel.sendParticles(ParticleTypes.END_ROD, pos.getX() + .5, pos.getY() + .9, pos.getZ() + .5,
                            10, .5, .05, .5, .05); // Central Pedestal Particles

                    for (BlockPos pedestalPosition : pedestals) {
                        pedestalBlockEntity = (PedestalBlockEntity) level.getBlockEntity(pedestalPosition);
                        pedestalBlockEntity.clearContents();

                        serverLevel.sendParticles(ParticleTypes.WITCH, pedestalPosition.getX() + .5, pedestalPosition.getY() + 1.5, pedestalPosition.getZ()+ .5,
                                20, .1, .1, .1, .1); // Side Pedestal Particles
                    }
                    return InteractionResult.SUCCESS;
                }
            }
            player.displayClientMessage(Component.translatable("message.simple_rituals.invalid_ritual_recipe_warning"), true);
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public @Nullable <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return level.isClientSide() ? null : createTickerHelper(blockEntityType, ModBlockEntities.PEDESTAL_BLOCK_ENTITY.get(), PedestalBlockEntity::tick);
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
        BlockPos[] positionsToCheck = {
                blockPosOffset(pos, 2, 0, 0),
                blockPosOffset(pos, -2, 0, 0),
                blockPosOffset(pos, 0, 0, 2),
                blockPosOffset(pos, 0, 0, -2),

                blockPosOffset(pos, 3, 0, 0),
                blockPosOffset(pos, -3, 0, 0),
                blockPosOffset(pos, 0, 0, 3),
                blockPosOffset(pos, 0, 0, -3),
                blockPosOffset(pos, 2, 0, 2),
                blockPosOffset(pos, 2, 0, -2),
                blockPosOffset(pos, -2, 0, 2),
                blockPosOffset(pos, -2, 0, -2)
        };
        int amountChanged = 0;
        for (BlockPos point : positionsToCheck) {
            if (level.getBlockEntity(point) instanceof PedestalBlockEntity pedestalBlockEntity) {
                if (pedestalBlockEntity.getMagicLevel() != 0) {
                    pedestalBlockEntity.setMagicLevel(0);
                    amountChanged++;
                }
            }
        }
        if (!level.isClientSide() && amountChanged > 0) {
            level.playSound(null, pos, SoundEvents.ALLAY_ITEM_TAKEN, SoundSource.BLOCKS, 4f, 0.5f);
        }
    }

    private static BlockPos blockPosOffset(BlockPos origin, double x, double y, double z) {
        return BlockPos.containing(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
    }
}
