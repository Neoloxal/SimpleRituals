package com.neoloxal.simple_rituals.client.renderer;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.neoloxal.simple_rituals.SimpleRituals;
import com.neoloxal.simple_rituals.blocks.entity.PedestalBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.lwjgl.opengl.GPU_DEVICE;

public class PedestalBlockEntityRender implements BlockEntityRenderer<PedestalBlockEntity> {
    public PedestalBlockEntityRender(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(PedestalBlockEntity pedestalBlockEntity, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight, int packedOverlay) {
        ModelResourceLocation model = ModelResourceLocation.standalone(
                ResourceLocation.fromNamespaceAndPath(SimpleRituals.MODID, "block/magic_layer_none"));
        if (pedestalBlockEntity.central) {
            model = ModelResourceLocation.standalone(
                    ResourceLocation.fromNamespaceAndPath(SimpleRituals.MODID, "block/magic_layer_none"));
        } else if (pedestalBlockEntity.magic_level == 0) {
            model = ModelResourceLocation.standalone(
                    ResourceLocation.fromNamespaceAndPath(SimpleRituals.MODID, "block/magic_layer_none"));
        } else if (pedestalBlockEntity.magic_level == 1) {
            model = ModelResourceLocation.standalone(
                    ResourceLocation.fromNamespaceAndPath(SimpleRituals.MODID, "block/magic_layer_1"));
        } else if (pedestalBlockEntity.magic_level == 2) {
            model = ModelResourceLocation.standalone(
                    ResourceLocation.fromNamespaceAndPath(SimpleRituals.MODID, "block/magic_layer_2"));
        } else if (pedestalBlockEntity.magic_level == 3) {
            model = ModelResourceLocation.standalone(
                    ResourceLocation.fromNamespaceAndPath(SimpleRituals.MODID, "block/magic_layer_3"));
        }

        BakedModel bakedModel = Minecraft.getInstance().getModelManager().getModel(model);

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack stack = pedestalBlockEntity.inventory.getStackInSlot(0);

        poseStack.pushPose();

        if (pedestalBlockEntity.magic_level == 0 && !pedestalBlockEntity.central) {
            poseStack.translate(.5f, 1, .5f);
            poseStack.mulPose(Axis.XP.rotationDegrees(90));
            poseStack.mulPose(Axis.ZP.rotationDegrees((pedestalBlockEntity.randomizer * 15) % 360));
        } else {
            float time = pedestalBlockEntity.getLevel().getGameTime() + pedestalBlockEntity.randomizer + partialTick;
            poseStack.translate(.5f, 1.5 + Math.sin(time/10) * .1f, .5f);
            float rotation = time * 2f % 360f;
            poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        }
        poseStack.scale(0.5f, 0.5f, 0.5f);

        itemRenderer.renderStatic(stack, ItemDisplayContext.FIXED, getLightLevel(pedestalBlockEntity.getLevel(), pedestalBlockEntity.getBlockPos()),
                OverlayTexture.NO_OVERLAY, poseStack, multiBufferSource, pedestalBlockEntity.getLevel(), 1);
        poseStack.popPose();

        VertexConsumer buffer = multiBufferSource.getBuffer(RenderType.translucent());
        ModelBlockRenderer blockRenderer = Minecraft.getInstance().getBlockRenderer().getModelRenderer();

        RenderSystem.disableCull();
        poseStack.pushPose();
        poseStack.translate(0.5f, 0, 0.5f);
        float time = pedestalBlockEntity.getLevel().getGameTime() - pedestalBlockEntity.randomizer + partialTick;
        poseStack.mulPose(Axis.YP.rotationDegrees(time * (2f * pedestalBlockEntity.magic_level) % 360));
        poseStack.translate(-0.5f, Math.sin(time/(10f / (pedestalBlockEntity.magic_level / 5f))) * .15f , -0.5f);

        blockRenderer.renderModel(poseStack.last(), buffer, pedestalBlockEntity.getBlockState(), bakedModel,
                1.0f, 1.0f, 1.0f, packedLight, packedOverlay, ModelData.EMPTY, RenderType.translucent());
        poseStack.popPose();
    }

    private int getLightLevel(Level level, BlockPos pos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, pos);
        int sLight = level.getBrightness(LightLayer.SKY, pos);
        return LightTexture.pack(bLight, sLight);
    }
}
