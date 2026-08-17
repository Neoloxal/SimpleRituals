package com.neoloxal.simple_rituals;

import com.mojang.logging.LogUtils;
import com.neoloxal.simple_rituals.advancment.ModAdvancementTriggers;
import com.neoloxal.simple_rituals.blocks.ModBlocks;
import com.neoloxal.simple_rituals.blocks.entity.ModBlockEntities;
import com.neoloxal.simple_rituals.client.renderer.PedestalBlockEntityRender;
import com.neoloxal.simple_rituals.items.ModItems;
import com.neoloxal.simple_rituals.recipe.ModRecipes;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import org.slf4j.Logger;

@Mod(SimpleRituals.MODID)
public class SimpleRituals {
    public static final String MODID = "simple_rituals";
    public static final Logger LOGGER = LogUtils.getLogger();

    public SimpleRituals(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModBlockEntities.register(modEventBus);

        ModRecipes.register(modEventBus);

        ModAdvancementTriggers.register(modEventBus);

        modEventBus.addListener(this::addCreative);

        NeoForge.EVENT_BUS.register(this);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == CreativeModeTabs.FUNCTIONAL_BLOCKS) {
            event.accept(ModBlocks.CENTRAL_PEDESTAL.get());
            event.insertAfter(ModBlocks.CENTRAL_PEDESTAL.toStack(), ModBlocks.PEDESTAL.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        } else if (event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.insertAfter(Items.EGG.getDefaultInstance(), ModItems.EMPTY_SPAWN_EGG.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        } else if (event.getTabKey() == CreativeModeTabs.SPAWN_EGGS) {
            event.insertBefore(Items.ALLAY_SPAWN_EGG.getDefaultInstance(), ModItems.EMPTY_SPAWN_EGG.toStack(), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    }

    @EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void clientSetup(FMLCommonSetupEvent events) {

        }

        @SubscribeEvent
        public static void registerBlockEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
            event.registerBlockEntityRenderer(ModBlockEntities.PEDESTAL_BLOCK_ENTITY.get(), PedestalBlockEntityRender::new);
        }

        @SubscribeEvent
        public static void registerModels(ModelEvent.RegisterAdditional event) {
            event.register(ModelResourceLocation.standalone(
                    ResourceLocation.fromNamespaceAndPath(MODID, "block/central_pedestal")
            ));
            event.register(ModelResourceLocation.standalone(
                    ResourceLocation.fromNamespaceAndPath(MODID, "block/pedestal")
            ));
            event.register(ModelResourceLocation.standalone(
                    ResourceLocation.fromNamespaceAndPath(MODID, "block/magic_layer_none")
            ));
            event.register(ModelResourceLocation.standalone(
                    ResourceLocation.fromNamespaceAndPath(MODID, "block/magic_layer_central")
            ));
            event.register(ModelResourceLocation.standalone(
                    ResourceLocation.fromNamespaceAndPath(MODID, "block/magic_layer_1")
            ));
            event.register(ModelResourceLocation.standalone(
                    ResourceLocation.fromNamespaceAndPath(MODID, "block/magic_layer_2")
            ));
            event.register(ModelResourceLocation.standalone(
                    ResourceLocation.fromNamespaceAndPath(MODID, "block/magic_layer_3")
            ));
        }
    }
}
