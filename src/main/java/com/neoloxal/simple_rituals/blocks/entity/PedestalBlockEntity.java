package com.neoloxal.simple_rituals.blocks.entity;

import com.neoloxal.simple_rituals.advancment.ModAdvancementTriggers;
import com.neoloxal.simple_rituals.blocks.custom.PedestalBlock;
import com.neoloxal.simple_rituals.items.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PedestalBlockEntity extends BlockEntity {
    private int magicLevel = 0;
    private boolean central = false;
    private float randomizer = (float) Math.random() * 100;
    private UUID owner = null;

    public final ItemStackHandler inventory = new ItemStackHandler(1) {
        @Override
        protected int getStackLimit(int slot, ItemStack stack) {
            return 1;
        }

        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null && !level.isClientSide()) {
                if (magicLevel == 0) {randomizer = (float) Math.random() * 100;}
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
                if (inventory.getStackInSlot(slot).is(ModItems.UNKNOWN.get())) {
                    inventory.setStackInSlot(slot, ItemStack.EMPTY);
                }
            }
        }
    };

    public PedestalBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.PEDESTAL_BLOCK_ENTITY.get(), pos, blockState);
    }

    public void clearContents() {
        inventory.setStackInSlot(0, ItemStack.EMPTY);
    }

    public void drops() {
        SimpleContainer inv = new SimpleContainer(inventory.getSlots());
        for (int i = 0; i < inventory.getSlots(); i++) {
            inv.setItem(i, inventory.getStackInSlot(i));
        }

        Containers.dropContents(this.level, this.worldPosition, inv);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putBoolean("central", this.central);
        tag.putInt("magic_level", this.magicLevel);
        tag.put("inventory", this.inventory.serializeNBT(registries));
        tag.putFloat("randomizer", this.randomizer);
        if (this.owner != null) {
            tag.putUUID("owner", this.owner);
        }
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.central = tag.getBoolean("central");
        this.magicLevel = tag.getInt("magic_level");
        this.inventory.deserializeNBT(registries, tag.getCompound("inventory"));
        this.randomizer = tag.getFloat("randomizer");
        if (tag.hasUUID("owner")) {
            this.owner = tag.getUUID("owner");
        }
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag, registries);
        return tag;
    }

    @Override
    public @Nullable Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, PedestalBlockEntity blockEntity) {
        if (!level.isClientSide()) {
            if (blockEntity.central) {
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
                int pedestalCount = 0;

                for (BlockPos point : positionsToCheckBasic) {
                    if (level.getBlockState(point).getBlock() instanceof PedestalBlock) {
                        pedestals.add(point);
                        pedestalCount++;
                    }
                }

                int amountChanged = 0;
                if (pedestalCount == 4) {
                    for (BlockPos pedestalPos : pedestals) {
                        PedestalBlockEntity pedestalBlockEntity = (PedestalBlockEntity) level.getBlockEntity(pedestalPos);
                        if (pedestalBlockEntity.getMagicLevel() != 2) {
                            pedestalBlockEntity.setMagicLevel(2);
                            amountChanged++;
                        }
                    }
                    if (amountChanged > 0) {
                        level.playSound(null, pos, SoundEvents.ALLAY_ITEM_GIVEN, SoundSource.BLOCKS, 4f, 0.5f);
                    }
                    return;
                } else if (pedestalCount == 2) {
                    for (BlockPos pedestalPos : pedestals) {
                        PedestalBlockEntity pedestalBlockEntity = (PedestalBlockEntity) level.getBlockEntity(pedestalPos);
                        if (pedestalBlockEntity.getMagicLevel() != 1) {
                            pedestalBlockEntity.setMagicLevel(1);
                            amountChanged++;
                        }
                    }
                    if (amountChanged > 0) {
                        level.playSound(null, pos, SoundEvents.ALLAY_ITEM_GIVEN, SoundSource.BLOCKS, 4f, 0.5f);
                    }
                    return;
                } else {
                    for (BlockPos point : positionsToCheckBasic) {
                        if (level.getBlockEntity(point) instanceof PedestalBlockEntity pedestalBlockEntity) {
                            if (pedestalBlockEntity.getMagicLevel() != 0) {
                                pedestalBlockEntity.setMagicLevel(0);
                                amountChanged++;
                            }
                        }
                    }
                    if (amountChanged > 0) {
                        level.playSound(null, pos, SoundEvents.ALLAY_ITEM_TAKEN, SoundSource.BLOCKS, 4f, 0.5f);
                    }
                }

                pedestals = new ArrayList<>();
                pedestalCount = 0;
                for (BlockPos point : positionsToCheckHigher) {
                    if (level.getBlockState(point).getBlock() instanceof PedestalBlock) {
                        pedestals.add(point);
                        pedestalCount++;
                    }
                }

                if (pedestalCount == 8) {
                    for (BlockPos pedestalPos : pedestals) {
                        PedestalBlockEntity pedestalBlockEntity = (PedestalBlockEntity) level.getBlockEntity(pedestalPos);
                        if (pedestalBlockEntity.getMagicLevel() != 3) {
                            pedestalBlockEntity.setMagicLevel(3);
                            amountChanged++;
                        }
                    }
                    if (amountChanged > 0) {
                        level.playSound(null, pos, SoundEvents.ALLAY_ITEM_GIVEN, SoundSource.BLOCKS, 4f, 0.5f);
                        if (level instanceof ServerLevel serverLevel) {
                            UUID ownerUuid = blockEntity.getOwner();
                            if (ownerUuid != null) {
                                ServerPlayer ownerPlayer = serverLevel.getServer().getPlayerList().getPlayer(ownerUuid);
                                if (ownerPlayer != null) {
                                    ModAdvancementTriggers.MAX_TIER_RITUAL_TRIGGER.get().trigger(ownerPlayer);
                                }
                            }
                        }
                    }
                } else {
                    for (BlockPos point : positionsToCheckHigher) {
                        if (level.getBlockEntity(point) instanceof PedestalBlockEntity pedestalBlockEntity) {
                            if (pedestalBlockEntity.getMagicLevel() != 0) {
                                pedestalBlockEntity.setMagicLevel(0);
                                amountChanged++;
                            }
                        }
                    }
                    if (amountChanged > 0) {
                        level.playSound(null, pos, SoundEvents.ALLAY_ITEM_TAKEN, SoundSource.BLOCKS, 4f, 0.5f);
                    }
                }
            }
        }
    }

    private static BlockPos blockPosOffset(BlockPos origin, double x, double y, double z) {
        return BlockPos.containing(origin.getX() + x, origin.getY() + y, origin.getZ() + z);
    }

    public int getMagicLevel() {
        return this.magicLevel;
    }

    public void setMagicLevel(int magicLevel) {
        this.magicLevel = magicLevel;

        this.setChanged();
        if (level != null && !level.isClientSide()) {
            this.level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public boolean getCentral() {
        return this.central;
    }

    public void setCentral(boolean central) {
        this.central = central;

        this.setChanged();
        if (level != null && !level.isClientSide()) {
            this.level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public float getRandomizer() {
        return this.randomizer;
    }

    public void setRandomizer(float randomizer) {
        this.randomizer = randomizer;

        this.setChanged();
        if (level != null && !level.isClientSide()) {
            this.level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
        }
    }

    public UUID getOwner() {
        return this.owner;
    }

    public void setOwner(Player owner) {
        if (owner != null) {
            this.owner = owner.getUUID();

            this.setChanged();
            if (level != null && !level.isClientSide()) {
                this.level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
            }
        }
    }
}
